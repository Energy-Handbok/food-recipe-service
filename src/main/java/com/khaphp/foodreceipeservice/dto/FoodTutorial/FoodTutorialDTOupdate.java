package com.khaphp.foodreceipeservice.dto.FoodTutorial;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FoodTutorialDTOupdate {
    private String id;
    @Min(value = 1, message = "numberOrder must be greater than 0")
    private short numberOrder;
    private String description;
}
