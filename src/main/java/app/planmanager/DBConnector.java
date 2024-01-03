package app.planmanager;

import java.sql.*;

public class DBConnector {
    public Connection connectToDatabase(String dbName, String user, String password) {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbName, user, password);
            if (connection != null) {
                System.out.println("Connected");
            } else {
                System.out.println("Failed");
            }
        } catch (SQLException | ClassNotFoundException exceptionID) {
            //handle exception, from forName (ClassNotFoundException) and sql (SQLException);
            //send exception id to another class, and handle it, then print for user with a specific text
            System.out.println("Error: " + exceptionID);
        }
        return connection;
    }

    void closeDatabase(Connection connection){
        try{
            // Close the database connection
            connection.close();
        }catch (SQLException e ){
            // Handle any exceptions that may occur when closing the connection
            System.out.println("Error: " + e);
        }
    }

}