package nl.abn.api.receipe.controller;

import nl.abn.api.receipe.domain.model.Ingredient;
import nl.abn.api.receipe.domain.model.IngredientModel;
import nl.abn.api.receipe.domain.model.Recipe;
import nl.abn.api.receipe.domain.request.IngredientRequest;
import nl.abn.api.receipe.service.IngredientService;
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
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IngredientControllerTest {

    @InjectMocks
    private IngredientController ingredientController;

    @Mock
    private IngredientService ingredientService;


    @Test
    public void testGetAllRecipes() {
        List<IngredientModel> ingredientList = new ArrayList<>();
        IngredientModel ingredient = IngredientModel.builder().ingredientName("Potato")
                .build();

        ingredientList.add(ingredient);
        when(ingredientService.getAllIngredients()).thenReturn(ingredientList);
        ResponseEntity<List<IngredientModel>> response = ingredientController.getAllIngredient();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetAllRecipesNoContent() {
        List<IngredientModel> ingredientList = new ArrayList<>();
        when(ingredientService.getAllIngredients()).thenReturn(ingredientList);
        ResponseEntity<List<IngredientModel>> response = ingredientController.getAllIngredient();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void testAddIngredient() {
        IngredientModel ingredient = IngredientModel.builder().ingredientName("Potato")
                .build();
        when(ingredientService.addIngredient(any())).thenReturn(ingredient);
        assertThat(ingredientController.addIngredient(new IngredientRequest()).getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}
