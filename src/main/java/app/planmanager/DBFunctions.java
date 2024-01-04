package app.planmanager;

import java.math.BigInteger;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

                User user = new User(name, surname, email,"1", Group.valueOf(group.toLowerCase()));
                user.setUserID_(userId);
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public ArrayList<Lesson> getAllPlanInformation(Connection connection) {
        PreparedStatement statement;
        ResultSet resultSet;

        ArrayList<Lesson> lessonList = new ArrayList<>();

        try{
            String getPlanInformation = """
                    SELECT *
                    FROM public."INF_1" as i
                    INNER JOIN public."Subjects" as s ON i."Subject" = s."SubjectID"
                    INNER JOIN public."Users" as u ON s."SubjectTeacher" = u."UserID";""";
            statement = connection.prepareStatement(getPlanInformation);
            resultSet = statement.executeQuery();

            while(resultSet.next()){
                Lesson lesson = new Lesson(
                        resultSet.getString("DayName"),
                        resultSet.getInt("LessonNumber"),
                        resultSet.getInt("Classroom"),
                        resultSet.getString("SubjectName"),
                        resultSet.getString("Name"),
                        resultSet.getString("Surname")
                );
                lessonList.add(lesson);
            }
            return lessonList;
        }catch (SQLException e){
            System.out.println("Error: " + e);
            return null;
        }
    }

    public void checkEmailAndPasswordValidity(Connection connection, String email, String password){
        PreparedStatement statement;

        try{
            String checkEmailAndPasswordQuery = "SELECT \"Email\", \"Password\" FROM public\"Users\" WHERE \"Email\" = ? AND \"Password\" = ?;";
            statement = connection.prepareStatement(checkEmailAndPasswordQuery);
            statement.setString(1, email);
            statement.setString(2, hashPassword(password));

        }catch (SQLException | NoSuchAlgorithmException e){
            System.out.println("Error: " + e);
        }
    }

    public void registerUser(Connection connection, User user){
        // name, surname, email, group, userID
        PreparedStatement statement;
        ResultSet resultSet;

        try{
            String registerUserQuery = "INSERT INTO public.\"Users\"(\n" +
                    "\"Name\", \"Surname\", \"Email\", \"Password\", \"Group\")\n" +
                    "\tVALUES (?, ?, ?, ?, ?);";
            statement = connection.prepareStatement(registerUserQuery);
            statement.setString(1, user.getName_());
            statement.setString(2, user.getSurname_());
            statement.setString(3, user.getEmail_());



            statement.setString(4, hashPassword(user.getPassword_()));
            statement.setString(5, user.getGroup_().toUpperCase());
            statement.executeUpdate();
        }catch (SQLException | NoSuchAlgorithmException e ){
            System.out.println("Error: " + e);
        }
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");

        byte[] md = messageDigest.digest(password.getBytes());
        BigInteger bigInteger = new BigInteger(1, md);

        return bigInteger.toString(16);
    }
}

