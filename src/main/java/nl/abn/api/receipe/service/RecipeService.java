package nl.abn.api.receipe.service;

import lombok.extern.slf4j.Slf4j;
import nl.abn.api.receipe.domain.mapper.RecipeMapper;
import nl.abn.api.receipe.domain.model.Recipe;
import nl.abn.api.receipe.domain.request.IngredientFilter;
import nl.abn.api.receipe.domain.request.IngredientRequest;
import nl.abn.api.receipe.domain.request.IngredientUpdateRequest;
import nl.abn.api.receipe.domain.request.RecipeFilterRequest;
import nl.abn.api.receipe.domain.request.RecipeRequest;
import nl.abn.api.receipe.domain.request.RecipeUpdateRequest;
import nl.abn.api.receipe.entity.Category;
import nl.abn.api.receipe.entity.IngredientEntity;
import nl.abn.api.receipe.entity.RecipeEntity;
import nl.abn.api.receipe.exception.ResourceNotFoundException;
import nl.abn.api.receipe.repository.IngredientRepository;
import nl.abn.api.receipe.repository.RecipeRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The type Recipe service.
 */
@Service
@Slf4j
public class RecipeService {

    private RecipeRepository recipeRepository;

    private IngredientRepository ingredientRepository;

    private RecipeMapper recipeMapper;

    /**
     * Instantiates a new Recipe service.
     *
     * @param recipeRepository     the recipe repository
     * @param ingredientRepository the ingredient repository
     * @param recipeMapper         the recipe mapper
     */
    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository, RecipeMapper recipeMapper) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.recipeMapper = recipeMapper;
    }

    /**
     * Gets all recipes.
     *
     * @return the all recipes
     */
    @Transactional(readOnly = true)
    public List<Recipe> getAllRecipes() {
        return recipeMapper.map(recipeRepository.findAll());
    }

    /**
     * Add recipe recipe.
     *
     * @param recipeRequest the recipe request
     * @return the recipe
     * @throws Exception the exception
     */
    @Transactional
    public Recipe addRecipe(final RecipeRequest recipeRequest) {
        RecipeEntity recipeEntity = new RecipeEntity();
        mapCategory(recipeRequest, recipeEntity);
        recipeEntity.setRecipeName(recipeRequest.getRecipeName());
        recipeEntity.setInstructions(recipeRequest.getInstructions());
        recipeEntity.setNumberOfServings(recipeRequest.getNumberOfServings());
        mapIngredients(recipeRequest, recipeEntity);
        return recipeMapper.map(recipeRepository.save(recipeEntity));
    }

    private void mapCategory(final RecipeRequest recipeRequest, final RecipeEntity recipeEntity) {
        if (recipeRequest.getCategory().equals(Category.VEGETARIAN.getCode())) {
            recipeEntity.setCategory(Category.VEGETARIAN);
        } else if (recipeRequest.getCategory().equals(Category.NON_VEGETARIAN.getCode())) {
            recipeEntity.setCategory(Category.NON_VEGETARIAN);
        } else {
            throw new IllegalArgumentException("Category [" + recipeRequest.getCategory()
                    + "] not supported.");
        }
    }

    private void mapIngredients(final RecipeRequest recipeRequest, final RecipeEntity recipeEntity) {
        if (ObjectUtils.isNotEmpty(recipeRequest.getIngredientList())) {
            for (IngredientRequest ingredient : recipeRequest.getIngredientList()) {
                if (StringUtils.isNotEmpty(ingredient.getIngredientName())) {
                    Optional<IngredientEntity> specificIngredient = ingredientRepository.
                            findByIngredientName(ingredient.getIngredientName());
                    if (specificIngredient.isPresent()) {
                        log.info("Associate existing Ingredient {} to recipe", ingredient.getIngredientName());
                        recipeEntity.addIngredient(specificIngredient.get());
                    } else {
                        IngredientEntity ingredientEntity = new IngredientEntity();
                        ingredientEntity.setIngredientName(ingredient.getIngredientName());
                        log.info("Add a new Ingredient {}", ingredient.getIngredientName());
                        recipeEntity.addIngredient(ingredientEntity);
                    }
                }
            }
        }
    }

    /**
     * Update recipe recipe.
     *
     * @param id            the id
     * @param recipeRequest the recipe request
     * @return the recipe
     * @throws Exception the exception
     */
    @Transactional
    public Recipe updateRecipe(final Long id, final RecipeUpdateRequest recipeRequest) {
        RecipeEntity recipeEntity = getRecipeEntityById(id);
        List<String> ingredientList = recipeEntity.getIngredients().stream()
                .map(IngredientEntity::getIngredientName).collect(Collectors.toList());
        updateRecipeValues(recipeRequest, recipeEntity);
        updateIngredientValues(recipeRequest, recipeEntity, ingredientList);
        return recipeMapper.map(recipeRepository.save(recipeEntity));
    }

    private RecipeEntity getRecipeEntityById(final Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe Not Found with id = " + id));
    }

    private void updateIngredientValues(final RecipeUpdateRequest recipeRequest,
                                        final RecipeEntity recipeEntity, final List<String> ingredientList) {
        if (ObjectUtils.isNotEmpty(recipeRequest.getIngredientList())) {
            for (IngredientUpdateRequest ingredient : recipeRequest.getIngredientList()) {
                ruleToAddOrRemoveIngredient(recipeEntity, ingredientList, ingredient);
            }
        }
    }

    private void ruleToAddOrRemoveIngredient(final RecipeEntity recipeEntity,
                                             final List<String> ingredientList, final IngredientUpdateRequest ingredient) {
        if (ingredient.getIngredientName() != null) {
            IngredientEntity specificIngredient = getIngredientEntityById(ingredient);
            if (ingredient.isRequired()) {
                addIngredientToRecipe(recipeEntity, ingredientList, ingredient, specificIngredient);
            } else {
                recipeEntity.removeIngredient(specificIngredient.getId());
            }
        }
    }

    private IngredientEntity getIngredientEntityById(final IngredientUpdateRequest ingredient) {
        return ingredientRepository.findByIngredientName(ingredient.getIngredientName())
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient Not Found = " + ingredient.getIngredientName()));
    }

    private void updateRecipeValues(final RecipeUpdateRequest recipeRequest, final RecipeEntity recipeEntity) {
        if (recipeRequest.getRecipeName() != null) {
            recipeEntity.setRecipeName(recipeRequest.getRecipeName());
        }
        if (recipeRequest.getInstructions() != null) {
            recipeEntity.setInstructions(recipeRequest.getInstructions());
        }
        if (recipeRequest.getNumberOfServings() != null) {
            recipeEntity.setNumberOfServings(recipeRequest.getNumberOfServings());
        }
        updateCategory(recipeRequest, recipeEntity);
    }

    private void updateCategory(final RecipeUpdateRequest recipeRequest, final RecipeEntity recipeEntity) {
        if (recipeRequest.getCategory() != null) {
            if (recipeRequest.getCategory().equals(Category.VEGETARIAN.getCode())) {
                recipeEntity.setCategory(Category.VEGETARIAN);
            } else if (recipeRequest.getCategory().equals(Category.NON_VEGETARIAN.getCode())) {
                recipeEntity.setCategory(Category.NON_VEGETARIAN);
            } else {
                throw new IllegalArgumentException("Category [" + recipeRequest.getCategory()
                        + "] not supported.");
            }
        }
    }

    private void addIngredientToRecipe(final RecipeEntity recipeEntity, final List<String> ingredientList,
                                       final IngredientUpdateRequest ingredient, final IngredientEntity specificIngredient) {
        if (!ingredientList.contains(ingredient.getIngredientName())) {
            log.info("Associate ingredient {} to recipe {}", ingredient.getIngredientName(), recipeEntity.getId());
            recipeEntity.addIngredient(specificIngredient);
        } else {
            log.info("Recipe is already associated with the ingredient {}", ingredient.getIngredientName());
        }
    }

    /**
     * Delete recipe.
     *
     * @param id the id
     */
    @Transactional
    public void deleteRecipe(final Long id) {
        RecipeEntity specificRecipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe Not Found with id = " + id));
        deleteRecipeWithIngredients(specificRecipe);
    }

    private void deleteRecipeWithIngredients(final RecipeEntity recipeEntity) {
        recipeEntity.removeIngredientsFromRecipe();
        recipeRepository.delete(recipeEntity);
    }

    /**
     * Search recipe by criteria list.
     *
     * @param recipeFilterRequest the recipe filter request
     * @return the list
     */
    @Transactional(readOnly = true)
    public List<Recipe> searchRecipeByCriteria(final RecipeFilterRequest recipeFilterRequest) {
        if (ObjectUtils.isNotEmpty(recipeFilterRequest.getIngredientFilter())) {
            for (IngredientFilter filterValue : recipeFilterRequest.getIngredientFilter()) {
                ingredientRepository.findByIngredientName(filterValue.getIngredientName())
                        .orElseThrow(() -> new ResourceNotFoundException("Ingredient Not Found = " + filterValue.getIngredientName()));
            }
        }
        return recipeMapper.map(recipeRepository.filterRecipeByCriteria(recipeFilterRequest));
    }
}
