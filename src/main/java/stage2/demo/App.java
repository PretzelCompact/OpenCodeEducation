package stage2.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class App {

    private static final String pathToDB = "jdbc:h2:file:D:/database/testDB;AUTO_SERVER=TRUE";
    private static final String username = "User";
    private static final String password = "password";

    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("org.h2.Driver");
        try(Connection conn = DriverManager.getConnection(pathToDB,username,password);
            Statement stat = conn.createStatement()){
            stat.execute("create table test(id int primary key, name varchar(255))");
            stat.execute("insert into test values(1, 'Hello')");
            try(var rs = stat.executeQuery("select * from test")){
                while(rs.next()){
                    System.out.println(rs.getString("name"));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
