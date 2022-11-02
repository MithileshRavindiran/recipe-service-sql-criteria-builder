package nl.abn.api.receipe.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeRequest {

    @NotNull(message="Category cannot be empty")
    private String category;
    @NotNull(message="Number Of Servings cannot be empty")
    private Integer numberOfServings;
    @NotNull(message="Recipe Name cannot be empty")
    private String recipeName;
    @NotNull(message="Category cannot be empty")
    private String instructions;
    private List<IngredientRequest> ingredientList;
}
