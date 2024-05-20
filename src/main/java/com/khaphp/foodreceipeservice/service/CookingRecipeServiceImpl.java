package com.khaphp.foodreceipeservice.service;

import com.khaphp.common.constant.EmailDefault;
import com.khaphp.common.constant.StatusCookingRecipe;
import com.khaphp.common.dto.ResponseObject;
import com.khaphp.common.dto.usersystem.UserSystemDTOviewInOrtherEntity;
import com.khaphp.common.entity.UserSystem;
import com.khaphp.foodreceipeservice.call.UserServiceCall;
import com.khaphp.foodreceipeservice.dto.CookingRecipe.CookingRecipeDTOcreate;
import com.khaphp.foodreceipeservice.dto.CookingRecipe.CookingRecipeDTOdetail;
import com.khaphp.foodreceipeservice.dto.CookingRecipe.CookingRecipeDTOupdate;
import com.khaphp.foodreceipeservice.dto.CookingRecipe.CookingRecipeDTOview;
import com.khaphp.foodreceipeservice.entity.CookingRecipe;
import com.khaphp.foodreceipeservice.exception.ObjectNotFound;
import com.khaphp.foodreceipeservice.repo.CookingRecipeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CookingRecipeServiceImpl implements CookingRecipeService {
    public static final String OBJECT_NOT_FOUND_MSG = "object not found";
    public static final String EMPLOYEE_NOT_FOUND_MSG = "Employee not found";
    public static final String CUSTOMER_NOT_FOUND_MSG = "Customer not found";
    private final CookingRecipeRepository cookingRecipeRepository;
//    private final VotesRepository votesRepository;
    private final UserServiceCall userServiceCall;
    private final ModelMapper modelMapper;
    private final FileStore fileStore;

    @Value("${aws.s3.link_bucket}")
    private String linkBucket;

    @Value("${logo.energy_handbook.name}")
    private String logoName;
    @Override
    public ResponseObject<Object> getAll(int pageSize, int pageIndex, String customerId) {
        Page<CookingRecipe> objListPage = null;
        List<CookingRecipe> objList = null;
        List<CookingRecipeDTOview> objListV = null;
        int totalPage = 0;
        //paging
        if(pageSize > 0 && pageIndex > 0){
            if(customerId.equals("")){  //lấy hết
                objListPage = cookingRecipeRepository.findByStatus(StatusCookingRecipe.PUBLIC.toString(), PageRequest.of(pageIndex - 1, pageSize));  //vì current page ở code nó start = 0, hay bên ngoài la 2pga đầu tiên hay 1
            }else{ //có filter theo customer
                objListPage = cookingRecipeRepository.findByCustomerIdAndStatus(customerId, StatusCookingRecipe.PUBLIC.toString(), PageRequest.of(pageIndex - 1, pageSize));
            }
            if(objListPage != null){
                totalPage = objListPage.getTotalPages();
                objList = objListPage.getContent();
            }
        }else{ //get all
            objList = cookingRecipeRepository.findAll();
            pageIndex = 1;
        }
        objListV = new ArrayList<>();
        for(CookingRecipe x : objList){
            CookingRecipeDTOview dto = modelMapper.map(x, CookingRecipeDTOview.class);
//            dto.setCmtSize(x.getComments().size());
//            dto.setLike(x.getUserLikes().size());
//            dto.setStar(votesRepository.averageVoteStar(x.getId()));
//            dto.setVote(x.getVotes().size());
            dto.setProductImg(linkBucket + x.getProductImg());
            objListV.add(dto);
        }
        return ResponseObject.builder()
                .code(200).message("Success")
                .pageSize(objListV.size()).pageIndex(pageIndex).totalPage(totalPage)
                .data(objListV)
                .build();
    }

    @Override
    public ResponseObject<Object> getDetail(String id) {
        try{
            CookingRecipe object = cookingRecipeRepository.findById(id).orElse(null);
            if(object == null) {
                throw new ObjectNotFound(OBJECT_NOT_FOUND_MSG);
            }
            object.setProductImg(linkBucket + object.getProductImg());
            CookingRecipeDTOdetail dto = modelMapper.map(object, CookingRecipeDTOdetail.class);

            //call user service to get info of employee
            UserSystem employee = userServiceCall.getObject(object.getEmployeeId());
            if(employee == null) {
                throw new ObjectNotFound(EMPLOYEE_NOT_FOUND_MSG);
            }
            dto.setEmployeeV(UserSystemDTOviewInOrtherEntity.builder()
                    .id(employee.getId())
                    .name(employee.getName())
                    .imgUrl(linkBucket + employee.getImgUrl()).build());

            //call user service to get info of employee
            UserSystem customer = userServiceCall.getObject(object.getCustomerId());
            if(customer == null) {
                throw new ObjectNotFound(CUSTOMER_NOT_FOUND_MSG);
            }
            dto.setCustomerV(UserSystemDTOviewInOrtherEntity.builder()
                    .id(customer.getId())
                    .name(customer.getName())
                    .imgUrl(linkBucket + customer.getImgUrl()).build());

            //caculate comment, like, star, vote
//            dto.setCmtSize(object.getComments().size());
//            dto.setLike(object.getUserLikes().size());
//            dto.setStar(votesRepository.averageVoteStar(id));
//            dto.setVote(object.getVotes().size());
            return ResponseObject.builder()
                    .code(200)
                    .message("Found")
                    .data(dto)
                    .build();
        }catch (Exception e){
            return ResponseObject.builder()
                    .code(400)
                    .message("Exception: "+ e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponseObject<Object> create(CookingRecipeDTOcreate object) {
        try{
            UserSystem userSystem = userServiceCall.getObject(object.getCustomerId());
            if(userSystem == null){
                throw new ObjectNotFound(CUSTOMER_NOT_FOUND_MSG);
            }
            CookingRecipe cookingRecipe = modelMapper.map(object, CookingRecipe.class);
            cookingRecipe.setUpdateDate(new Date(System.currentTimeMillis()));
            UserSystem employ = userServiceCall.getDetailByEmail(EmailDefault.DEFAULT_EMPLOYEE_MAIL);
            if(employ == null){
                throw new ObjectNotFound(EMPLOYEE_NOT_FOUND_MSG);
            }
            cookingRecipe.setEmployeeId(employ.getId());
            cookingRecipe.setCustomerId(userSystem.getId());
            cookingRecipe.setProductImg(logoName);
            cookingRecipeRepository.save(cookingRecipe);
            return ResponseObject.builder()
                    .code(200)
                    .message("Success")
                    .data(cookingRecipe)
                    .build();
        }catch (Exception e){
            return ResponseObject.builder()
                    .code(400)
                    .message("Exception: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponseObject<Object> update(CookingRecipeDTOupdate object) {
        try{
            CookingRecipe object1 = cookingRecipeRepository.findById(object.getId()).orElse(null);
            if(object1 == null) {
                throw new Exception(OBJECT_NOT_FOUND_MSG);
            }
            object1.setName(object.getName());
            object1.setLevel(object.getLevel());
            object1.setTimeCook(object.getTimeCook());
            object1.setMealServing(object.getMealServing());
            object1.setDescription(object.getDescription());
            object1.setStatus(object.getStatus());
            object1.setUpdateDate(new Date(System.currentTimeMillis()));
            cookingRecipeRepository.save(object1);
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
            CookingRecipe object = cookingRecipeRepository.findById(id).orElse(null);
            if(object == null) {
                throw new Exception(OBJECT_NOT_FOUND_MSG);
            }
            try{
                //delete old img
                if(!object.getProductImg().equals("")){
                    fileStore.deleteImage(object.getProductImg());
                }
            }catch (Exception e){
                // img is null =>continue
            }
            //upload new img
            object.setProductImg(fileStore.uploadImg(file));
            cookingRecipeRepository.save(object);
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
            CookingRecipe object = cookingRecipeRepository.findById(id).orElse(null);
            if(object == null) {
                throw new Exception(OBJECT_NOT_FOUND_MSG);
            }
            if(!object.getProductImg().equals(logoName)){
                fileStore.deleteImage(object.getProductImg());
            }
            cookingRecipeRepository.delete(object);
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
