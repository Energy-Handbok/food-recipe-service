package com.khaphp.foodreceipeservice.controller;

import com.khaphp.common.dto.ResponseObject;
import com.khaphp.foodreceipeservice.dto.FoodTutorial.FoodTutorialDTOcreate;
import com.khaphp.foodreceipeservice.dto.FoodTutorial.FoodTutorialDTOupdate;
import com.khaphp.foodreceipeservice.service.FoodTutorialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/food-tutorials")
//@SecurityRequirement(name = "EnergyHandbook")
@RequiredArgsConstructor
public class FoodTutorialController {
    private final FoodTutorialService foodTutorialService;

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestParam(defaultValue = "10") int pageSize,
                                    @RequestParam(defaultValue = "1") int pageIndex,
                                    @RequestParam(defaultValue = "") String cookingRecipeId){
        ResponseObject<Object> responseObject = foodTutorialService.getAll(pageSize, pageIndex, cookingRecipeId);
        if(responseObject.getCode() == 200){
            return ResponseEntity.ok(responseObject);
        }
        return ResponseEntity.badRequest().body(responseObject);
    }
    @GetMapping("/detail")
    public ResponseEntity<Object> getObject(String id){
        ResponseObject<Object> responseObject = foodTutorialService.getDetail(id);
        if(responseObject.getCode() == 200){
            return ResponseEntity.ok(responseObject);
        }
        return ResponseEntity.badRequest().body(responseObject);
    }

    @PostMapping
    public ResponseEntity<Object> createObject(@RequestBody @Valid FoodTutorialDTOcreate object){
        ResponseObject<Object> responseObject = foodTutorialService.create(object);
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
        ResponseObject<Object> responseObject = foodTutorialService.updateImage(id, file);
        if(responseObject.getCode() == 200){
            return ResponseEntity.ok(responseObject);
        }
        return ResponseEntity.badRequest().body(responseObject);
    }

    @PutMapping
    public ResponseEntity<Object> updateObject(@RequestBody @Valid FoodTutorialDTOupdate object){
        ResponseObject<Object> responseObject = foodTutorialService.update(object);
        if(responseObject.getCode() == 200){
            return ResponseEntity.ok(responseObject);
        }
        return ResponseEntity.badRequest().body(responseObject);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteObject(String id){
        ResponseObject<Object> responseObject = foodTutorialService.delete(id);
        if(responseObject.getCode() == 200){
            return ResponseEntity.ok(responseObject);
        }
        return ResponseEntity.badRequest().body(responseObject);
    }
}
