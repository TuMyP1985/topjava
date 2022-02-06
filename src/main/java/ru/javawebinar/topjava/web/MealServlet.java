package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final Logger log = getLogger(MealServlet.class);
    private static String INSERT_OR_EDIT = "/mealEdit.jsp";
    private static String LIST_MEALS = "/meals.jsp";
    private MealDao dao;

    public MealServlet() {
        super();
        dao = new MealDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

//        request.getRequestDispatcher("/users.jsp").forward(request, response);
//        response.sendRedirect("meals.jsp");

        List<MealTo> mealToList = MealsUtil.findAll();
//        request.setAttribute("meals", mealToList);
//        request.getRequestDispatcher("meals.jsp").forward(request, response);

        String forward = "";
        String action = request.getParameter("action");
        action = action==null?"listMeals":action;
        if (action.equalsIgnoreCase("delete")){
            int id = Integer.parseInt(request.getParameter("mealId"));
            dao.deleteMeal(id);
            forward = LIST_MEALS;
            request.setAttribute("name", dao.getAllMeals());
        } else if (action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            int mealToId = Integer.parseInt(request.getParameter("mealToId"));
            MealTo mealTo = dao.getMealById(mealToId);
            request.setAttribute("mealTo", mealTo);
        } else if (action.equalsIgnoreCase("listMeals")){
            forward = LIST_MEALS;
            //request.setAttribute("meals", dao.getAllMeals());
            request.setAttribute("meals", mealToList);
        } else {
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getParameter("bottName").equalsIgnoreCase("cancel")){
            List<MealTo> mealToList = MealsUtil.findAll();

            //request.setAttribute("meals", dao.getAllMeals());
            request.setAttribute("meals", mealToList);
            RequestDispatcher view = request.getRequestDispatcher(LIST_MEALS);
            view.forward(request, response);
        }

        MealTo mealTo = new MealTo();
        mealTo.setDescription(request.getParameter("description"));
        mealTo.setCalories(Integer.parseInt(request.getParameter("calories")));
        mealTo.setDateTime(LocalDateTime.parse(request.getParameter("dateTime"), formatter));

        String mealToId = request.getParameter("mealToId");
        if(mealToId == null || mealToId.isEmpty())
            dao.addMeal(mealTo);
        else
        {
            //mealTo.setId(Integer.parseInt(mealToId));
            dao.updateMeal(mealTo);
        }
        RequestDispatcher view = request.getRequestDispatcher(LIST_MEALS);
        request.setAttribute("meals", dao.getAllMeals());
        view.forward(request, response);
    }
}
