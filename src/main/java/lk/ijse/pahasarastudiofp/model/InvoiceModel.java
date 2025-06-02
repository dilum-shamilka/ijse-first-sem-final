package lk.ijse.pahasarastudiofp.model;

import lk.ijse.pahasarastudiofp.db.DBConnection;
import lk.ijse.pahasarastudiofp.dto.InvoiceDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceModel {

    public boolean saveInvoice(InvoiceDTO dto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO invoice (appointment_id, date, amount, report_id) VALUES (?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, dto.getAppointmentId());
        pstm.setDate(2, dto.getDate());
        pstm.setDouble(3, dto.getAmount());
        pstm.setObject(4, dto.getReportId()); // Use setobject
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public boolean updateInvoice(InvoiceDTO dto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "UPDATE invoice SET appointment_id=?, date=?, amount=?, report_id=? WHERE invoice_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, dto.getAppointmentId());
        pstm.setDate(2, dto.getDate());
        pstm.setDouble(3, dto.getAmount());
        pstm.setObject(4, dto.getReportId()); // Use setObject
        pstm.setInt(5, dto.getInvoiceId());
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public boolean deleteInvoice(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM invoice WHERE invoice_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, id);
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public ArrayList<InvoiceDTO> getAllInvoices() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM invoice";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet rst = pstm.executeQuery();
        List<InvoiceDTO> invoices = new ArrayList<>();
        while (rst.next()) {
            InvoiceDTO dto = new InvoiceDTO();
            dto.setInvoiceId(rst.getInt("invoice_id"));
            dto.setAppointmentId(rst.getInt("appointment_id"));
            dto.setDate(rst.getDate("date"));
            dto.setAmount(rst.getDouble("amount"));
            dto.setReportId(rst.getObject("report_id", Integer.class)); // Use getObject
            invoices.add(dto);
        }
        return (ArrayList<InvoiceDTO>) invoices;
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

    public ArrayList<Integer> getAppointmentIds() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT appointment_id FROM appointment";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet rst = pstm.executeQuery();
        List<Integer> ids = new ArrayList<>();
        while (rst.next()) {
            ids.add(rst.getInt("appointment_id"));
        }
        return (ArrayList<Integer>) (ArrayList<Integer>) ids;
    }
}