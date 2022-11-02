package nl.abn.api.receipe.repository;

import nl.abn.api.receipe.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Recipe repository.
 */
@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long>, RecipeCustomRepository {

}

