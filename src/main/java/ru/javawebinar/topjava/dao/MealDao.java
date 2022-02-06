package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.DbUtils;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MealDao {
    private Connection connection;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm");

    public MealDao() {
//        this.connection = DbUtils.getConnection();
    }

    public void addMeal(MealTo mealTo) {
        try {
            PreparedStatement pr = connection
                    .prepareStatement("INSERT INTO Meals(dateTime, description, calories) values (?,?,?) ");
            pr.setDate(1, Date.valueOf(mealTo.getDateTime().format(formatter)));
            pr.setString(2, mealTo.getDescription());
            pr.setInt(3, mealTo.getCalories());
            pr.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteMeal(int mealId) {

    }

    public void updateMeal(MealTo mealTo) {

    }

    public MealTo getMealById(int mealId) {
        return null;
    }

    public List<MealTo> getAllMeals() {
        List<MealTo> list = new ArrayList<>();
        try {
            Statement pr = connection.createStatement();
            ResultSet resultSet = pr.executeQuery("Select * from Meals");
            while (resultSet.next()){
                MealTo mealTo = new MealTo();
                mealTo.setDateTime(resultSet.getTimestamp("dateTime").toLocalDateTime());
                mealTo.setDescription(resultSet.getString("description"));
                mealTo.setCalories(resultSet.getInt("calories"));
                list.add(mealTo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
