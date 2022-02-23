package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.postgresql.core.Encoding;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.Util;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal actual = service.get(MEAL_USER_1.getId(),USER_ID);
        Meal newMeal = MEAL_USER_1;
        assertMatch(actual,newMeal);
    }

    @Test
    public void delete() {
        service.delete(MEAL_USER_1.getId(),USER_ID);
        assertThrows(NotFoundException.class, ()-> service.get(MEAL_USER_1.getId(),USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        LocalDate begin = LocalDate.of(2020, Month.JANUARY,30);
        LocalDate end   = LocalDate.of(2020, Month.JANUARY,30);
        List<Meal> mealListActual = service.getBetweenInclusive(begin, end, USER_ID);

//        List<Meal> meals = MEALS_USER.values()
//                .stream()
//                .filter(n-> n.getDate().compareTo(begin)>=0 && n.getDate().compareTo(end)<=0)
//                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
//                .collect(Collectors.toList());

        assertMatch(mealListActual,MEAL_USER_3,MEAL_USER_2, MEAL_USER_1);

    }

    @Test
    public void update() {
        Meal mealUpdate = getUpdated();
        service.update(mealUpdate,USER_ID);
        assertMatch(service.get(mealUpdate.getId(),USER_ID),mealUpdate);
    }

    @Test
    public void create() {
        Meal actual = service.create(getNew(),USER_ID);
        Meal newMeal = getNew();
        newMeal.setId(actual.getId());
        assertMatch(actual,newMeal);
        assertMatch(service.get(actual.getId(),USER_ID),newMeal);
    }

    @Test
    public void deleteOther() {
        assertThrows(NotFoundException.class, ()-> service.get(NOT_MEAL_USER_ID, USER_ID));
    }

    @Test
    public void getOther() {
        assertThrows(NotFoundException.class, ()-> service.get(NOT_MEAL_USER_ID, USER_ID));
    }

    @Test
    public void updateOther() {
        Meal mealUpdate = getUpdated();
        assertThrows(NotFoundException.class, ()->
                service.update(mealUpdate,ADMIN_ID));
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, ()->
                service.create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Дубликат", 111),USER_ID));
    }

//    @Test
//    public void getAll() {
//        List<Meal> mealList = service.getAll(USER_ID);
//
//        assertMatch(mealList,
//                MEALS_USER
//                        .values()
//                        .stream()
//                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
//                        .collect(Collectors.toList()));
//    }



}