package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DbUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MealDao implements MealRepository{
    private Connection connection;

    public MealDao() {
        this.connection = DbUtils.getConnection();
    }

    @Override
    public void addMeal(Meal meal) {
        try {
            PreparedStatement pr = connection
                    .prepareStatement("INSERT INTO Meals(dateTime, description, calories) values (?,?,?) ");
            pr.setTimestamp(1, Timestamp.valueOf(meal.getDateTime()));
            pr.setString(2, meal.getDescription());
            pr.setInt(3, meal.getCalories());
            //pr.setBoolean(4, meal.isExcess());
            pr.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMeal(int mealId) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("delete from Meals where id=?");
            // Parameters start with 1
            preparedStatement.setInt(1, mealId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateMeal(Meal meal) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("update Meals set dateTime=?, description=?, calories=?" +
                            "where Id=?");
            // Parameters start with 1

            preparedStatement.setTimestamp(1, Timestamp.valueOf(meal.getDateTime()));
            preparedStatement.setString(2, meal.getDescription());
            preparedStatement.setInt(3, meal.getCalories());
            //preparedStatement.setBoolean(4, meal.isExcess());
            preparedStatement.setInt(4, meal.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Meal getMealById(int mealId) {
        Meal meal = new Meal();
        try {
            PreparedStatement preparedStatement = connection.
                    prepareStatement("select * from Meals where id=?");
            preparedStatement.setInt(1, mealId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                meal.setId(rs.getInt("id"));
                meal.setDateTime(rs.getTimestamp("dateTime").toLocalDateTime());
                meal.setDescription(rs.getString("description"));
                meal.setCalories(rs.getInt("calories"));
//                meal.setExcess(rs.getBoolean("excess"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return meal;
    }

    @Override
    public List<Meal> getAllMeals() {
        List<Meal> list = new ArrayList<>();
        try {
            Statement pr = connection.createStatement();
            ResultSet resultSet = pr.executeQuery("Select * from Meals");
            while (resultSet.next()){
                Meal meal = new Meal();
                meal.setId(resultSet.getInt("id"));
                meal.setDateTime(resultSet.getTimestamp("dateTime").toLocalDateTime());
                meal.setDescription(resultSet.getString("description"));
                meal.setCalories(resultSet.getInt("calories"));
//                meal.setExcess(resultSet.getBoolean("excess"));
                list.add(meal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
