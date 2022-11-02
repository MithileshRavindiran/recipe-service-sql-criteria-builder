package nl.abn.api.receipe.repository.impl;

import nl.abn.api.receipe.domain.request.IngredientFilter;
import nl.abn.api.receipe.domain.request.RecipeFilterRequest;
import nl.abn.api.receipe.entity.Category;
import nl.abn.api.receipe.entity.IngredientEntity;
import nl.abn.api.receipe.entity.IngredientEntity_;
import nl.abn.api.receipe.entity.RecipeEntity;
import nl.abn.api.receipe.entity.RecipeEntity_;
import nl.abn.api.receipe.repository.RecipeCustomRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Recipe repository.
 */
@Repository
public class RecipeRepositoryImpl implements RecipeCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<RecipeEntity> filterRecipeByCriteria(final RecipeFilterRequest recipeFilterRequest) {
        List<Predicate> andPredicates = new ArrayList<>();
        List<Predicate> orPredicate = new ArrayList<>();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RecipeEntity> cq = cb.createQuery(RecipeEntity.class);
        Root<RecipeEntity> recipe = cq.from(RecipeEntity.class);

        if (recipeFilterRequest.getNumberOfServings() != null) {
            andPredicates.add(cb.equal(recipe.get(RecipeEntity_.NUMBER_OF_SERVINGS), recipeFilterRequest.getNumberOfServings()));
        }

        if (recipeFilterRequest.getCategory() != null) {
            Category category = getCategory(recipeFilterRequest);
            andPredicates.add(cb.equal(recipe.get(RecipeEntity_.CATEGORY), category));
        }

        if (StringUtils.isNotEmpty(recipeFilterRequest.getTextSearch())) {
            andPredicates.add(cb.like(recipe.get(RecipeEntity_.INSTRUCTIONS), "%" + recipeFilterRequest.getTextSearch() + "%"));
        }

        if(ObjectUtils.isNotEmpty(recipeFilterRequest.getIngredientFilter())) {
            for (IngredientFilter filterValue : recipeFilterRequest.getIngredientFilter()) {
                Subquery<Long> subQuery = cq.subquery(Long.class);
                Root<RecipeEntity> subQueryRecipe = subQuery.from(RecipeEntity.class);
                Join<IngredientEntity, RecipeEntity> subQueryIngredient = subQueryRecipe.join(RecipeEntity_.INGREDIENTS);

                subQuery.select(subQueryRecipe.get(RecipeEntity_.ID)).where(
                        cb.equal(subQueryIngredient.get(IngredientEntity_.INGREDIENT_NAME), filterValue.getIngredientName()));

                if (filterValue.isIncludeIngredient()) {
                    orPredicate.add(cb.in(recipe.get(RecipeEntity_.ID)).value(subQuery));
                } else {
                    andPredicates.add(cb.not(cb.in(recipe.get(RecipeEntity_.ID)).value(subQuery)));
                }
            }
        }

        Predicate andOperation = cb.and(andPredicates.toArray(new Predicate[andPredicates.size()]));
        Predicate orOperation = cb.or(orPredicate.toArray(new Predicate[orPredicate.size()]));

        if (ObjectUtils.isNotEmpty(orPredicate)) {
            cq.where(cb.and(andOperation, orOperation));
        } else {
            cq.where(cb.and(andPredicates.toArray(new Predicate[andPredicates.size()])));
        }
        TypedQuery<RecipeEntity> query = entityManager.createQuery(cq);

        return query.getResultList();
    }

    private Category getCategory(final RecipeFilterRequest recipeFilterRequest) {
        Category category;
        if (recipeFilterRequest.getCategory().equals(Category.VEGETARIAN.getCode())) {
            category = Category.VEGETARIAN;
        } else if (recipeFilterRequest.getCategory().equals(Category.NON_VEGETARIAN.getCode())) {
            category = Category.NON_VEGETARIAN;
        } else {
            throw new IllegalArgumentException("Category [" + recipeFilterRequest.getCategory()
                    + "] not supported.");
        }
        return category;
    }
}
