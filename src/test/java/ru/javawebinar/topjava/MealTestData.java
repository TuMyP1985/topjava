package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.inmemory.InMemoryBaseRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int NOT_MEAL_USER_ID = START_SEQ+10;

    public static final Meal MEAL_USER_1 = new Meal(START_SEQ+3, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal MEAL_USER_2 = new Meal(START_SEQ+4, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal MEAL_USER_3 = new Meal(START_SEQ+5, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);


    public static Meal getNew() {
        return new Meal(null,LocalDateTime.of(2020, Month.JANUARY, 1, 1, 1), "Завтрак", 111);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(MEAL_USER_1);
        updated.setDateTime(LocalDateTime.of(2022, Month.FEBRUARY, 1, 1, 1));
        updated.setDescription("смена ужина на завтрак");
        updated.setCalories(111);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
