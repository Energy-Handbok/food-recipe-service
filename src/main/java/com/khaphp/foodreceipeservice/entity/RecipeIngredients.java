package com.khaphp.foodreceipeservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RecipeIngredients {
    @Id
    @UuidGenerator
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;
    private String name;
    private String amount;
    private String img;
    private String note;

    @ManyToOne
    @JsonIgnore
    private CookingRecipe cookingRecipe;
}
