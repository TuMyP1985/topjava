package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository.*;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRepository repository;
    private ConfigurableApplicationContext appCtx;
    private MealRestController сontroller;
    private Integer idUserTest = 1;
    private boolean idEx = true;

    @Override
    public void init() {
        repository = new InMemoryMealRepository();
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        сontroller = appCtx.getBean(MealRestController.class);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getParameter("Esc") != null) {
            log.info("getAll");
            Collection<Meal> meals = сontroller.getAll().stream().map(m -> (createFromTo(m, idUserTest))).collect(Collectors.toList());
            request.setAttribute("meals",MealsUtil.getTos(meals, DEFAULT_CALORIES_PER_DAY));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        } else if (request.getParameter("Filtr") != null)
        {
            log.info("getAll filter");
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String startTime = request.getParameter("startTime");
            String endTime = request.getParameter("endTime");

            Collection<Meal> meals = сontroller.getAllFilter(startDate.isEmpty()?null:LocalDate.parse(startDate),
                    startTime.isEmpty()?null: LocalTime.parse(startTime),
                    endDate.isEmpty()?null:LocalDate.parse(endDate),
                    endTime.isEmpty()?null:LocalTime.parse(endTime)
            ).stream().map(m -> (createFromTo(m, idUserTest))).collect(Collectors.toList());
            request.setAttribute("meals",MealsUtil.getTos(meals, DEFAULT_CALORIES_PER_DAY));
            request.setAttribute("startDate",startDate);
            request.setAttribute("endDate",endDate);
            request.setAttribute("startTime",startTime);
            request.setAttribute("endTime",endTime);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
//            getAllFilter

            return;
        }


            request.setCharacterEncoding("UTF-8");
            String id = request.getParameter("id");
            Integer userId = SecurityUtil.authUserId();

            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                    SecurityUtil.authUserId(),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));

            log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
            if (meal.isNew())
                сontroller.create(createTo(meal, idEx));
            else
                сontroller.update(createTo(meal, idEx), meal.getId());

//        repository.save(meal, userId);
            response.sendRedirect("meals");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        Integer userId = SecurityUtil.authUserId();


        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
//                repository.delete(id, userId);                                                               //repository
                сontroller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
//                        repository.get(getId(request), userId);                                                 //repository
                        createFromTo(сontroller.get(getId(request)), idUserTest);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals",
//                        MealsUtil.getTos(repository.getAll(userId), MealsUtil.DEFAULT_CALORIES_PER_DAY));       //repository
                        MealsUtil.getTos(сontroller.getAll().stream().map(m -> (createFromTo(m, idUserTest))).collect(Collectors.toList()),
                                MealsUtil.DEFAULT_CALORIES_PER_DAY));
//                request.setAttribute("startDate",);
//                request.setAttribute("endDate",endDate);
//                request.setAttribute("startTime",startTime);
//                request.setAttribute("endTime",endTime);
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        appCtx.close();

    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
