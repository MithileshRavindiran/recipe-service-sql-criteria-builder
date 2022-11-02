package nl.abn.api.receipe.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "RECIPE")
@Getter
@Setter
public class RecipeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String recipeName;

    @Column(nullable = false)
    private Integer numberOfServings;

    @Column(nullable = false)
    private String instructions;

    private Category category;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name = "recipe_ingredient", joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private Set<IngredientEntity> ingredients = new HashSet<>();


    public void addIngredient(IngredientEntity ingredientEntity) {
        this.ingredients.add(ingredientEntity);
        ingredientEntity.getRecipes().add(this);
    }

    public void removeIngredient(long ingredientId) {
        IngredientEntity ingredientEntity = this.ingredients.stream().filter(t -> t.getId() == ingredientId).findFirst().orElse(null);
        if (ingredientEntity != null) {
            this.ingredients.remove(ingredientEntity);
            ingredientEntity.getRecipes().remove(this);
        }
    }

    @PreRemove
    public void removeIngredientsFromRecipe() {
        this.ingredients.clear();
    }
}
