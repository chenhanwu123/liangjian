package com.example.raj_liangjian.dto;


import com.example.raj_liangjian.entity.Dish;
import com.example.raj_liangjian.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
