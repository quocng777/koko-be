package com.quocnguyen.koko.auth;

/**
 * @author Quoc Nguyen on {7/28/2024}
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcTest {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/kokoDB";
    static final String USER = "postgres";
    static final String PASSWORD = "123456";
    static final String QUERY_INSERT = "INSERT INTO users (name, email, gender, status) values('Quoc Nguyen', 'quocng777@gmail.com', 0, 0)";
    static final String QUERY_SELECT = "SELECT id, name from users";
    static final String QUERY_INSERT2 = "INSERT INTO users (name, email,gender, status) values('Quoc Nguyen', 'quocng778@gmail.com', 0, 0)";


    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;

        try {
            System.out.println("Connect to database");

            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            conn.setAutoCommit(false);

            System.out.println("Insert value");
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            stmt.executeUpdate(QUERY_INSERT);

            ResultSet rs = stmt.executeQuery(QUERY_SELECT);

            rs.beforeFirst();
            while(rs.next()){
                // Display values
                System.out.print("ID: " + rs.getInt("id"));
                System.out.print("Name: " + rs.getString("name"));

            }

            stmt.executeUpdate(QUERY_INSERT2);


            rs.close();
            stmt.close();
            conn.close();

        } catch(SQLException se) {
            se.printStackTrace();

            System.out.println("Rolling back data");


        }
    }
}
