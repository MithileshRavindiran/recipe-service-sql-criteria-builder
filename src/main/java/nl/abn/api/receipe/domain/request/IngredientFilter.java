package nl.abn.api.receipe.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IngredientFilter {

    private String ingredientName;
    private boolean includeIngredient;
}
