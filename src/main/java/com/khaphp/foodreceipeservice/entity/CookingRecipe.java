package com.khaphp.foodreceipeservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CookingRecipe {
    @Id
    @UuidGenerator
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;
    private String name;
    private String productImg;
    private String level;
    private float timeCook;
    private Date updateDate;
    private short mealServing;
    private String description;
    private String status;

    @Column(columnDefinition = "VARCHAR(36)")
    private String employeeId;

    @Column(columnDefinition = "VARCHAR(36)")
    private String customerId;

    @OneToMany(mappedBy = "cookingRecipe")
    @JsonIgnore
    private List<RecipeIngredients> recipeIngredients;

    @OneToMany(mappedBy = "cookingRecipe")
    @JsonIgnore
    private List<FoodTutorial> foodTutorials;
}
