package nl.abn.api.receipe.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.abn.api.receipe.domain.model.Recipe;
import nl.abn.api.receipe.domain.request.RecipeFilterRequest;
import nl.abn.api.receipe.domain.request.RecipeRequest;
import nl.abn.api.receipe.domain.request.RecipeUpdateRequest;
import nl.abn.api.receipe.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * The type Recipe controller.
 */
@RestController
@RequestMapping("/api/recipe")
@Slf4j
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;

    /**
     * Gets all recipes.
     *
     * @return the all recipes
     */
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        List<Recipe> allRecipes = recipeService.getAllRecipes();
        return allRecipes.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(allRecipes, HttpStatus.OK);
    }

    /**
     * Search recipe response entity.
     *
     * @param recipeFilterRequest the recipe filter request
     * @return the response entity
     */
    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> searchRecipe(@Valid @RequestBody final RecipeFilterRequest recipeFilterRequest) {
        List<Recipe> recipeEntities = recipeService.searchRecipeByCriteria(recipeFilterRequest);
        return recipeEntities.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(recipeEntities, HttpStatus.OK);
    }

    /**
     * Add recipe response entity.
     *
     * @param recipeRequest the recipe request
     * @return the response entity
     * @throws IllegalArgumentException the exception
     */
    @PostMapping
    public ResponseEntity<Recipe> addRecipe(@Valid @RequestBody final RecipeRequest recipeRequest) {
        return new ResponseEntity<>(recipeService.addRecipe(recipeRequest), HttpStatus.CREATED);
    }

    /**
     * Update recipe response entity.
     *
     * @param id                  the id
     * @param recipeUpdateRequest the recipe update request
     * @return the response entity
     * @throws IllegalArgumentException the exception
     */
    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable final Long id,
                                               @Valid @RequestBody final RecipeUpdateRequest recipeUpdateRequest) {
        return new ResponseEntity<>(recipeService.updateRecipe(id, recipeUpdateRequest), HttpStatus.OK);
    }

    /**
     * Delete recipe response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteRecipe(@PathVariable final Long id) {
        recipeService.deleteRecipe(id);
        log.info("Recipe id {} Successfully deleted", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
