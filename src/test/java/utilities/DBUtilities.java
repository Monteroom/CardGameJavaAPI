package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtilities {

    private static Connection connection;
    private static final Logger logger = LogManager.getLogger(DBUtilities.class);

    public static Connection connect() throws SQLException {
        logger.info("checking for JDBC connection");
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                    Config.getValue("dbUrl"),
                    Config.getValue("dbUsername"),
                    Config.getValue("dbPassword"));
            logger.info("created new JDBC connection for: " + Config.getValue("dbUrl"));
        }
        return connection;
    }
    public static void closeConnection() throws SQLException{
        if (connection != null && !connection.isClosed()){
            connection.close();
            logger.info("closed JDBC connection");
        }
    }

    public static ResultSet sendQuery(String query) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        return preparedStatement.executeQuery();
    }

    public static ResultSet sendQuery(String query, Object [] parameters) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        for (int i = 0; i < parameters.length; i++){
            preparedStatement.setObject(i + 1, parameters[i]);
        }

        return preparedStatement.executeQuery();
    }

    /*
    this method retrieves all rows from one column and put them in arraylist
     */
    public static List<Object> retrieveResultsToList(ResultSet resultSet, String columnName) throws SQLException{

        List<Object> result = new ArrayList<>();

        while(resultSet.next()){
            result.add(resultSet.getObject(columnName));
        }

        return  result;
    }

    /*
        this method retrieves all rows from one column and put them in arraylist
    */
    public static List<Object> retrieveResultsToList(ResultSet resultSet, int columnIndex) throws SQLException{

        List<Object> result = new ArrayList<>();

        while(resultSet.next()){
            result.add(resultSet.getObject(columnIndex));
        }
        return  result;
    }


    public static List<Map<Object, Object>> retrieveResultsToListOfMaps
            (ResultSet resultSet, String columnName1, String columnName2) throws SQLException{

        List<Map<Object, Object>> results = new ArrayList<>();

        while(resultSet.next()){
            Map<Object, Object> map = new HashMap<>();
            map.put(resultSet.getObject(columnName1), resultSet.getObject(columnName2));
            results.add(map);
        }

        return results;
    }

    public static Map<String, String> retrieveResultsToMap
            (ResultSet resultSet, String columnName1, String columnName2) throws SQLException{
        Map<String, String> map = new HashMap<>();

        while(resultSet.next()){
            map.put(resultSet.getString(columnName1), resultSet.getString(columnName2));
        }

        return map;
    }

}
