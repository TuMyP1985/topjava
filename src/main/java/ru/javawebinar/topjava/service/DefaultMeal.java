package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealDao;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultMeal implements MealService{
    private final MealRepository mealRepository;
    private MealsUtil mealsUtil;

    public DefaultMeal() {
        mealRepository = new MealDao();
    }

    @Override
    public void addMeal(MealTo mealTo) {
        Meal meal = MealsUtil.ConvertToMeal(mealTo);
        mealRepository.addMeal(meal);
    }

    @Override
    public void deleteMeal(int mealId) {
        mealRepository.deleteMeal(mealId);
    }

    @Override
    public void updateMeal(MealTo mealTo) {
        Meal meal = MealsUtil.ConvertToMeal(mealTo);
        mealRepository.updateMeal(meal);
    }

    @Override
    public MealTo getMealById(int mealId) {
        synchronized (this){
        Meal meal = mealRepository.getMealById(mealId);
        return MealsUtil.ConvertToMealTo(meal);}
    }

    @Override
    public List<MealTo> getAllMeals() {
        List<Meal> meals =  mealRepository.getAllMeals();
        return MealsUtil.filteredByStreams(mealRepository.getAllMeals(), LocalTime.of(0, 0), LocalTime.of(23, 0), MealsUtil.caloriesPerDay);
    }
}
