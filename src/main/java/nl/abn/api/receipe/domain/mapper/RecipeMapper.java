package nl.abn.api.receipe.domain.mapper;

import nl.abn.api.receipe.domain.model.Recipe;
import nl.abn.api.receipe.entity.RecipeEntity;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * The interface Recipe mapper.
 */
@Mapper(componentModel = "spring")
public interface RecipeMapper {

    /**
     * Map recipe.
     *
     * @param recipeEntity the recipe entity
     * @return the recipe
     */
    Recipe map(RecipeEntity recipeEntity);

    /**
     * Map list.
     *
     * @param recipeList the recipe list
     * @return the list
     */
    List<Recipe> map(List<RecipeEntity> recipeList);

}
