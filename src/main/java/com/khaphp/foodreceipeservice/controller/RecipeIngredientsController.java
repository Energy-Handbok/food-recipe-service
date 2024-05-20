package com.khaphp.foodreceipeservice.controller;

import com.khaphp.common.dto.ResponseObject;
import com.khaphp.foodreceipeservice.dto.RecipeIngredirents.RecipeIngredientsDTOcreate;
import com.khaphp.foodreceipeservice.dto.RecipeIngredirents.RecipeIngredientsDTOupdateItem;
import com.khaphp.foodreceipeservice.service.RecipeIngredientsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/recipe-ingredients")
//@SecurityRequirement(name = "EnergyHandbook")
@RequiredArgsConstructor
public class RecipeIngredientsController {
    private final RecipeIngredientsService recipeIngredientsService;

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestParam(defaultValue = "10") int pageSize,
                                    @RequestParam(defaultValue = "1") int pageIndex,
                                    @RequestParam(defaultValue = "") String cookingRecipeId){
        ResponseObject<Object> responseObject = recipeIngredientsService.getAll(pageSize, pageIndex, cookingRecipeId);
        if(responseObject.getCode() == 200){
            return ResponseEntity.ok(responseObject);
        }
        return ResponseEntity.badRequest().body(responseObject);
    }
    @GetMapping("/detail")
    public ResponseEntity<Object> getObject(String id){
        ResponseObject<Object> responseObject = recipeIngredientsService.getDetail(id);
        if(responseObject.getCode() == 200){
            return ResponseEntity.ok(responseObject);
        }
        return ResponseEntity.badRequest().body(responseObject);
    }

    @PostMapping
    public ResponseEntity<Object> createObject(@RequestBody @Valid RecipeIngredientsDTOcreate object){
        ResponseObject<Object> responseObject = recipeIngredientsService.create(object);
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
        ResponseObject<Object> responseObject = recipeIngredientsService.updateImage(id, file);
        if(responseObject.getCode() == 200){
            return ResponseEntity.ok(responseObject);
        }
        return ResponseEntity.badRequest().body(responseObject);
    }

    @PutMapping
    public ResponseEntity<Object> updateObject(@RequestBody @Valid RecipeIngredientsDTOupdateItem object){
        ResponseObject<Object> responseObject = recipeIngredientsService.update(object);
        if(responseObject.getCode() == 200){
            return ResponseEntity.ok(responseObject);
        }
        return ResponseEntity.badRequest().body(responseObject);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteObject(String id){
        ResponseObject<Object> responseObject = recipeIngredientsService.delete(id);
        if(responseObject.getCode() == 200){
            return ResponseEntity.ok(responseObject);
        }
        return ResponseEntity.badRequest().body(responseObject);
    }
}
