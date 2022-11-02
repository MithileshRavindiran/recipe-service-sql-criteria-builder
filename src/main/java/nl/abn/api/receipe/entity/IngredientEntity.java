package nl.abn.api.receipe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "INGREDIENT")
@Getter
@Setter
public class IngredientEntity implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name="ingredient_name")
    private String ingredientName;

    @JsonIgnore
    @ManyToMany(cascade=CascadeType.ALL, mappedBy = "ingredients")
    private Set<RecipeEntity> recipes = new HashSet<>();
}
