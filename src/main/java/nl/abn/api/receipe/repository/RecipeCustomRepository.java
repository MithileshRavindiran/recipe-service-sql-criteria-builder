package nl.abn.api.receipe.repository;

import nl.abn.api.receipe.domain.request.RecipeFilterRequest;
import nl.abn.api.receipe.entity.RecipeEntity;

import java.util.List;

/**
 * The interface Recipe custom repository.
 */
public interface RecipeCustomRepository {

    /**
     * Filter recipe by criteria list.
     *
     * @param recipeFilterRequest the recipe filter request
     * @return the list
     */
    List<RecipeEntity> filterRecipeByCriteria(RecipeFilterRequest recipeFilterRequest);
}
