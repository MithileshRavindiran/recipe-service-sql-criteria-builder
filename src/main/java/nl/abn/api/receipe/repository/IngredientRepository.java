package nl.abn.api.receipe.repository;

import nl.abn.api.receipe.entity.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Ingredient repository.
 */
@Repository
public interface IngredientRepository extends JpaRepository<IngredientEntity,Long> {

    /**
     * Find by ingredient name optional.
     *
     * @param ingredientName the ingredient name
     * @return the optional
     */
    Optional<IngredientEntity> findByIngredientName(String ingredientName);
}
