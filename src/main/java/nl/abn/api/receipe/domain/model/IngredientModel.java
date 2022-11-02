package nl.abn.api.receipe.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.abn.api.receipe.entity.RecipeEntity;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientModel {

    private long id;
    private String ingredientName;
    private Set<RecipeEntity> recipes;
}
