package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {

    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);
    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public InMemoryMealRepository() {
        MealsUtil.meals.forEach(n-> {n.setId(null);save(n, 1);});
    }


    @Override
    public Meal save(Meal meal, Integer userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }
        if (!valudeUser(meal, userId))
            return null;
        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, Integer userId) {
        if (!valudeUser(repository.get(id), userId))
            return false;
        return repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, Integer userId) {
        if (!valudeUser(repository.get(id), userId))
            return null;
        return repository.get(id);
    }

    @Override
    public Collection<Meal> getAll(Integer userId) {
        return repository.values()
                .stream()
                .sorted((a,b)->b.getDate().compareTo(a.getDate()))
                .collect(Collectors.toList());
    }



    //add
    public boolean valudeUser(Meal meal, Integer userId){
        return meal!=null &&
                meal.getUserId()!=null &&
                meal.getUserId().equals(userId);
    }


    public static List<MealTo> getTos(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filterUserId) {
        return filterByPredicate(meals, caloriesPerDay, meal -> true, meal -> true, filterUserId);
    }

    public static List<MealTo> getFilteredTos(Collection<Meal> meals, int caloriesPerDay, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, Predicate<Meal> filterUserId) {
        return filterByPredicate(meals,
                caloriesPerDay,
                meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), startDate, endDate),
                meal -> DateTimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime),
                filterUserId);
    }

    public static List<MealTo> filterByPredicate(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filterDate, Predicate<Meal> filterTime, Predicate<Meal> filterUserId) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(filterDate)
                .filter(filterTime)
                .filter(filterUserId)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static MealTo createTo(Meal meal, boolean excess) {
        return meal==null?null:new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    public static Meal createFromTo(MealTo mealTo, Integer userId) {
        return mealTo==null?null:new Meal(mealTo.getId(),userId, mealTo.getDateTime(), mealTo.getDescription(), mealTo.getCalories());
    }

}

