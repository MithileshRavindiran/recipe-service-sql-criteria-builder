package nl.abn.api.receipe.controller;

import lombok.extern.slf4j.Slf4j;
import nl.abn.api.receipe.domain.model.IngredientModel;
import nl.abn.api.receipe.domain.request.IngredientRequest;
import nl.abn.api.receipe.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * The type Ingredient controller.
 */
@RestController
@RequestMapping("/api/ingredient")
@Slf4j
public class IngredientController {

    private final IngredientService ingredientService;

    /**
     * Instantiates a new Ingredient controller.
     *
     * @param ingredientService the ingredient service
     */
    public IngredientController(final IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    /**
     * Gets all ingredient.
     *
     * @return the all ingredient
     */
    @GetMapping
    public ResponseEntity<List<IngredientModel>> getAllIngredient() {
        List<IngredientModel> allIngredients = ingredientService.getAllIngredients();
        return allIngredients.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(allIngredients, HttpStatus.OK);
    }

    /**
     * Add ingredient response entity.
     *
     * @param ingredientRequest the ingredient request
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<IngredientModel> addIngredient(@Valid @RequestBody final IngredientRequest ingredientRequest) {
        IngredientModel ingredient = ingredientService.addIngredient(ingredientRequest);
        log.info("Ingredient {} Successfully persisted to the database", ingredientRequest.getIngredientName());
        return new ResponseEntity<>(ingredient, HttpStatus.CREATED);
    }
}
