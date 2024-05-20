package com.khaphp.foodreceipeservice.dto.CookingRecipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CookingRecipeDTOview {
    private String id;
    private String name;
    private String productImg;
    private String level;
    private float timeCook;
    private Date updateDate;
    private short mealServing;
    private String description;
    private String status;
    private int cmtSize;
    private int like;
    private float star;
    private int vote;
}
