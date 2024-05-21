package com.khaphp.foodreceipeservice.service;

import com.khaphp.common.dto.ResponseObject;
import com.khaphp.foodreceipeservice.dto.CookingRecipe.CookingRecipeDTOdetail;
import com.khaphp.foodreceipeservice.dto.FoodTutorial.FoodTutorialDTOcreate;
import com.khaphp.foodreceipeservice.dto.FoodTutorial.FoodTutorialDTOcreateItem;
import com.khaphp.foodreceipeservice.dto.FoodTutorial.FoodTutorialDTOupdate;
import com.khaphp.foodreceipeservice.entity.CookingRecipe;
import com.khaphp.foodreceipeservice.entity.FoodTutorial;
import com.khaphp.foodreceipeservice.exception.ObjectNotFound;
import com.khaphp.foodreceipeservice.repo.CookingRecipeRepository;
import com.khaphp.foodreceipeservice.repo.FoodTutorialRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodTutorialServiceImpl implements FoodTutorialService {
    public static final String NUMBER_ORDER_IN_FOOTTUTORIAL = "numberOrder";
    public static final String OBJECT_NOT_FOUND_MSG = "object not found";
    public static final String EXCEPTION_MSG = "Exception: ";
    public static final String SUCCESS_MSG = "Success";
    private final FoodTutorialRepository foodTutorialRepository;
    private final CookingRecipeRepository cookingRecipeRepository;
    private final ModelMapper modelMapper;
    private final FileStore fileStore;

    @Value("${aws.s3.link_bucket}")
    private String linkBucket;

    @Value("${logo.energy_handbook.name}")
    private String logoName;
    @Override
    public ResponseObject<Object> getAll(int pageSize, int pageIndex, String cookingRecipeId) {
        Page<FoodTutorial> objListPage = null;
        List<FoodTutorial> objList = null;
        int totalPage = 0;
        //paging
        if(pageSize > 0 && pageIndex > 0){
            if(cookingRecipeId.equals("")){  //lấy hết
                objListPage = foodTutorialRepository.findAll(PageRequest.of(pageIndex - 1, pageSize));  //vì current page ở code nó start = 0, hay bên ngoài la 2pga đầu tiên hay 1
            }else{ //có filter theo customer
                objListPage = foodTutorialRepository.findAllByCookingRecipeId(cookingRecipeId, PageRequest.of(pageIndex - 1, pageSize).withSort(Sort.by(NUMBER_ORDER_IN_FOOTTUTORIAL)));
            }
            if(objListPage != null){
                totalPage = objListPage.getTotalPages();
                objList = objListPage.getContent();
            }
        }else{ //get all
            objList = foodTutorialRepository.findAll();
            pageIndex = 1;
        }
        objList.forEach(object -> object.setImg(linkBucket + object.getImg()));
        return ResponseObject.builder()
                .code(200).message(SUCCESS_MSG)
                .pageSize(objList.size()).pageIndex(pageIndex).totalPage(totalPage)
                .data(objList)
                .build();
    }

    @Override
    public ResponseObject<Object> getDetail(String id) {
        try{
            FoodTutorial object = foodTutorialRepository.findById(id).orElse(null);
            if(object == null){
                throw new ObjectNotFound(OBJECT_NOT_FOUND_MSG);
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
                    .message(EXCEPTION_MSG + e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponseObject<Object> create(FoodTutorialDTOcreate object) {
        try{
            CookingRecipe cookingRecipe = cookingRecipeRepository.findById(object.getCookingRecipeId()).orElse(null);
            if(cookingRecipe == null){
                throw new ObjectNotFound("CookingRecipe not found");
            }

            for (FoodTutorialDTOcreateItem item: object.getItems()) {
                FoodTutorial recipeIngredients = modelMapper.map(item, FoodTutorial.class);
                recipeIngredients.setCookingRecipe(cookingRecipe);
                recipeIngredients.setImg(logoName);
                foodTutorialRepository.save(recipeIngredients);
            }
            return ResponseObject.builder()
                    .code(200)
                    .message(SUCCESS_MSG)
                    .data(foodTutorialRepository.findAllByCookingRecipeId(object.getCookingRecipeId(), null).getContent())
                    .build();
        }catch (Exception e){
            return ResponseObject.builder()
                    .code(400)
                    .message(EXCEPTION_MSG + e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponseObject<Object> update(FoodTutorialDTOupdate object) {
        try{
            FoodTutorial object1 = foodTutorialRepository.findById(object.getId()).orElse(null);
            if(object1 == null) {
                throw new ObjectNotFound(OBJECT_NOT_FOUND_MSG);
            }
            object1.setNumberOrder(object.getNumberOrder());
            object1.setDescription(object.getDescription());
            //update date lại cho cooking recipe
            object1.getCookingRecipe().setUpdateDate(new Date(System.currentTimeMillis()));
            foodTutorialRepository.save(object1);
            return ResponseObject.builder()
                    .code(200)
                    .message(SUCCESS_MSG)
                    .build();
        }catch (Exception e){
            return ResponseObject.builder()
                    .code(400)
                    .message(EXCEPTION_MSG + e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponseObject<Object> updateImage(String id, MultipartFile file) {
        try{
            FoodTutorial object = foodTutorialRepository.findById(id).orElse(null);
            if(object == null) {
                throw new ObjectNotFound(OBJECT_NOT_FOUND_MSG);
            }
            if(!object.getImg().equals(logoName)){
                fileStore.deleteImage(object.getImg());
            }
            //upload new img
            object.setImg(fileStore.uploadImg(file));
            //update lại time cho cooking recipe của nó
            object.getCookingRecipe().setUpdateDate(new Date(System.currentTimeMillis()));
            foodTutorialRepository.save(object);
            return ResponseObject.builder()
                    .code(200)
                    .message(SUCCESS_MSG)
                    .build();
        }catch (Exception e){
            return ResponseObject.builder()
                    .code(400)
                    .message(EXCEPTION_MSG + e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponseObject<Object> delete(String id) {
        try{
            FoodTutorial object = foodTutorialRepository.findById(id).orElse(null);
            if(object == null) {
                throw new ObjectNotFound(OBJECT_NOT_FOUND_MSG);
            }
            if(!object.getImg().equals(logoName)){
                fileStore.deleteImage(object.getImg());
            }

            foodTutorialRepository.delete(object);
            return ResponseObject.builder()
                    .code(200)
                    .message(SUCCESS_MSG)
                    .build();
        }catch (Exception e){
            return ResponseObject.builder()
                    .code(400)
                    .message(EXCEPTION_MSG + e.getMessage())
                    .build();
        }
    }
}
