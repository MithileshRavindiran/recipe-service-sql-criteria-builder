package nl.abn.api.receipe.service;

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
import nl.abn.api.receipe.repository.IngredientRepository;
import nl.abn.api.receipe.repository.RecipeRepository;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RecipeServiceTest {

    @InjectMocks
    RecipeService recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private RecipeMapper recipeMapper;

    @Test
    public void testGetAllRecipes() {
        List<RecipeEntity> recipeList = getRecipeEntities();
        when(recipeRepository.findAll()).thenReturn(recipeList);
        assertNotNull(recipeService.getAllRecipes());
    }

    @Test
    public void testAddRecipe_Vegetarian() {
        List<IngredientRequest> ingredientRequestList = new ArrayList<>();
        IngredientRequest ingredientRequest = new IngredientRequest();
        ingredientRequest.setIngredientName("Tomato");
        ingredientRequestList.add(ingredientRequest);

        RecipeRequest request = RecipeRequest.builder()
                .category(Category.VEGETARIAN.getCode())
                .recipeName("Italian")
                .instructions("Oven")
                .numberOfServings(4)
                .ingredientList(ingredientRequestList)
                .build();

        IngredientEntity ingredientEntity = new IngredientEntity();
        ingredientEntity.setIngredientName("Tomato");
         when(ingredientRepository.findByIngredientName(any())).thenReturn(Optional.of(ingredientEntity));
         recipeService.addRecipe(request);
        verify(recipeRepository, times(1)).save(any());
    }

    @Test
    public void testAddRecipe_NonVegetarian() {
        List<IngredientRequest> ingredientRequestList = new ArrayList<>();
        IngredientRequest ingredientRequest = new IngredientRequest();
        ingredientRequest.setIngredientName("Tomato");
        ingredientRequestList.add(ingredientRequest);
        RecipeRequest request = RecipeRequest.builder()
                .category(Category.NON_VEGETARIAN.getCode())
                .recipeName("Italian")
                .instructions("Oven")
                .numberOfServings(4)
                .ingredientList(ingredientRequestList)
                .build();
        recipeService.addRecipe(request);
        verify(recipeRepository, times(1)).save(any());
    }

    @Test
    public void testAddRecipe_CategoryNotSupported() {
        RecipeRequest request = RecipeRequest.builder()
                .category("asian")
                .recipeName("Italian")
                .instructions("Oven")
                .numberOfServings(4)
                .build();
        assertThrows(IllegalArgumentException.class, () -> {
            recipeService.addRecipe(request);
        });
    }

    @Test
    public void testUpdateRecipe_VegetarianCategory() {
        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setRecipeName("Italian");
        recipeEntity.setCategory(Category.VEGETARIAN);
        recipeEntity.setInstructions("Oven");
        recipeEntity.setNumberOfServings(4);

        Set<IngredientEntity> ingredientRequestEntityList = new HashSet<>();
        IngredientEntity ingredientEntity = new IngredientEntity();
        ingredientEntity.setId(1L);
        ingredientEntity.setIngredientName("Tomato");
        ingredientRequestEntityList.add(ingredientEntity);

        recipeEntity.setIngredients(ingredientRequestEntityList);

        RecipeUpdateRequest request = getRecipeUpdateRequest();
        when(recipeRepository.findById(any())).thenReturn(Optional.of(recipeEntity));
        when(ingredientRepository.findByIngredientName(any())).thenReturn(Optional.of(ingredientEntity));
        recipeService.updateRecipe(1L, request);
        verify(recipeRepository, times(1)).save(any());
    }

    private RecipeUpdateRequest getRecipeUpdateRequest() {
        List<IngredientUpdateRequest> ingredientRequestList = new ArrayList<>();
        IngredientUpdateRequest ingredientRequest = new IngredientUpdateRequest();
        ingredientRequest.setIngredientName("Tomato");
        ingredientRequest.setRequired(true);
        ingredientRequestList.add(ingredientRequest);
        RecipeUpdateRequest request = RecipeUpdateRequest.builder()
                .category(Category.VEGETARIAN.getCode())
                .recipeName("Italian")
                .instructions("Oven")
                .numberOfServings(4)
                .ingredientList(ingredientRequestList)
                .build();
        return request;
    }

    @Test
    public void testUpdateRecipe_NonVegetarianCategory() {
        Set<IngredientEntity> ingredientRequestEntityList = new HashSet<>();
        IngredientEntity ingredientEntity = new IngredientEntity();
        ingredientEntity.setId(1L);
        ingredientEntity.setIngredientName("Tomato");
        ingredientRequestEntityList.add(ingredientEntity);
        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setRecipeName("Italian");
        recipeEntity.setCategory(Category.NON_VEGETARIAN);
        recipeEntity.setInstructions("Oven");
        recipeEntity.setNumberOfServings(4);
        recipeEntity.setIngredients(ingredientRequestEntityList);
        List<IngredientUpdateRequest> ingredientRequestList = new ArrayList<>();
        IngredientUpdateRequest ingredientRequest = new IngredientUpdateRequest();
        ingredientRequest.setIngredientName("potato");
        ingredientRequest.setRequired(false);
        IngredientUpdateRequest ingredientSecondRequest = new IngredientUpdateRequest();
        ingredientSecondRequest.setIngredientName("lemon");
        ingredientRequest.setRequired(true);
        ingredientRequestList.add(ingredientRequest);
        ingredientRequestList.add(ingredientSecondRequest);

        RecipeUpdateRequest request = RecipeUpdateRequest.builder()
                .category(Category.NON_VEGETARIAN.getCode())
                .recipeName("Italian")
                .instructions("Oven")
                .numberOfServings(4)
                .ingredientList(ingredientRequestList)
                .build();
        when(recipeRepository.findById(any())).thenReturn(Optional.of(recipeEntity));
        when(ingredientRepository.findByIngredientName(any())).thenReturn(Optional.of(ingredientEntity));
        recipeService.updateRecipe(1L ,request);
        verify(recipeRepository, times(1)).save(any());
    }

    @Test
    public void testUpdateRecipe_InvalidCategory() {
        RecipeUpdateRequest request = RecipeUpdateRequest.builder()
                .category("Vegan")
                .recipeName("Italian")
                .instructions("Oven")
                .numberOfServings(4)
                .build();
        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setRecipeName("Italian");
        recipeEntity.setCategory(Category.NON_VEGETARIAN);
        recipeEntity.setInstructions("Oven");
        recipeEntity.setNumberOfServings(4);
        when(recipeRepository.findById(any())).thenReturn(Optional.of(recipeEntity));
        assertThrows(IllegalArgumentException.class, () -> {
            recipeService.updateRecipe(1L ,request);
        });
    }

    @Test
    public void testFilterByCriteria () {
        List<IngredientFilter> filterList = new ArrayList<>();

        IngredientFilter ingredientFilter = new IngredientFilter();
        ingredientFilter.setIngredientName("Tomato");
        ingredientFilter.setIncludeIngredient(true);
        filterList.add(ingredientFilter);

        Set<IngredientEntity> ingredientRequestEntityList = new HashSet<>();
        IngredientEntity ingredientEntity = new IngredientEntity();
        ingredientEntity.setId(1L);
        ingredientEntity.setIngredientName("Tomato");
        ingredientRequestEntityList.add(ingredientEntity);
        List<RecipeEntity> entityList = new ArrayList<>();
        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setRecipeName("Italian");
        recipeEntity.setCategory(Category.VEGETARIAN);
        recipeEntity.setInstructions("Oven");
        recipeEntity.setNumberOfServings(4);
        recipeEntity.setIngredients(ingredientRequestEntityList);
        entityList.add(recipeEntity);
        RecipeFilterRequest filterRequest = RecipeFilterRequest.builder()
                .numberOfServings(4)
                .category("vegetarian")
                .textSearch("oven")
                .ingredientFilter(filterList).build();
        when(ingredientRepository.findByIngredientName(any())).thenReturn(Optional.of(ingredientEntity));
        when(recipeRepository.filterRecipeByCriteria(any())).thenReturn(entityList);
        recipeService.searchRecipeByCriteria(filterRequest);
        verify(ingredientRepository, times(1)).findByIngredientName(any());
    }

    @Test
    public void testDeleteRecipe () {
        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setRecipeName("Italian");
        recipeEntity.setCategory(Category.NON_VEGETARIAN);
        recipeEntity.setInstructions("Oven");
        recipeEntity.setNumberOfServings(4);
        when(recipeRepository.findById(any())).thenReturn(Optional.of(recipeEntity));
        recipeService.deleteRecipe(1L);
        verify(recipeRepository, times(1)).delete(any());
    }

    private List<RecipeEntity> getRecipeEntities() {
        List<RecipeEntity> recipeList = new ArrayList<>();
        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setRecipeName("Italian");
        recipeEntity.setCategory(Category.VEGETARIAN);
        recipeEntity.setInstructions("Oven");
        recipeEntity.setNumberOfServings(4);
        recipeList.add(recipeEntity);
        return recipeList;
    }
}
