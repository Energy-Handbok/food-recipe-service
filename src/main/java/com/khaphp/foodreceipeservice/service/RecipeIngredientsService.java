package com.khaphp.foodreceipeservice.service;

import com.khaphp.common.dto.ResponseObject;
import com.khaphp.foodreceipeservice.dto.RecipeIngredirents.RecipeIngredientsDTOcreate;
import com.khaphp.foodreceipeservice.dto.RecipeIngredirents.RecipeIngredientsDTOupdateItem;
import org.springframework.web.multipart.MultipartFile;

public interface RecipeIngredientsService {

    ResponseObject<Object> getAll(int pageSize, int pageIndex, String cookingRecipeId);
    ResponseObject<Object> getDetail(String id);
    ResponseObject<Object> create(RecipeIngredientsDTOcreate object);
    ResponseObject<Object> update(RecipeIngredientsDTOupdateItem object);
    ResponseObject<Object> updateImage(String id, MultipartFile file);
    ResponseObject<Object> delete(String id);
}
