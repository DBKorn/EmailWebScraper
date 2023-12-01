import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLDBConnection {
    private final String arr = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String connectionUrl = // specifies how to connect to the database
            "jdbc:sqlserver://mco364.ckxf3a0k0vuw.us-east-1.rds.amazonaws.com;"
                    + "database=DovidKorn;"
                    + "user=admin364;"
                    + "password=mco364lcm;"
                    + "encrypt=false;"
                    + "trustServerCertificate=false;"
                    + "loginTimeout=30;";


    public void qry() throws ClassNotFoundException {qry("SELECT  * FROM Emails");}
    public void qry(String query) throws ClassNotFoundException {

        ResultSet resultSet = null;
        ///*
        try (Connection connection = DriverManager.getConnection(connectionUrl); // AutoCloseable
             Statement statement = connection.createStatement())
        {
            // Create and execute a SELECT SQL statement.
            String selectSql = query;
            resultSet = statement.executeQuery(query);

            // Print results from select statement
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1) + " " + resultSet.getString(2));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
/*
    public void insertToDB(String emailAddress) throws ClassNotFoundException {
        ResultSet resultSet = null;
        String insertSql2 = "INSERT INTO Emails (Address) VALUES (?);";
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql2, Statement.RETURN_GENERATED_KEYS);) {
            {
                prepsInsertProduct.setString(1,emailAddress);
                prepsInsertProduct.execute();
                resultSet = prepsInsertProduct.getGeneratedKeys();
                //while (resultSet.next()) {
                //    System.out.println(resultSet.getInt(1));
                //}
            }} catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
*/

    public void insertToDB(String email) throws ClassNotFoundException {
        ResultSet resultSet = null;

        String insertSql2 = "INSERT INTO Emails (Address) VALUES (?);";
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql2, Statement.RETURN_GENERATED_KEYS))
        {
                prepsInsertProduct.setString(1, email);
                prepsInsertProduct.addBatch();
                //prepsInsertProduct.setString(1,email);
                //prepsInsertProduct.execute();

            prepsInsertProduct.executeBatch();
            //resultSet = prepsInsertProduct.getGeneratedKeys();
            //while (resultSet.next()) {
            //    System.out.println(resultSet.getInt(1));
            //}
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void insertSetToDB(Set<String> group){
        String insertSql2 = "INSERT INTO Emails (Address) VALUES (?);";
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql2, Statement.RETURN_GENERATED_KEYS))
        {
            for (String email : group) {
                prepsInsertProduct.setString(1, email);
                prepsInsertProduct.addBatch();
            }
            //prepsInsertProduct.setString(1, email);
            //prepsInsertProduct.addBatch();
            //prepsInsertProduct.setString(1,email);
            //prepsInsertProduct.execute();

            prepsInsertProduct.executeBatch();
            //resultSet = prepsInsertProduct.getGeneratedKeys();
            //while (resultSet.next()) {
            //    System.out.println(resultSet.getInt(1));
            //}
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


}
