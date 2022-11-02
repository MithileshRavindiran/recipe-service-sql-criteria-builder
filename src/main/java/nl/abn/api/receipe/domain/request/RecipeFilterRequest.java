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
public class RecipeFilterRequest {

    private Integer numberOfServings;
    private List<IngredientFilter> ingredientFilter;
    private String textSearch;
    private String category;
}
