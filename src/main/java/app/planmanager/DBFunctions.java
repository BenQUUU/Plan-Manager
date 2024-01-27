package app.planmanager;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class DBFunctions {

    public ArrayList<Lesson> getAllPlanInformation(Connection connection, String dayName, String major) {
        PreparedStatement statement;
        ResultSet resultSet;

        ArrayList<Lesson> lessonList = new ArrayList<>();
        try {
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
            Comparator<Lesson> lessonComparator = Comparator.comparing(Lesson::getLessonNumber);

            lessonList.sort(lessonComparator);
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

    public boolean registerUser(Connection connection, User user) {
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
            return false;
        }
        return true;
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

    public boolean editPlan(Connection connection, EditPlanContainer planContainer){

        PreparedStatement statement;


        int subjectId = getSubjectId(connection,planContainer.subject());
        if(subjectId == -1){
            System.out.println("Error, no subject in DB");
            return false;
        }

        String editPlanQuery = "";
        try{
            if(doesRowExistsInTable(connection, planContainer.planName(), planContainer.lessonNumber())){
                editPlanQuery = "UPDATE public." + "\"" + planContainer.planName() + "\" " +
                        "SET \"DayName\" = ?, \"Classroom\" = ?, \"Subject\" = ? " +
                        "WHERE \"LessonNumber\" = ?;";

                statement = connection.prepareStatement(editPlanQuery);

                statement.setString(1, planContainer.dayName());
                statement.setInt(2, planContainer.classroom());
                statement.setInt(3, subjectId);
                statement.setInt(4, planContainer.lessonNumber());
            }else{
                editPlanQuery = "INSERT INTO public." + "\"" + planContainer.planName() + "\" " +
                        "(\"DayName\", \"LessonNumber\", \"Classroom\", \"Subject\") " +
                        "VALUES (?, ?, ? ,?);";

                statement = connection.prepareStatement(editPlanQuery);

                statement.setString(1, planContainer.dayName());
                statement.setInt(2, planContainer.lessonNumber());
                statement.setInt(3, planContainer.classroom());
                statement.setInt(4, subjectId);
            }
            System.out.println(editPlanQuery);

            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println("Error with edit: " + e);
            return false;
        }
        return true;
    }

    public boolean deletePlan(Connection connection, String planName){
        PreparedStatement statement;

        try{
            String deletePlanQuery = "DROP TABLE public.\""+ planName +"\";";
            statement = connection.prepareStatement(deletePlanQuery);

            statement.executeUpdate();
            return true;
        }catch(SQLException e ){
            System.out.println("Error Delete " + e);
            return false;
        }
    }

    public ArrayList<String> getAllSubjectsFromDB(Connection connection){
        PreparedStatement statement;
        ResultSet resultSet;
        ArrayList<String> subjectArrayList = new ArrayList<>();
        try{
            String getAllSubjectsFromDBQuery = "SELECT \"SubjectName\" FROM public.\"Subjects\"";
            statement = connection.prepareStatement(getAllSubjectsFromDBQuery);
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                subjectArrayList.add(resultSet.getString("SubjectName"));
            }
        }catch (SQLException e){
            System.out.println("Error WITH subjects from DB" + e );
            return null;
        }
        return subjectArrayList;
    }

    private boolean doesRowExistsInTable(Connection connection, String planName, int lessonNumber){
        PreparedStatement statement;

        try{
            String checkRowExistenceQuery = "SELECT 1 FROM public." + "\"" + planName + "\" " +
                    "WHERE \"LessonNumber\" = ?";
            statement = connection.prepareStatement(checkRowExistenceQuery);
            statement.setInt(1, lessonNumber);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }catch (SQLException e ){
            System.out.println("Error With Row Existence");
            return false;
        }
    }

    private int getSubjectId(Connection connection, String subjectName){
        PreparedStatement statement;
        ResultSet resultSet;
        try{
            String getSubjectIdQuery = "SELECT \"SubjectID\" FROM public.\"Subjects\" WHERE \"SubjectName\" = ?;";
            statement = connection.prepareStatement(getSubjectIdQuery);
            statement.setString(1, subjectName);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt("SubjectID");
            }
            return -1;
        }catch (SQLException e ){
            System.out.println("Error " + e);
        }
        return -1;
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

