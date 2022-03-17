package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    private MealService service;

//    @GetMapping("/")
//    public String root() {
//        log.info("root");
//        return "index";
//    }

    @GetMapping("/meals")
    public String getMeals(Model model,HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        String action = request.getParameter("action");
        action = action == null ? "all" : action;

        List<MealTo> mealTos = null;
        if (action.equals("filter")) {
            LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
            LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
            LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
            LocalTime endTime = parseLocalTime(request.getParameter("endTime"));

            log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);

            List<Meal> mealsDateFiltered = service.getBetweenInclusive(startDate, endDate, userId);
            mealTos = MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
        }else if(action.equals("mealform")){
            //request.setCharacterEncoding("UTF-8");
            Meal meal = new Meal(
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));
            if (StringUtils.hasLength(request.getParameter("id"))) {
                int id = getId(request);
                assureIdConsistent(meal, id);
                log.info("update {} for user {}", meal, userId);
                service.update(meal, userId);
            } else {
                checkNew(meal);
                log.info("create {} for user {}", meal, userId);
                meal = service.create(meal, userId);
            }
        }else if(action.equals("delete")){
            int id = getId(request);
            log.info("delete meal {} for user {}", id, userId);
            service.delete(id, userId);
        }else if(action.equals("create") || action.equals("update")){
            final Meal meal = "create".equals(action) ?
                    new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000):
                    service.get(getId(request), userId);
//            request.setAttribute("meal", meal);
//            request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
            model.addAttribute("meal", meal);
            return "mealForm";
        }

        if (mealTos==null)
            mealTos = MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay());

        log.info("meals");
        model.addAttribute("meals", mealTos);
        return "meals";

    }

    @PostMapping("/meals")
    public String setMeals(HttpServletRequest request, Model model) {

        log.info("NOT RIGHT!!!!!");
//        SecurityUtil.setAuthUserId(userId);
        return "redirect:meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
