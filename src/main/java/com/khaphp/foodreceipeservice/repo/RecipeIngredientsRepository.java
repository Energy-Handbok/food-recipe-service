package com.khaphp.foodreceipeservice.repo;

import com.khaphp.foodreceipeservice.entity.RecipeIngredients;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeIngredientsRepository extends JpaRepository<RecipeIngredients, String> {
    Page<RecipeIngredients> findAllByCookingRecipeId(String id, PageRequest of);
}
