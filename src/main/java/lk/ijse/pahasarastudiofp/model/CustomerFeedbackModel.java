package lk.ijse.pahasarastudiofp.model;

import lk.ijse.pahasarastudiofp.db.DBConnection;
import lk.ijse.pahasarastudiofp.dto.CustomerFeedbackDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerFeedbackModel {

    public boolean saveFeedback(CustomerFeedbackDTO dto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO customer_feedback (customer_id, appointment_id, rating) VALUES (?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, dto.getCustomerId());
        pstm.setInt(2, dto.getAppointmentId());
        pstm.setInt(3, dto.getRating());
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public boolean updateFeedback(CustomerFeedbackDTO dto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "UPDATE customer_feedback SET customer_id=?, appointment_id=?, rating=? WHERE feedback_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, dto.getCustomerId());
        pstm.setInt(2, dto.getAppointmentId());
        pstm.setInt(3, dto.getRating());
        pstm.setInt(4, dto.getFeedbackId());
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public boolean deleteFeedback(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM customer_feedback WHERE feedback_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, id);
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public ArrayList<CustomerFeedbackDTO> getAllFeedback() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM customer_feedback";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet rst = pstm.executeQuery();
        ArrayList<CustomerFeedbackDTO> feedbackList = new ArrayList<>();
        while (rst.next()) {
            feedbackList.add(new CustomerFeedbackDTO(
                    rst.getInt("feedback_id"),
                    rst.getInt("customer_id"),
                    rst.getInt("appointment_id"),
                    rst.getInt("rating")
            ));
        }
        return feedbackList;
    }

    public CustomerFeedbackDTO getFeedback(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM customer_feedback WHERE feedback_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, id);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            return new CustomerFeedbackDTO(
                    rst.getInt("feedback_id"),
                    rst.getInt("customer_id"),
                    rst.getInt("appointment_id"),
                    rst.getInt("rating")
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

    public String getAppointmentName(int appointmentId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT name FROM appointment WHERE appointment_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, appointmentId);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            return rst.getString("name");
        }
        return null;
    }
}