package nl.abn.api.receipe.domain.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class IngredientRequest {

    @NotNull(message = "IngredientName cannot be null")
    private String ingredientName;
}
