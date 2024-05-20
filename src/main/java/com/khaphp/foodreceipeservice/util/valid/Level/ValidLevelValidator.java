package com.khaphp.foodreceipeservice.util.valid.Level;

import com.khaphp.common.constant.Level;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class ValidLevelValidator implements ConstraintValidator<ValidLevel, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        List<String> roles = List.of(Level.EASY.toString(), Level.MEDIUM.toString(), Level.DIFFICULT.toString());
        if(roles.contains(value)){
            return true;
        }else{
            return false;
        }
    }
}
