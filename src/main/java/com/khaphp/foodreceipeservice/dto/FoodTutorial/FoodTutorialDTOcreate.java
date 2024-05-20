package com.khaphp.foodreceipeservice.dto.FoodTutorial;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FoodTutorialDTOcreate {
    private String cookingRecipeId;
    private List<FoodTutorialDTOcreateItem> items;
}
