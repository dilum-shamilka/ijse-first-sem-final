package lk.ijse.pahasarastudiofp.model;

import lk.ijse.pahasarastudiofp.db.DBConnection;
import lk.ijse.pahasarastudiofp.dto.EmployeeDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeModel {

    // This method needs to be implemented to fetch all employee IDs from the database.
    public static List<String> getEmployeeIds() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT employee_id FROM employee";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet rst = pstm.executeQuery();
        List<String> employeeIds = new ArrayList<>();
        while (rst.next()) {
            employeeIds.add(String.valueOf(rst.getInt("employee_id")));
        }
        return employeeIds;
    }

    // This method needs to be implemented to fetch an employee's name by their ID.
    public static String getEmployeeName(String employeeId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT name FROM employee WHERE employee_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, Integer.parseInt(employeeId));
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            return rst.getString("name");
        }
        return null;
    }


    public boolean saveEmployee(EmployeeDTO dto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO employee (name, description) VALUES (?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, dto.getName());
        pstm.setString(2, dto.getDescription());
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public boolean updateEmployee(EmployeeDTO dto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "UPDATE employee SET name=?, description=? WHERE employee_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, dto.getName());
        pstm.setString(2, dto.getDescription());
        pstm.setInt(3, dto.getEmployeeId());
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public boolean deleteEmployee(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM employee WHERE employee_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, id);
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public ArrayList<EmployeeDTO> getAllEmployees() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM employee";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet rst = pstm.executeQuery();
        ArrayList<EmployeeDTO> employees = new ArrayList<>();
        while (rst.next()) {
            employees.add(new EmployeeDTO(
                    rst.getInt("employee_id"),
                    rst.getString("name"),
                    rst.getString("description")
            ));
        }
        return employees;
    }

    public EmployeeDTO getEmployee(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM employee WHERE employee_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, id);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            return new EmployeeDTO(
                    rst.getInt("employee_id"),
                    rst.getString("name"),
                    rst.getString("description")
            );
        }
        return null;
    }
}