package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.MealTo;

import java.util.List;

public interface MealService {
    void addMeal(MealTo mealTo);
    void deleteMeal(int mealId);
    void updateMeal(MealTo mealTo);
    MealTo getMealById(int mealId);
    List<MealTo> getAllMeals();
}
