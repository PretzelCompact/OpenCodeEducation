package stage2.practice.two.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    /*
    Устанавливает соединение с базой данных (H2)
     */
    private String pathToDB;

    private final String pathPrefixToDB = "jdbc:h2:file:";
    private final String pathPostfixToDb = ";AUTO_SERVER=TRUE";
    private final String driverName = "org.h2.Driver";

    public DatabaseConnector(String pathToDB){
        try{
            Class.forName(driverName);
        } catch (ClassNotFoundException e){
            System.out.println("Неверное имя драйвера БД");
        }
        this.pathToDB = pathToDB;
    }

    public Connection getConnection(String username, String password){
        Connection connection;
        try{
            connection = DriverManager.getConnection(pathPrefixToDB + pathToDB + pathPostfixToDb,username,password);
        } catch (SQLException e){
            System.out.println("Ошибка подключения к БД");
            connection = null;
        }
        return connection;
    }
}
