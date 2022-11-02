package nl.abn.api.receipe.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Recipe {
    private long id;
    private String recipeName;
    private String instructions;
    private Integer numberOfServings;
    private String category;
    private Set<Ingredient> ingredients;

}
