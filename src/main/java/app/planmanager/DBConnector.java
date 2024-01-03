package app.planmanager;

import java.nio.file.attribute.UserPrincipalNotFoundException;
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

    public User getUserEmailAndPassword(Connection connection, String email, String password) {
        PreparedStatement statement;
        ResultSet resultSet;

        try {
            String getUserEmailAndPassword = "SELECT \"Email\", \"Password\" FROM public.\"Users\" WHERE \"Email\" = ? AND \"Password\" = ?;";

            statement = connection.prepareStatement(getUserEmailAndPassword);
            statement.setString(1, email);
            statement.setString(2, password);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                //If resultSet is not empty, then get rest of user information
                return getAllUserInformation(connection, email);
            } else {
                throw new UserPrincipalNotFoundException("Incorrect email or password!");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e);
            return null;
        } catch (UserPrincipalNotFoundException e) {
            System.out.println("Error: " + e.getName());
            return null;
        }
    }

    private User getAllUserInformation(Connection connection, String email) {
        PreparedStatement statement;
        ResultSet resultSet;

        try {
            String getAllUserInformationQuery = "SELECT * FROM public.\"Users\" WHERE \"Email\" = ?;";
            statement = connection.prepareStatement(getAllUserInformationQuery);
            statement.setString(1, email);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {

                int userId = resultSet.getInt("UserID");
                String name = resultSet.getString("Name");
                String surname = resultSet.getString("Surname");
                String group = resultSet.getString("Group");

                return new User(userId, name, surname, email, Group.valueOf(group.toLowerCase()));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}