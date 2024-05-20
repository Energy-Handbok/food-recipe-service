package com.khaphp.foodreceipeservice.service;

import com.khaphp.common.dto.ResponseObject;
import com.khaphp.foodreceipeservice.dto.CookingRecipe.CookingRecipeDTOdetail;
import com.khaphp.foodreceipeservice.dto.RecipeIngredirents.RecipeIngredientsDTOcreate;
import com.khaphp.foodreceipeservice.dto.RecipeIngredirents.RecipeIngredientsDTOcreateItem;
import com.khaphp.foodreceipeservice.dto.RecipeIngredirents.RecipeIngredientsDTOupdateItem;
import com.khaphp.foodreceipeservice.entity.CookingRecipe;
import com.khaphp.foodreceipeservice.entity.RecipeIngredients;
import com.khaphp.foodreceipeservice.repo.CookingRecipeRepository;
import com.khaphp.foodreceipeservice.repo.RecipeIngredientsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeIngredientsServiceImpl implements RecipeIngredientsService {
    private final RecipeIngredientsRepository recipeIngredientsRepository;
    private final CookingRecipeRepository cookingRecipeRepository;
    private final ModelMapper modelMapper;
    private final FileStore fileStore;

    @Value("${aws.s3.link_bucket}")
    private String linkBucket;

    @Value("${logo.energy_handbook.name}")
    private String logoName;
    @Override
    public ResponseObject<Object> getAll(int pageSize, int pageIndex, String cookingRecipeId) {
        Page<RecipeIngredients> objListPage = null;
        List<RecipeIngredients> objList = null;
        int totalPage = 0;
        //paging
        if(pageSize > 0 && pageIndex > 0){
            if(cookingRecipeId.equals("")){  //lấy hết
                objListPage = recipeIngredientsRepository.findAll(PageRequest.of(pageIndex - 1, pageSize));  //vì current page ở code nó start = 0, hay bên ngoài la 2pga đầu tiên hay 1
            }else{ //có filter theo customer
                objListPage = recipeIngredientsRepository.findAllByCookingRecipeId(cookingRecipeId, PageRequest.of(pageIndex - 1, pageSize));
            }
            if(objListPage != null){
                totalPage = objListPage.getTotalPages();
                objList = objListPage.getContent();
            }
        }else{ //get all
            objList = recipeIngredientsRepository.findAll();
            pageIndex = 1;
        }
        if(objList != null){
            objList.forEach(object -> object.setImg(linkBucket + object.getImg()));
        }
        return ResponseObject.builder()
                .code(200).message("Success")
                .pageSize(objList.size()).pageIndex(pageIndex).totalPage(totalPage)
                .data(objList)
                .build();
    }

    @Override
    public ResponseObject<Object> getDetail(String id) {
        try{
            RecipeIngredients object = recipeIngredientsRepository.findById(id).orElse(null);
            if(object == null){
                throw new Exception("object not found");
            }
            object.setImg(linkBucket + object.getImg());
            return ResponseObject.builder()
                    .code(200)
                    .message("Found")
                    .data(object)
                    .build();
        }catch (Exception e){
            return ResponseObject.builder()
                    .code(400)
                    .message("Exception: "+ e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponseObject<Object> create(RecipeIngredientsDTOcreate object) {
        try{
            CookingRecipe cookingRecipe = cookingRecipeRepository.findById(object.getCookingRecipeId()).orElse(null);
            if(cookingRecipe == null){
                throw new Exception("CookingRecipe not found");
            }

            for (RecipeIngredientsDTOcreateItem item: object.getItems()) {
                RecipeIngredients recipeIngredients = modelMapper.map(item, RecipeIngredients.class);
                recipeIngredients.setCookingRecipe(cookingRecipe);
                recipeIngredients.setImg(logoName);
                recipeIngredientsRepository.save(recipeIngredients);
            }
            return ResponseObject.builder()
                    .code(200)
                    .message("Success")
                    .data(recipeIngredientsRepository.findAllByCookingRecipeId(object.getCookingRecipeId(), null).getContent())
                    .build();
        }catch (Exception e){
            return ResponseObject.builder()
                    .code(400)
                    .message("Exception: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponseObject<Object> update(RecipeIngredientsDTOupdateItem object) {
        try{
            RecipeIngredients object1 = recipeIngredientsRepository.findById(object.getId()).orElse(null);
            if(object1 == null) {
                throw new Exception("object not found");
            }
            object1.setName(object.getName());
            object1.setAmount(object.getAmount());
            object1.setNote(object.getNote());
            //update date lại cho cooking recipe
            object1.getCookingRecipe().setUpdateDate(new Date(System.currentTimeMillis()));
            recipeIngredientsRepository.save(object1);
            return ResponseObject.builder()
                    .code(200)
                    .message("Success")
                    .build();
        }catch (Exception e){
            return ResponseObject.builder()
                    .code(400)
                    .message("Exception: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponseObject<Object> updateImage(String id, MultipartFile file) {
        try{
            RecipeIngredients object = recipeIngredientsRepository.findById(id).orElse(null);
            if(object == null) {
                throw new Exception("object not found");
            }
            if(!object.getImg().equals(logoName)){
                fileStore.deleteImage(object.getImg());
            }
            //upload new img
            object.setImg(fileStore.uploadImg(file));
            //update lại time cho cooking recipe của nó
            object.getCookingRecipe().setUpdateDate(new Date(System.currentTimeMillis()));
            recipeIngredientsRepository.save(object);
            return ResponseObject.builder()
                    .code(200)
                    .message("Success")
                    .build();
        }catch (Exception e){
            return ResponseObject.builder()
                    .code(400)
                    .message("Exception: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponseObject<Object> delete(String id) {
        try{
            RecipeIngredients object = recipeIngredientsRepository.findById(id).orElse(null);
            if(object == null) {
                throw new Exception("object not found");
            }
            if(!object.getImg().equals(logoName)){
                fileStore.deleteImage(object.getImg());
            }

            recipeIngredientsRepository.delete(object);
            return ResponseObject.builder()
                    .code(200)
                    .message("Success")
                    .build();
        }catch (Exception e){
            return ResponseObject.builder()
                    .code(400)
                    .message("Exception: " + e.getMessage())
                    .build();
        }
    }
}
