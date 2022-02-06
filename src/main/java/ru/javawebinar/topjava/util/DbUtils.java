package ru.javawebinar.topjava.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DbUtils {
    private static Connection connection;
    public static Connection getConnection(){
        if (connection!=null)
            return connection;
        else
        {
            try{
                Properties prop = new Properties();
                InputStream is = DbUtils.class.getClassLoader().getResourceAsStream("application.properties");
                prop.load(is);
                String driver = prop.getProperty("driver");
                String url = prop.getProperty("url");
                String username = prop.getProperty("username");
                String password = prop.getProperty("password");
                Class.forName(driver);
                connection = DriverManager.getConnection(url,username,password);
            }catch (Exception e){e.printStackTrace();}
            return connection;
        }
    }
}
