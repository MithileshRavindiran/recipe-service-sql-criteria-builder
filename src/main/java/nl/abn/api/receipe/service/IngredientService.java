package nl.abn.api.receipe.service;

import lombok.extern.slf4j.Slf4j;
import nl.abn.api.receipe.domain.mapper.IngredientMapper;
import nl.abn.api.receipe.domain.model.IngredientModel;
import nl.abn.api.receipe.domain.request.IngredientRequest;
import nl.abn.api.receipe.entity.IngredientEntity;
import nl.abn.api.receipe.exception.RecipeServiceException;
import nl.abn.api.receipe.repository.IngredientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class IngredientService {

    private IngredientRepository ingredientRepository;

    private IngredientMapper ingredientMapper;

    public IngredientService(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
    }
    @Transactional(readOnly = true)
    public List<IngredientModel> getAllIngredients() {
        return ingredientMapper.map(ingredientRepository.findAll());
    }

    @Transactional
    public IngredientModel addIngredient(IngredientRequest ingredientRequest) {
        Optional<IngredientEntity> byIngredientName = ingredientRepository.findByIngredientName(ingredientRequest.getIngredientName());
        if(byIngredientName.isPresent()) {
            log.error("Ingredient {} already exist", ingredientRequest.getIngredientName());
            throw new RecipeServiceException("Ingredient already exist");
        }
        IngredientEntity ingredientEntity = new IngredientEntity();
        ingredientEntity.setIngredientName(ingredientRequest.getIngredientName());
        return ingredientMapper.map(ingredientRepository.save(ingredientEntity));
    }
}
