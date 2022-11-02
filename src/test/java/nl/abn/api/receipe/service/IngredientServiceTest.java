package nl.abn.api.receipe.service;

import nl.abn.api.receipe.domain.mapper.IngredientMapper;
import nl.abn.api.receipe.domain.mapper.RecipeMapper;
import nl.abn.api.receipe.domain.request.IngredientRequest;
import nl.abn.api.receipe.entity.IngredientEntity;
import nl.abn.api.receipe.repository.IngredientRepository;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IngredientServiceTest {

    @InjectMocks
    private IngredientService ingredientService;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private IngredientMapper ingredientMapper;

    @Test
    public void testGetAllIngredients() {
        List<IngredientEntity> ingredientEntityList = new ArrayList<>();
        IngredientEntity ingredientEntity = new IngredientEntity();
        ingredientEntity.setIngredientName("Tomato");
        ingredientEntity.setId(1L);
        ingredientEntityList.add(ingredientEntity);
        when(ingredientRepository.findAll()).thenReturn(ingredientEntityList);
        assertNotNull(ingredientService.getAllIngredients());
    }

    @Test
    public void testAddIngredient() throws Exception {
        IngredientRequest ingredientRequest = new IngredientRequest();
        ingredientRequest.setIngredientName("Tomato");
        ingredientService.addIngredient(ingredientRequest);
        verify(ingredientRepository, times(1)).save(any());
    }


    @Test
    public void testAddDuplicateIngredient() {
        IngredientRequest ingredientRequest = new IngredientRequest();
        ingredientRequest.setIngredientName("Tomato");
        when(ingredientRepository.findByIngredientName(any())).thenReturn(Optional.of(new IngredientEntity()));
        assertThrows(Exception.class, () -> {
            ingredientService.addIngredient(ingredientRequest);
        });
    }
}
