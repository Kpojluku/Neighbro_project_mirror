package core.dataBase;

import core.constans.Constants;
import core.constans.Status;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class DataBaseHandler extends Configs {
    Connection dbConnection;
    private static DataBaseHandler dataBaseHandlerInstance;


    public DataBaseHandler() {
        String connectionString = "jdbc:postgresql://" + dbHost + ":"
                + dbPort + "/" + dbName;
        try {
            dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized DataBaseHandler getDataBaseHandler() {
        if (dataBaseHandlerInstance == null) {
            dataBaseHandlerInstance = new DataBaseHandler();
        }
        return dataBaseHandlerInstance;
    }


    public void addNewUser(int id) throws SQLException {
        String insert = "INSERT INTO " + Constants.USER_TABLE + " (" +
                Constants.USERS_ID + ", " + Constants.USERS_STATUS + ") " + "VALUES(?, ?)";

        PreparedStatement prSt = dbConnection.prepareStatement(insert);
        prSt.setInt(1, id);
        prSt.setString(2, Status.NEWUSER);
        prSt.executeUpdate();

    }

    public String[] getUserState(int id) throws SQLException {
        ResultSet result;
        String[] state = new String[2];

        String select = "SELECT " + Constants.USERS_STATUS + ", " +
                Constants.USERS_ISACTIVE + " FROM " + Constants.USER_TABLE + " WHERE id = ?";

        PreparedStatement prSt = dbConnection.prepareStatement(select);
        prSt.setInt(1, id);
        result = prSt.executeQuery();
        if(result.next()){
            state[0] = result.getString(1);
            state[1] = result.getString(2);
            return state;
        }
        return new String[2];
    }

    public void setUserStringField(int id, String value, String field) throws SQLException {

        String update = "UPDATE " + Constants.USER_TABLE + " SET " + field + " = ? " +
                "WHERE id = ?";

        PreparedStatement prSt = dbConnection.prepareStatement(update);
        prSt.setString(1, value);
        prSt.setInt(2, id);
        prSt.executeUpdate();

    }

    public void setUserIntField(int id, int value, String field) throws SQLException {

        String update = "UPDATE " + Constants.USER_TABLE + " SET " + field + " = ? " +
                "WHERE id = ?";

        PreparedStatement prSt = dbConnection.prepareStatement(update);
        prSt.setInt(1, value);
        prSt.setInt(2, id);
        prSt.executeUpdate();

    }

    public void setUserLakedState(int WhoLikeId, int id) throws SQLException {

        String update = "UPDATE " + Constants.USER_TABLE +
                " SET " + Constants.USERS_STATUS + " = ?, " +
                Constants.USERS_LASTLIKED + " = ? " +
                "WHERE id = ?";

        PreparedStatement prSt = dbConnection.prepareStatement(update);
        prSt.setString(1, Status.SOMEONELIKED);
        prSt.setInt(2, WhoLikeId);
        prSt.setInt(3, id);
        prSt.executeUpdate();

    }

    public int getLastLikedUserProfile(int id) throws SQLException {
        ResultSet result;

        String select = "SELECT " + Constants.USERS_LASTLIKED  + " FROM " + Constants.USER_TABLE + " WHERE id = ?";

        PreparedStatement prSt = dbConnection.prepareStatement(select);
        prSt.setInt(1, id);
        result = prSt.executeQuery();

        return result.next() ? result.getInt(1) : 0;
    }

    public void changeStatus(int id, String status) throws SQLException {
        setUserStringField(id, status, Constants.USERS_STATUS);
    }

    public String getUserStatus(int id) throws SQLException {
        ResultSet result;

        String select = "SELECT " + Constants.USERS_STATUS  + " FROM " + Constants.USER_TABLE + " WHERE id = ?";

        PreparedStatement prSt = dbConnection.prepareStatement(select);
        prSt.setInt(1, id);
        result = prSt.executeQuery();

        return result.next() ? result.getString(1) : null;
    }

    public Map<String, String> getUserProfile(int id) throws SQLException {
        String select = "SELECT id, name, sex, searchingGender, age, city, description, image, isActive " +
                "FROM " + Constants.USER_TABLE + " WHERE id = ?";

        ResultSet result;
        Map<String, String> userInfo = new HashMap<>();
        PreparedStatement prSt = dbConnection.prepareStatement(select);
        prSt.setInt(1, id);
        result = prSt.executeQuery();
        if (result.next()) {
            userInfo.put("id", String.valueOf(result.getInt(1)));
            userInfo.put("name", result.getString(2));
            userInfo.put("sex", result.getString(3));
            userInfo.put("searchingGender", result.getString(4));
            userInfo.put("age", result.getString(5));
            userInfo.put("city", result.getString(6));
            userInfo.put("description", result.getString(7));
            userInfo.put("image", result.getString(8));
            userInfo.put("isActive", result.getString(9));
        }

        return userInfo;
    }

    public void setUserActivity(int id) throws SQLException {

        String update =
                "UPDATE " + Constants.USER_TABLE +
                " SET " + Constants.USERS_LASTACTIVITYDATE + " = current_date, " +
                Constants.USERS_ISACTIVE + " = 'y' " +
                "WHERE id = ?";

        PreparedStatement prSt = dbConnection.prepareStatement(update);
        prSt.setInt(1, id);
        prSt.executeUpdate();

    }

    public void setUserLastActivity(int id) throws SQLException {

        String update =
                "UPDATE " + Constants.USER_TABLE +
                        " SET " + Constants.USERS_LASTACTIVITYDATE + " = current_date " +
                        "WHERE id = ?";

        PreparedStatement prSt = dbConnection.prepareStatement(update);
        prSt.setInt(1, id);
        prSt.executeUpdate();

    }

    public Map<String, String> getNextUserProfile(int id) throws SQLException {
        String select = getNextProfileQuery();

        ResultSet result;
        Map<String, String> userInfo = new HashMap<>();
        PreparedStatement prSt = dbConnection.prepareStatement(select);
        prSt.setInt(1, id);
        prSt.setInt(2, id);
        prSt.setInt(3, id);
        prSt.setInt(4, id);
        result = prSt.executeQuery();
        if (result.next()) {
            userInfo.put("id", String.valueOf(result.getInt(1)));
            userInfo.put("name", result.getString(2));
            userInfo.put("age", result.getString(3));
            userInfo.put("city", result.getString(4));
            userInfo.put("description", result.getString(5));
            userInfo.put("image", result.getString(6));
        }
        //в этом случае падает
        return userInfo;
    }

    public void setPreviousUserId(int id, int prevId) throws SQLException {

        String update =
                "UPDATE " + Constants.USER_TABLE +
                " SET " + Constants.USERS_PREVIOUSUSERID + " = ? " +
                "WHERE id = ?";

        PreparedStatement prSt = dbConnection.prepareStatement(update);
        prSt.setInt(1, prevId);
        prSt.setInt(2, id);
        prSt.executeUpdate();

    }

    public int getPreviousUserId(int id) throws SQLException {
        ResultSet result;

        String select = "SELECT " + Constants.USERS_PREVIOUSUSERID + " FROM " + Constants.USER_TABLE + " WHERE id = ?";

        PreparedStatement prSt = dbConnection.prepareStatement(select);
        prSt.setInt(1, id);
        result = prSt.executeQuery();

        if(result.next()){
            return result.getInt(1);
        }
        return 0;
    }

    public void addUserToLikeTable(int id, int anotherId) throws SQLException {
        String insert = "INSERT INTO " + Constants.USER_LIKE + " (" +
                Constants.USERS_ID + ", " + Constants.ANOTHER_ID + ") " + "VALUES(?, ?)";

        PreparedStatement prSt = dbConnection.prepareStatement(insert);
        prSt.setInt(1, id);
        prSt.setInt(2, anotherId);
        prSt.executeUpdate();
    }

    public void addUserToDislikeTable(int id, int anotherId) throws SQLException {
        String SQL = "CALL neighbro.addDislike(?, ?)";

        PreparedStatement prSt = dbConnection.prepareCall(SQL);
        prSt.setInt(1, id);
        prSt.setInt(2, anotherId);
        prSt.executeUpdate();
    }

    public boolean isMatch(int id, int anotherId) throws SQLException {
        ResultSet result;

        String select = "SELECT * FROM " + Constants.USER_LIKE + " WHERE " + Constants.USERS_ID + " = ? " +
                "AND " + Constants.ANOTHER_ID + " = ?";

        PreparedStatement prSt = dbConnection.prepareStatement(select);
        prSt.setInt(1, id);
        prSt.setInt(2, anotherId);
        result = prSt.executeQuery();

        return result.next();
    }

    public void offProfile(int id) throws SQLException {

        String update =
                "UPDATE " + Constants.USER_TABLE +
                        " SET " + Constants.USERS_ISACTIVE + " = 'n' " +
                        "WHERE id = ?";

        PreparedStatement prSt = dbConnection.prepareStatement(update);
        prSt.setInt(1, id);
        prSt.executeUpdate();

    }

    public boolean isUserPhotoExist(int id) throws SQLException {
        ResultSet result;

        String select = "SELECT " + Constants.USERS_IMAGE + " FROM " + Constants.USER_TABLE + " WHERE id = ?";

        PreparedStatement prSt = dbConnection.prepareStatement(select);
        prSt.setInt(1, id);
        result = prSt.executeQuery();

        return result.next();
    }

    // search algorithm
    private String getNextProfileQuery() {
        return "WITH userProfile\n" +
                "as (SELECT * FROM neighbro.users WHERE id = ?),\n" +
                "getSearchingGender as\n" +
                "(SELECT  \n" +
                "CASE\n" +
                "    WHEN searchingGender = 'm' \n" +
                "        THEN searchingGender\n" +
                "    WHEN searchingGender ='f' \n" +
                "        THEN searchingGender\n" +
                "    WHEN searchingGender = 'a' \n" +
                "        THEN 'm'\n" +
                "END searchingGender\n" +
                "FROM userProfile\n" +
                "UNION\n" +
                "SELECT  \n" +
                "CASE\n" +
                "    WHEN searchingGender = 'a' \n" +
                "        THEN 'f'\n" +
                "END searchingGender\n" +
                "FROM userProfile)\n" +
                "\n" +
                "SELECT id, name, age, city , description, image\n" +
                "FROM neighbro.users\n" +
                "WHERE city = (SELECT city FROM userProfile)\n" +
                "AND sex in (SELECT searchingGender FROM getSearchingGender)\n" +
                "AND searchingGender in ( 'a', (SELECT sex FROM userProfile))\n" +
                "AND id not in (SELECT anotherId FROM neighbro.users_like WHERE id = ?)\n" +
                "AND id not in (SELECT anotherId FROM neighbro.users_dislike WHERE id = ?)\n" +
                "AND id <> ?\n" +
                "AND isActive = 'y'\n" +
                "order by lastActivityDate desc \n" +
                "limit 1;";
    }

}
