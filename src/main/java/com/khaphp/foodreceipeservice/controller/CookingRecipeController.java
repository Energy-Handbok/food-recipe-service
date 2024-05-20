package com.khaphp.foodreceipeservice.controller;

import com.khaphp.common.dto.ResponseObject;
import com.khaphp.foodreceipeservice.dto.CookingRecipe.CookingRecipeDTOcreate;
import com.khaphp.foodreceipeservice.dto.CookingRecipe.CookingRecipeDTOupdate;
import com.khaphp.foodreceipeservice.service.CookingRecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/cooking-recipe")
//@SecurityRequirement(name = "EnergyHandbook")
@RequiredArgsConstructor
public class CookingRecipeController {
    private final CookingRecipeService cookingRecipeService;

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestParam(defaultValue = "10") int pageSize,
                                    @RequestParam(defaultValue = "1") int pageIndex,
                                    @RequestParam(defaultValue = "") String customerId) {
        ResponseObject<Object> responseObject = cookingRecipeService.getAll(pageSize, pageIndex, customerId);
        if(responseObject.getCode() == 200){
            return ResponseEntity.ok(responseObject);
        }
        return ResponseEntity.badRequest().body(responseObject);
    }
    @GetMapping("/detail")
    public ResponseEntity<Object> getObject(String id){
        ResponseObject<Object> responseObject = cookingRecipeService.getDetail(id);
        if(responseObject.getCode() == 200){
            return ResponseEntity.ok(responseObject);
        }
        return ResponseEntity.badRequest().body(responseObject);
    }

    @PostMapping
    public ResponseEntity<Object> createObject(@RequestBody @Valid CookingRecipeDTOcreate object){
        ResponseObject<Object> responseObject = cookingRecipeService.create(object);
        if(responseObject.getCode() == 200){
            return ResponseEntity.ok(responseObject);
        }
        return ResponseEntity.badRequest().body(responseObject);
    }

    @PutMapping(
            path = "/img",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> updateImg(@RequestParam("id") String id,
                                       @RequestParam("file") MultipartFile file){
        ResponseObject<Object> responseObject = cookingRecipeService.updateImage(id, file);
        if(responseObject.getCode() == 200){
            return ResponseEntity.ok(responseObject);
        }
        return ResponseEntity.badRequest().body(responseObject);
    }

    @PutMapping
    public ResponseEntity<Object> updateObject(@RequestBody @Valid CookingRecipeDTOupdate object){
        ResponseObject<Object> responseObject = cookingRecipeService.update(object);
        if(responseObject.getCode() == 200){
            return ResponseEntity.ok(responseObject);
        }
        return ResponseEntity.badRequest().body(responseObject);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteObject(String id){
        ResponseObject<Object> responseObject = cookingRecipeService.delete(id);
        if(responseObject.getCode() == 200){
            return ResponseEntity.ok(responseObject);
        }
        return ResponseEntity.badRequest().body(responseObject);
    }
}
