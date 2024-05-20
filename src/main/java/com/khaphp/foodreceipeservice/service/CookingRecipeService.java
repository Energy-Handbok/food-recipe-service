package com.khaphp.foodreceipeservice.service;

import com.khaphp.common.dto.ResponseObject;
import com.khaphp.foodreceipeservice.dto.CookingRecipe.CookingRecipeDTOcreate;
import com.khaphp.foodreceipeservice.dto.CookingRecipe.CookingRecipeDTOupdate;
import org.springframework.web.multipart.MultipartFile;

public interface CookingRecipeService {

    ResponseObject<Object> getAll(int pageSize, int pageIndex, String customerId);
    ResponseObject<Object> getDetail(String id);
    ResponseObject<Object> create(CookingRecipeDTOcreate object);
    ResponseObject<Object> update(CookingRecipeDTOupdate object);
    ResponseObject<Object> updateImage(String id, MultipartFile file);
    ResponseObject<Object> delete(String id);
}
