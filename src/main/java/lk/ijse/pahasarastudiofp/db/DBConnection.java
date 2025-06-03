

package lk.ijse.pahasarastudiofp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbConnection;
    private Connection connection;


    private DBConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/myproject", "root", "dilum123");
    }


    public static DBConnection getInstance() throws SQLException, ClassNotFoundException {
        // If no instance exists, create one. Otherwise, return the existing one.
        if (dbConnection == null) {
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }


    public Connection getConnection() throws SQLException, ClassNotFoundException {
        if (connection == null || connection.isClosed()) {

            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/myproject", "root", "dilum123");
        }
        return connection;
    }


    public void startTransaction() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.setAutoCommit(false);
        } else {
            throw new SQLException("Cannot start transaction: Database connection is not open or valid.");
        }
    }


    public void commit() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.commit();
            connection.setAutoCommit(true);
        } else {
            throw new SQLException("Cannot commit transaction: Database connection is not open or valid.");
        }
    }

    public void rollback() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.rollback();
            connection.setAutoCommit(true);
        } else {
            throw new SQLException("Cannot rollback transaction: Database connection is not open or valid.");
        }
    }


    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}