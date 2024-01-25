package app.planmanager;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DBFunctions {

    public ArrayList<Lesson> getAllPlanInformation(Connection connection, String dayName, String major) {
        PreparedStatement statement;
        ResultSet resultSet;

        ArrayList<Lesson> lessonList = new ArrayList<>();
        try {
            if (major == null) {
                major = "INF_1";
            }
            String getPlanInformation = "SELECT * FROM public." + "\"" + major + "\"" + " as i" +
                    " INNER JOIN public.\"Subjects\" as s ON i.\"Subject\" = s.\"SubjectID\"\n" +
                    " INNER JOIN public.\"Users\" as u ON s.\"SubjectTeacher\" = u.\"UserID\" WHERE \"DayName\" = ?;";

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

    public boolean addNewPlanToDB(Connection connection, String planName) {
        PreparedStatement statement;

        try {
            String checkTableExistenceQuery = "SELECT EXISTS ( " +
                    "   SELECT 1 " +
                    "   FROM information_schema.tables " +
                    "   WHERE table_name = ? " +
                    "   )";

            PreparedStatement checkTableExistenceStatement = connection.prepareStatement(checkTableExistenceQuery);
            checkTableExistenceStatement.setString(1, planName);
            ResultSet resultSet = checkTableExistenceStatement.executeQuery();

            if (resultSet.next()) {
                if (resultSet.getBoolean(1)) {
                    System.out.println("TABELA JUŻ ISTNIEJE");
                    return false;
                }
            }

            String addNewPlanToDBQuery = "CREATE TABLE IF NOT EXISTS public." + "\"" + planName + "\"" +
                    " ( " +
                    "    \"DayName\" character varying(16) COLLATE pg_catalog.\"default\" NOT NULL, " +
                    "    \"LessonNumber\" integer NOT NULL, " +
                    "    \"Classroom\" integer NOT NULL, " +
                    "    \"Subject\" integer NOT NULL, " +
                    "    CONSTRAINT \"SubjectId\" FOREIGN KEY (\"Subject\") " +
                    "        REFERENCES public.\"Subjects\" (\"SubjectID\") MATCH SIMPLE " +
                    "        ON UPDATE NO ACTION " +
                    "        ON DELETE NO ACTION " +
                    "        NOT VALID " +
                    ")";
            statement = connection.prepareStatement(addNewPlanToDBQuery);
            statement.executeUpdate();
            System.out.println("UTWORZONO");
        } catch (SQLException e) {
            System.out.println("ERROR NIE UTWORZONO: " + e);
            return false;
        }
        return true;
    }

    public ArrayList<String> getAllTablesName(Connection connection) {

        ArrayList<String> resultArray = new ArrayList<>();
        ResultSet resultSet;

        try {
            DatabaseMetaData metaData = connection.getMetaData();

            resultSet = metaData.getTables(null, "public", "%", new String[]{"TABLE"});

            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                if (!isTableNameInIgnoredArray(tableName)) {
                    resultArray.add(tableName);
                }
            }
            return resultArray;
        } catch (SQLException e) {
            System.out.println("Error " + e);
        }
        return null;
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        byte[] md = messageDigest.digest(password.getBytes());
        BigInteger bigInteger = new BigInteger(1, md);

        return bigInteger.toString(16);
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

//DO $$
//BEGIN
//IF EXISTS (SELECT 1 FROM public."Bartek" WHERE "LessonNumber" = 2) THEN
//UPDATE public."Bartek"
//SET "DayName" = 'Monday', "Classroom" = 100, "Subject" = 1
//WHERE "LessonNumber" = 2;
//ELSE
//INSERT INTO public."Bartek" ("DayName", "LessonNumber", "Classroom", "Subject")
//VALUES ('Monday', 2, 100, 1);
//END IF;
//END $$;