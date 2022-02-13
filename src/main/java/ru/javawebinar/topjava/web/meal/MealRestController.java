package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealTo> getAll() {
        log.info("getAll");
        return service.getAll(authUserId());
    }

    public MealTo get(int id) {
        log.info("get {}", id);
        return service.get(id, authUserId());
    }

    public MealTo create(MealTo mealTo) {
        log.info("create {}", mealTo);
        checkNew(mealTo);
        return service.create(mealTo, authUserId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, authUserId());
    }

    public void update(MealTo mealTo, int id) {
        log.info("update {} with id={}", mealTo, id);
        assureIdConsistent(mealTo, id);
        service.update(mealTo, authUserId());
    }

    public List<MealTo> getAllFilter(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        log.info("getAll filter");
        return service.getAllFilter(authUserId(), startDate, startTime, endDate, endTime);
    }


}