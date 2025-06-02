package lk.ijse.pahasarastudiofp.model;

import lk.ijse.pahasarastudiofp.db.DBConnection;
import lk.ijse.pahasarastudiofp.dto.PaymentDetailsDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentDetailsModel {

    public PaymentDetailsDTO getPaymentDetails(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT payment_id, appointment_id, amount FROM payment_details WHERE payment_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, id);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            return new PaymentDetailsDTO(
                    rst.getInt("payment_id"),
                    rst.getInt("appointment_id"),
                    rst.getDouble("amount")
            );
        }
        return null;
    }

    public List<PaymentDetailsDTO> getAllPaymentDetails() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT payment_id, appointment_id, amount FROM payment_details";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet rst = pstm.executeQuery();
        List<PaymentDetailsDTO> paymentDetails = new ArrayList<>();
        while (rst.next()) {
            paymentDetails.add(new PaymentDetailsDTO(
                    rst.getInt("payment_id"),
                    rst.getInt("appointment_id"),
                    rst.getDouble("amount")
            ));
        }
        return paymentDetails;
    }

    public ArrayList<PaymentDetailsDTO> getAllPayments()  throws SQLException, ClassNotFoundException{
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT payment_id, appointment_id, amount FROM payment_details";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet rst = pstm.executeQuery();
        List<PaymentDetailsDTO> paymentDetails = new ArrayList<>();
        while (rst.next()) {
            paymentDetails.add(new PaymentDetailsDTO(
                    rst.getInt("payment_id"),
                    rst.getInt("appointment_id"),
                    rst.getDouble("amount")
            ));
        }
        return (ArrayList<PaymentDetailsDTO>) (ArrayList<PaymentDetailsDTO>) paymentDetails;
    }

    public String getAppointmentName(int appointmentId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT name FROM appointment WHERE appointment_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, appointmentId);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            return rst.getString("name");
        }
        return "Unknown";
    }

    public boolean savePayment(PaymentDetailsDTO paymentDetailsDTO)  throws SQLException, ClassNotFoundException{
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO payment_details (appointment_id, amount) VALUES (?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, paymentDetailsDTO.getAppointmentId());
        pstm.setDouble(2, paymentDetailsDTO.getAmount());
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public boolean updatePayment(PaymentDetailsDTO paymentDetailsDTO)  throws SQLException, ClassNotFoundException{
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "UPDATE payment_details SET appointment_id=?, amount=? WHERE payment_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, paymentDetailsDTO.getAppointmentId());
        pstm.setDouble(2, paymentDetailsDTO.getAmount());
        pstm.setInt(3, paymentDetailsDTO.getPaymentId());
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public boolean deletePayment(int paymentId)  throws SQLException, ClassNotFoundException{
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM payment_details WHERE payment_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, paymentId);
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public PaymentDetailsDTO getPaymentDetailsByAppointmentId(int appointmentId)  throws SQLException, ClassNotFoundException{
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT payment_id, appointment_id, amount FROM payment_details WHERE appointment_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, appointmentId);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            return new PaymentDetailsDTO(
                    rst.getInt("payment_id"),
                    rst.getInt("appointment_id"),
                    rst.getDouble("amount")
            );
        }
        return null;
    }
}