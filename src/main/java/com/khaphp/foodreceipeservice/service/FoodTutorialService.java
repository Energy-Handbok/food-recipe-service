package com.khaphp.foodreceipeservice.service;


import com.khaphp.common.dto.ResponseObject;
import com.khaphp.foodreceipeservice.dto.FoodTutorial.FoodTutorialDTOcreate;
import com.khaphp.foodreceipeservice.dto.FoodTutorial.FoodTutorialDTOupdate;
import org.springframework.web.multipart.MultipartFile;

public interface FoodTutorialService {

    ResponseObject<Object> getAll(int pageSize, int pageIndex, String cookingRecipeId);
    ResponseObject<Object> getDetail(String id);
    ResponseObject<Object> create(FoodTutorialDTOcreate object);
    ResponseObject<Object> update(FoodTutorialDTOupdate object);
    ResponseObject<Object> updateImage(String id, MultipartFile file);
    ResponseObject<Object> delete(String id);
}
