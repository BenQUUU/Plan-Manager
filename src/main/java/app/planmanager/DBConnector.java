package app.planmanager;
import java.sql.*;

public class DBConnector {
    public Connection connectToDatabase(String dbName, String user, String password){
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbName, user, password);
            if(connection != null){
                System.out.println("Connected");
            }else{
                System.out.println("Failed");
            }
        } catch (SQLException | ClassNotFoundException exceptionID) {
            //handle exception, from forName (ClassNotFoundException) and sql (SQLException);
            //send exception id to another class, and handle it, then print for user with a specific text
            System.out.println("Error: " + exceptionID);
        }
        return connection;
    }

    public void fetchUserEmailByUserId(Connection connection, int userID){
        PreparedStatement statement;
        ResultSet resultSet = null;

        try {
            String getUserIdQuery = "SELECT \"Email\" FROM public.\"Users\" WHERE \"UserID\" = ?;";

            statement = connection.prepareStatement(getUserIdQuery);
            statement.setInt(1, userID);
            resultSet = statement.executeQuery();

            if (!resultSet.next()){
                System.out.println("No user found");
            }else{
                while(resultSet.next()){
                    System.out.println(resultSet.getString("Email"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }
}
