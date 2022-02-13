package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository.*;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    @Autowired
    private MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }


    public MealTo create(MealTo mealTo, Integer userId) {
        return createTo(repository.save(createFromTo(mealTo, userId), userId)
                ,mealTo.isExcess());
    }

    public void delete(int id, Integer userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public MealTo get(int id, Integer userId) {
        return checkNotFoundWithId(createTo(repository.get(id, userId),false),id);
    }

    public List<MealTo> getAll(Integer userId) {
        Predicate<Meal> filterUserId = m->m.getUserId().equals(userId);
        return getTos(repository.getAll(userId),DEFAULT_CALORIES_PER_DAY, filterUserId);
    }

    public List<MealTo> getAllFilter(Integer userId, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        Predicate<Meal> filterUserId = m->m.getUserId().equals(userId);
        return getFilteredTos(repository.getAll(userId), DEFAULT_CALORIES_PER_DAY, startDate, startTime, endDate, endTime, filterUserId);
    }

    public void update(MealTo mealTo, Integer userId) {
        checkNotFoundWithId(repository.save(createFromTo(mealTo,userId), userId), mealTo.getId());
    }







}