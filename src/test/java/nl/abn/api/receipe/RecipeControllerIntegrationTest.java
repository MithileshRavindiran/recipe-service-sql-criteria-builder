package nl.abn.api.receipe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import nl.abn.api.receipe.domain.model.Recipe;
import nl.abn.api.receipe.domain.request.IngredientRequest;
import nl.abn.api.receipe.domain.request.IngredientUpdateRequest;
import nl.abn.api.receipe.domain.request.RecipeRequest;
import nl.abn.api.receipe.domain.request.RecipeUpdateRequest;
import nl.abn.api.receipe.entity.RecipeEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecipeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipeControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String getRootUrl() {
        return "http://localhost:" + port;
    }

    @Test
    public void contextLoads() {

    }

    @Test
    public void testGetAllRecipes() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<Recipe[]> response = restTemplate.exchange(getRootUrl() + "/api/recipe",
                HttpMethod.GET, entity, Recipe[].class);

        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testAddRecipe() {
        List<IngredientRequest> ingredientList = new ArrayList<>();
        IngredientRequest ingredientRequest = new IngredientRequest();
        ingredientRequest.setIngredientName("Noodles");
        IngredientRequest secondIngredientRequest = new IngredientRequest();
        secondIngredientRequest.setIngredientName("rice");
        ingredientList.add(ingredientRequest);
        ingredientList.add(secondIngredientRequest);
        RecipeRequest recipe = RecipeRequest.builder()
                .recipeName("Chinese")
                .instructions("PLace it in the oven for 10 mins")
                .numberOfServings(4)
                .category("vegetarian")
                .ingredientList(ingredientList).build();

        ResponseEntity<Recipe> postResponse = restTemplate.postForEntity(getRootUrl() + "/api/recipe", recipe, Recipe.class);
        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());
        assertEquals(postResponse.getBody().getRecipeName(), "Chinese");
        assertEquals(postResponse.getStatusCode(), HttpStatus.CREATED);

    }

    @Test
    public void testUpdateRecipe() {
        long id = 1;
        List<IngredientUpdateRequest> ingredientList = new ArrayList<>();
        IngredientUpdateRequest ingredientRequest = new IngredientUpdateRequest();
        ingredientRequest.setIngredientName("Noodles");
        ingredientRequest.setRequired(false);
        IngredientUpdateRequest secondIngredientRequest = new IngredientUpdateRequest();
        secondIngredientRequest.setIngredientName("rice");
        IngredientUpdateRequest thirdIngredientRequest = new IngredientUpdateRequest();
        secondIngredientRequest.setIngredientName("Salt");
        ingredientRequest.setRequired(true);
        ingredientList.add(ingredientRequest);
        ingredientList.add(secondIngredientRequest);
        ingredientList.add(thirdIngredientRequest);
        RecipeUpdateRequest recipe = RecipeUpdateRequest.builder()
                .recipeName("Italian")
                .numberOfServings(5)
                .ingredientList(ingredientList).build();

         restTemplate.put(getRootUrl() + "/api/recipe" + id, recipe);
         Recipe updatedRecipe = restTemplate.getForObject(getRootUrl() + "/api/recipe" + id, Recipe.class);
         assertNotNull(updatedRecipe);
    }

    @Test
    public void testDeleteEmployee() {
        int id = 1;
        RecipeEntity recipeById = restTemplate.getForObject(getRootUrl() + "/api/recipe" + id, RecipeEntity.class);
        assertNotNull(recipeById);
        restTemplate.delete(getRootUrl() + "/employees/" + id);
        try {
            recipeById = restTemplate.getForObject(getRootUrl() + "/api/recipe" + id, RecipeEntity.class);
        } catch (final HttpClientErrorException e) {
            assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }
    }
}
