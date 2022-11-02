package nl.abn.api.receipe.domain.mapper;

import nl.abn.api.receipe.domain.model.IngredientModel;
import nl.abn.api.receipe.entity.IngredientEntity;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * The interface Ingredient mapper.
 */
@Mapper(componentModel = "spring")
public interface IngredientMapper {

    /**
     * Map ingredient model.
     *
     * @param ingredientEntity the ingredient entity
     * @return the ingredient model
     */
    IngredientModel map(IngredientEntity ingredientEntity);

    /**
     * Map list.
     *
     * @param ingredientEntity the ingredient entity
     * @return the list
     */
    List<IngredientModel> map(List<IngredientEntity> ingredientEntity);
}
