package ru.javawebinar.topjava.strategy;

import ru.javawebinar.topjava.model.MealTo;

import java.util.List;

public interface FilterStrategy {
    List<MealTo> getMeals();
}
