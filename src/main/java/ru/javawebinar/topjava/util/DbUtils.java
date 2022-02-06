package ru.javawebinar.topjava.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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

                connection.setAutoCommit(false);
                Statement stmt = null;
                stmt = connection.createStatement();
                stmt.execute("CREATE TABLE Meals( " +
                        "    id             BIGINT(20)  NOT NULL AUTO_INCREMENT," +
                        "    dateTime       datetime      NULL," +
                        "    description    VARCHAR(50) NULL," +
                        "    calories       INT(10)     NULL," +
                        "    excess         BOOLEAN     NULL," +
                        "    PRIMARY KEY (id)" +
                        "" +
                        ")");
                stmt.execute("INSERT INTO Meals(dateTime, description, calories, excess)" +
                        "VALUES ('2020-01-30 10:00:00', 'Завтрак', 500, false)," +
                        "        ('2020-01-30 13:00:00', 'Обед', 1000, false)," +
                        "        ('2020-01-30 20:00:00', 'Ужин', 500, false)," +
                        "        ('2020-01-31 00:00:00', 'Еда на граничное значение', 100, true)," +
                        "        ('2020-01-31 10:00:00', 'Завтрак', 1000, true)," +
                        "        ('2020-01-31 13:00:00', 'Обед', 500, true)," +
                        "        ('2020-01-31 20:00:00', 'Ужин', 410, true);");


            }catch (Exception e){
                e.printStackTrace();
            }
            return connection;
        }
    }
}
