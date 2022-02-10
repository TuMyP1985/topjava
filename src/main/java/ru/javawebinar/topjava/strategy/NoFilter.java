package ru.javawebinar.topjava.strategy;

import ru.javawebinar.topjava.model.MealTo;

import java.util.List;

public class NoFilter implements FilterStrategy{
    @Override
    public List<MealTo> getMeals() {
        return null;
    }
}
