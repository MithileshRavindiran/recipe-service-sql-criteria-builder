package nl.abn.api.receipe.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeUpdateRequest {

    private String category;
    private Integer numberOfServings;
    private String recipeName;
    private String instructions;
    private List<IngredientUpdateRequest> ingredientList;
}
