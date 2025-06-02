// File: src/main/java/lk/ijse/pahasarastudiofp/model/CustomerModel.java

package lk.ijse.pahasarastudiofp.model;

import lk.ijse.pahasarastudiofp.db.DBConnection;
import lk.ijse.pahasarastudiofp.dto.CustomerDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerModel {

    public boolean saveCustomer(CustomerDTO dto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO customer (name, email, contact, address) VALUES (?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, dto.getName());
        pstm.setString(2, dto.getEmail());
        pstm.setString(3, dto.getContact());
        pstm.setString(4, dto.getAddress());
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public boolean updateCustomer(CustomerDTO dto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "UPDATE customer SET name=?, email=?, contact=?, address=? WHERE customer_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, dto.getName());
        pstm.setString(2, dto.getEmail());
        pstm.setString(3, dto.getContact());
        pstm.setString(4, dto.getAddress());
        pstm.setInt(5, dto.getCustomerId());
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public boolean deleteCustomer(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM customer WHERE customer_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, id);
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public ArrayList<CustomerDTO> getAllCustomers() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM customer";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet rst = pstm.executeQuery();
        ArrayList<CustomerDTO> customers = new ArrayList<>();
        while (rst.next()) {
            customers.add(new CustomerDTO(
                    rst.getInt("customer_id"),
                    rst.getString("name"),
                    rst.getString("email"),
                    rst.getString("contact"),
                    rst.getString("address")
            ));
        }
        return customers;
    }

    public CustomerDTO getCustomer(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM customer WHERE customer_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, id);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            return new CustomerDTO(
                    rst.getInt("customer_id"),
                    rst.getString("name"),
                    rst.getString("email"),
                    rst.getString("contact"),
                    rst.getString("address")
            );
        }
        return null;
    }

    public String getCustomerName(int customerId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT name FROM customer WHERE customer_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, customerId);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            return rst.getString("name");
        }
        return null;
    }
}