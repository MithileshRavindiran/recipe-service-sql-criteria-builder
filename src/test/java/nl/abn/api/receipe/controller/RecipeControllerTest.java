package nl.abn.api.receipe.controller;

import nl.abn.api.receipe.domain.model.Recipe;
import nl.abn.api.receipe.domain.request.RecipeFilterRequest;
import nl.abn.api.receipe.domain.request.RecipeRequest;
import nl.abn.api.receipe.domain.request.RecipeUpdateRequest;
import nl.abn.api.receipe.service.RecipeService;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RecipeControllerTest {

    @InjectMocks
    private RecipeController recipeController;

    @Mock
    private RecipeService recipeService;

    @Test
     public void testGetAllRecipes() {
        List<Recipe> recipeList = new ArrayList<>();
        recipeList.add(getRecipe()) ;
        when(recipeService.getAllRecipes()).thenReturn(recipeList);
        ResponseEntity<List<Recipe>> response = recipeController.getAllRecipes();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetAllRecipesNoContent() {
        List<Recipe> recipeList = new ArrayList<>();
        when(recipeService.getAllRecipes()).thenReturn(recipeList);
        ResponseEntity<List<Recipe>> response = recipeController.getAllRecipes();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void testSearchRecipes() {
        List<Recipe> recipeList = new ArrayList<>();
        RecipeFilterRequest recipeFilterRequest = new RecipeFilterRequest();
        recipeList.add(getRecipe());
        when(recipeService.searchRecipeByCriteria(any())).thenReturn(recipeList);
        ResponseEntity<List<Recipe>> response = recipeController.searchRecipe(recipeFilterRequest);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testSearchRecipesNoContent() {
        List<Recipe> recipeList = new ArrayList<>();
        RecipeFilterRequest recipeFilterRequest = new RecipeFilterRequest();
        when(recipeService.searchRecipeByCriteria(any())).thenReturn(recipeList);
        ResponseEntity<List<Recipe>> response = recipeController.searchRecipe(recipeFilterRequest);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void testAddRecipe() {
        RecipeRequest recipeRequest = new RecipeRequest();
        when(recipeService.addRecipe(any())).thenReturn(getRecipe());
        ResponseEntity<Recipe> response = recipeController.addRecipe(recipeRequest);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void testUpdateRecipe() {
        RecipeUpdateRequest recipeRequest = new RecipeUpdateRequest();
        when(recipeService.updateRecipe(any(), any())).thenReturn(getRecipe());
        ResponseEntity<Recipe> response = recipeController.updateRecipe(1L, recipeRequest);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testDeleteRecipe() {
        recipeController.deleteRecipe(1L);
        verify(recipeService, times(1)).deleteRecipe(any());
    }

    private Recipe getRecipe() {
        Recipe recipe = Recipe.builder().recipeName("Juice")
                .category("vegetarian")
                .numberOfServings(5)
                .instructions("In the Oven")
                .build();
        return recipe;
    }
}
