
package lk.ijse.pahasarastudiofp.model;

import lk.ijse.pahasarastudiofp.db.DBConnection;
import lk.ijse.pahasarastudiofp.dto.AppointmentDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AppointmentModel {

    public boolean saveAppointment(AppointmentDTO dto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO appointment (customer_id, package_id, name) VALUES (?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, dto.getCustomerId());
        pstm.setInt(2, dto.getPackageId());
        pstm.setString(3, dto.getName());
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public boolean updateAppointment(AppointmentDTO dto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "UPDATE appointment SET customer_id=?, package_id=?, name=? WHERE appointment_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, dto.getCustomerId());
        pstm.setInt(2, dto.getPackageId());
        pstm.setString(3, dto.getName());
        pstm.setInt(4, dto.getAppointmentId());
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public boolean deleteAppointment(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM appointment WHERE appointment_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, id);
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public ArrayList<AppointmentDTO> getAllAppointments() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM appointment";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet rst = pstm.executeQuery();
        ArrayList<AppointmentDTO> appointments = new ArrayList<>();
        while (rst.next()) {
            appointments.add(new AppointmentDTO(
                    rst.getInt("appointment_id"),
                    rst.getInt("customer_id"),
                    rst.getInt("package_id"),
                    rst.getString("name")
            ));
        }
        return appointments;
    }

    public AppointmentDTO getAppointment(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM appointment WHERE appointment_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, id);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            return new AppointmentDTO(
                    rst.getInt("appointment_id"),
                    rst.getInt("customer_id"),
                    rst.getInt("package_id"),
                    rst.getString("name")
            );
        }
        return null;
    }

    public int getCustomerIdForAppointment(int appointmentId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT customer_id FROM appointment WHERE appointment_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, appointmentId);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            return rst.getInt("customer_id");
        }
        return -1;
    }

    public int getAppointmentCountForCustomer(int customerId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM appointment WHERE customer_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, customerId);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            return rst.getInt(1);
        }
        return 0;
    }


    public boolean deleteAppointmentAndCustomerIfNoOtherAppointments(int appointmentId) throws SQLException, ClassNotFoundException {
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            DBConnection.getInstance().startTransaction(); // ** Start transaction **


            int customerId = getCustomerIdForAppointment(appointmentId);
            if (customerId == -1) {
                DBConnection.getInstance().rollback();
                return false;
            }


            String deleteAppointmentSql = "DELETE FROM appointment WHERE appointment_id=?";
            PreparedStatement pstmAppointment = connection.prepareStatement(deleteAppointmentSql);
            pstmAppointment.setInt(1, appointmentId);
            int affectedRowsAppointment = pstmAppointment.executeUpdate();

            if (affectedRowsAppointment == 0) {
                DBConnection.getInstance().rollback();
                return false;
            }

            int remainingAppointments = getAppointmentCountForCustomer(customerId);

            if (remainingAppointments == 0) {

                String deleteCustomerSql = "DELETE FROM customer WHERE customer_id=?";
                PreparedStatement pstmCustomer = connection.prepareStatement(deleteCustomerSql);
                pstmCustomer.setInt(1, customerId);
                int affectedRowsCustomer = pstmCustomer.executeUpdate();

                if (affectedRowsCustomer == 0) {

                    DBConnection.getInstance().rollback();
                    return false;
                }
            }

            DBConnection.getInstance().commit();
            return true;

        } catch (SQLException | ClassNotFoundException e) {

            if (connection != null) {
                DBConnection.getInstance().rollback();
            }
            throw e;
        } finally {

            if (connection != null && !connection.getAutoCommit()) {
                connection.setAutoCommit(true);
            }
        }
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

    public String getPackageName(int packageId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT name FROM package WHERE package_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, packageId);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            return rst.getString("name");
        }
        return null;
    }
}