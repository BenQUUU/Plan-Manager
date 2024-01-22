package app.planmanager;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DBFunctions {

    public ArrayList<Lesson> getAllPlanInformation(Connection connection, String dayName) {
        PreparedStatement statement;
        ResultSet resultSet;

        ArrayList<Lesson> lessonList = new ArrayList<>();
        try {
            String getPlanInformation = """
                    SELECT *
                    FROM public."INF_1" as i
                    INNER JOIN public."Subjects" as s ON i."Subject" = s."SubjectID"
                    INNER JOIN public."Users" as u ON s."SubjectTeacher" = u."UserID"
                    WHERE "DayName" = ?;""";
            statement = connection.prepareStatement(getPlanInformation);
            statement.setString(1, dayName);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
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
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            return null;
        }
    }

//    public User getUserEmailAndPassword(Connection connection, String email, String password) {
//        PreparedStatement statement;
//        ResultSet resultSet;
//
//        try {
//            String getUserEmailAndPassword = "SELECT \"Email\", \"Password\" FROM public.\"Users\" WHERE \"Email\" = ? AND \"Password\" = ?;";
//
//            statement = connection.prepareStatement(getUserEmailAndPassword);
//            statement.setString(1, email);
//            statement.setString(2, password);
//            resultSet = statement.executeQuery();
//
//            if (resultSet.next()) {
//                //If resultSet is not empty, then get rest of user information
//                return getAllUserInformation(connection, email);
//            } else {
//                throw new UserPrincipalNotFoundException("Incorrect email or password!");
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Error: " + e);
//            return null;
//        } catch (UserPrincipalNotFoundException e) {
//            System.out.println("Error: " + e.getName());
//            return null;
//        }
//    }

//    private User getAllUserInformation(Connection connection, String email) {
//        PreparedStatement statement;
//        ResultSet resultSet;
//
//        try {
//            String getAllUserInformationQuery = "SELECT * FROM public.\"Users\" WHERE \"Email\" = ?;";
//            statement = connection.prepareStatement(getAllUserInformationQuery);
//            statement.setString(1, email);
//            resultSet = statement.executeQuery();
//
//            if (resultSet.next()) {
//
//                int userId = resultSet.getInt("UserID");
//                String name = resultSet.getString("Name");
//                String surname = resultSet.getString("Surname");
//                String group = resultSet.getString("Group");
//                String password = resultSet.getString("Password"); // HASHED
//                User user = new User(name, surname, email, password, Group.valueOf(group.toLowerCase()));
//                user.setUserID_(userId);
//                return user;
//            } else {
//                return null;
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

    public User checkEmailAndPasswordValidity(Connection connection, String email, String password) {
        PreparedStatement statement;
        ResultSet resultSet;

        try {
            String checkEmailAndPasswordQuery = "SELECT * FROM public.\"Users\" WHERE \"Email\" = ? AND \"Password\" = ?;";
            statement = connection.prepareStatement(checkEmailAndPasswordQuery);
            statement.setString(1, email);
            statement.setString(2, hashPassword(password));
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String groupName = resultSet.getString("Group").toLowerCase();
                User user = new User(
                        resultSet.getString("Name"),
                        resultSet.getString("Surname"),
                        resultSet.getString("Email"),
                        resultSet.getString("Password"),
                        Group.valueOf(groupName));
                user.setUserID_(resultSet.getInt("UserID"));
                return user;
            } else {
                return null;
            }

        } catch (SQLException | NoSuchAlgorithmException e) {
            System.out.println("Error: " + e);
            return null;
        }
    }

    public void registerUser(Connection connection, User user) {
        // name, surname, email, group, userID
        PreparedStatement statement;

        try {
            String registerUserQuery = """
                    INSERT INTO public."Users"(
                    "Name", "Surname", "Email", "Password", "Group")
                    \tVALUES (?, ?, ?, ?, ?);""";
            statement = connection.prepareStatement(registerUserQuery);
            statement.setString(1, user.getName_());
            statement.setString(2, user.getSurname_());
            statement.setString(3, user.getEmail_());
            statement.setString(4, hashPassword(user.getPassword_()));
            statement.setString(5, user.getGroup_().toUpperCase());
            statement.executeUpdate();
        } catch (SQLException | NoSuchAlgorithmException e) {
            System.out.println("Error: " + e);
        }
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        byte[] md = messageDigest.digest(password.getBytes());
        BigInteger bigInteger = new BigInteger(1, md);

        return bigInteger.toString(16);
    }

    public ArrayList<String> getAllTablesName(Connection connection) {

        ArrayList<String> resultArray = new ArrayList<>();
        ResultSet resultSet;

        try {
            DatabaseMetaData metaData = connection.getMetaData();

            resultSet = metaData.getTables(null, "public" , "%", new String[]{"TABLE"});

            while(resultSet.next()){
                String tableName = resultSet.getString("TABLE_NAME");
                if(!isTableNameInIgnoredArray(tableName)){
                    resultArray.add(tableName);
                }
            }
            return resultArray;
        } catch (SQLException e) {
            System.out.println("Error " + e);
        }
        return null;
    }

    private boolean isTableNameInIgnoredArray(String tableName) { // returns true if table name SHOULD BE IGNORED
        ArrayList<String> tableNamesToIgnore = new ArrayList<>(Arrays.asList("Users", "Subjects", "Major"));
        for (String s : tableNamesToIgnore) {
            if (s.equals(tableName)) {
                return true;
            }
        }
        return false;
    }
}