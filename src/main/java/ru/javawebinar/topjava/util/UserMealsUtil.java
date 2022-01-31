package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
        //1.должны возвращаться только записи между startTime и endTime
        //2.поле UserMealWithExcess.excess должно показывать, превышает ли сумма калорий за весь день значение caloriesPerDay
        //Т.е UserMealWithExcess - это запись одной еды, но поле excess будет одинаково для всех записей за этот день.

        HashMap<LocalDate,Integer> groupCal = new HashMap<>();
        meals.forEach(u->{
           groupCal.put(u.getDateTime().toLocalDate(),
                   groupCal.getOrDefault(u.getDateTime().toLocalDate(),0)+u.getCalories());
        });

        List<UserMealWithExcess> userList = new ArrayList<>();
        meals.forEach(u->{
            if (TimeUtil.isBetweenHalfOpen(u.getDateTime().toLocalTime(),startTime,endTime))
                userList.add(new UserMealWithExcess(u.getDateTime(),u.getDescription(),u.getCalories(),
                    caloriesPerDay>groupCal.get(u.getDateTime().toLocalDate())));
        });
        return userList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        //3.Реализовать метод `UserMealsUtil.filteredByStreams` через Java 8 Stream API.

        //1
        Map<LocalDate,Integer> groupCal = meals.stream().collect(
                Collectors.groupingBy(n->n.getDateTime().toLocalDate(),
                        Collectors.summingInt(n->n.getCalories())
                ));

        List<UserMealWithExcess> userList = meals.stream()
                .filter(u->(TimeUtil.isBetweenHalfOpen(u.getDateTime().toLocalTime(),startTime,endTime)))
                .map(u->new UserMealWithExcess(u.getDateTime()
                        ,u.getDescription()
                        ,u.getCalories(),
                        caloriesPerDay>groupCal.get(u.getDateTime().toLocalDate())))
                .collect(Collectors.toList());

//        //2
//        List<UserMealWithExcess> userList1 = meals.stream().filter(u->(TimeUtil.isBetweenHalfOpen(u.getDateTime().toLocalTime(),startTime,endTime))).map(u->
//                new UserMealWithExcess(u.getDateTime(),u.getDescription(),u.getCalories(),
//                        caloriesPerDay>
//
//        (meals.stream().collect(
//                                Collectors.groupingBy(k->k.getDateTime().toLocalDate(),Collectors.summingInt(k->u.getCalories()))).get(u.getDateTime().toLocalDate())
//                )
//        )).collect(Collectors.toList());


        return userList;
    }
}
