package app.planmanager;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBFunctions {
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
