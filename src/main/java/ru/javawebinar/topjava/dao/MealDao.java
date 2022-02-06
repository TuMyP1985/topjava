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

    public MealDao() {
        this.connection = DbUtils.getConnection();
    }

    public void addMeal(MealTo mealTo) {
        try {
            PreparedStatement pr = connection
                    .prepareStatement("INSERT INTO Meals(dateTime, description, calories, excess) values (?,?,?,?) ");
            pr.setTimestamp(1, Timestamp.valueOf(mealTo.getDateTime()));
            pr.setString(2, mealTo.getDescription());
            pr.setInt(3, mealTo.getCalories());
            pr.setBoolean(4, mealTo.isExcess());
            pr.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public void updateMeal(MealTo mealTo) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("update Meals set dateTime=?, description=?, calories=?" +
                            "where Id=?");
            // Parameters start with 1

            preparedStatement.setTimestamp(1, Timestamp.valueOf(mealTo.getDateTime()));
            preparedStatement.setString(2, mealTo.getDescription());
            preparedStatement.setInt(3, mealTo.getCalories());
            //preparedStatement.setBoolean(4, mealTo.isExcess());
            preparedStatement.setInt(4, mealTo.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MealTo getMealById(int mealId) {
        MealTo mealTo = new MealTo();
        try {
            PreparedStatement preparedStatement = connection.
                    prepareStatement("select * from Meals where id=?");
            preparedStatement.setInt(1, mealId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                mealTo.setId(rs.getInt("id"));
                mealTo.setDateTime(rs.getTimestamp("dateTime").toLocalDateTime());
                mealTo.setDescription(rs.getString("description"));
                mealTo.setCalories(rs.getInt("calories"));
                mealTo.setExcess(rs.getBoolean("excess"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mealTo;
    }

    public List<MealTo> getAllMeals() {
        List<MealTo> list = new ArrayList<>();
        try {
            Statement pr = connection.createStatement();
            ResultSet resultSet = pr.executeQuery("Select * from Meals");
            while (resultSet.next()){
                MealTo mealTo = new MealTo();
                mealTo.setId(resultSet.getInt("id"));
                mealTo.setDateTime(resultSet.getTimestamp("dateTime").toLocalDateTime());
                mealTo.setDescription(resultSet.getString("description"));
                mealTo.setCalories(resultSet.getInt("calories"));
                mealTo.setExcess(resultSet.getBoolean("excess"));
                list.add(mealTo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
