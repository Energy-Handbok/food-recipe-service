package com.khaphp.foodreceipeservice.repo;

import com.khaphp.foodreceipeservice.entity.CookingRecipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CookingRecipeRepository extends JpaRepository<CookingRecipe, String> {
    Page<CookingRecipe> findByCustomerIdAndStatus(String customerId, String status, PageRequest pageRequest);

    Page<CookingRecipe> findByStatus(String status, PageRequest pageRequest);
}
