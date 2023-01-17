package com.example.raj_liangjian.dto;


import com.example.raj_liangjian.entity.Setmeal;
import com.example.raj_liangjian.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
