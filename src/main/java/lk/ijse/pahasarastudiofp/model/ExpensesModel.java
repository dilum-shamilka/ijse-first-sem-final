package lk.ijse.pahasarastudiofp.model;

import lk.ijse.pahasarastudiofp.db.DBConnection;
import lk.ijse.pahasarastudiofp.dto.ExpensesDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExpensesModel {

    public boolean saveExpense(ExpensesDTO dto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO expenses (employee_id, amount, description) VALUES (?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, dto.getEmployeeId());
        pstm.setDouble(2, dto.getAmount());
        pstm.setString(3, dto.getDescription());
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public boolean updateExpense(ExpensesDTO dto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "UPDATE expenses SET employee_id=?, amount=?, description=? WHERE expenses_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, dto.getEmployeeId());
        pstm.setDouble(2, dto.getAmount());
        pstm.setString(3, dto.getDescription());
        pstm.setInt(4, dto.getExpensesId());
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public boolean deleteExpense(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM expenses WHERE expenses_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, id);
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public ArrayList<ExpensesDTO> getAllExpenses() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM expenses";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet rst = pstm.executeQuery();
        ArrayList<ExpensesDTO> expenses = new ArrayList<>();
        while (rst.next()) {
            expenses.add(new ExpensesDTO(
                    rst.getInt("expenses_id"),
                    rst.getInt("employee_id"),
                    rst.getDouble("amount"),
                    rst.getString("description")
            ));
        }
        return expenses;
    }

    public ExpensesDTO getExpense(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM expenses WHERE expenses_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, id);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            return new ExpensesDTO(
                    rst.getInt("expenses_id"),
                    rst.getInt("employee_id"),
                    rst.getDouble("amount"),
                    rst.getString("description")
            );
        }
        return null;
    }

    public String getEmployeeName(int employeeId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT name FROM employee WHERE employee_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, employeeId);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            return rst.getString("name");
        }
        return null;
    }
}