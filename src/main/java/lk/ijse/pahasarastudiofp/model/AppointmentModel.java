// File: src/main/java/lk/ijse/pahasarastudiofp/model/AppointmentModel.java

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

    // Original deleteAppointment method - still exists but controller will call the transactional one
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

    /**
     * Retrieves the customer ID associated with a specific appointment.
     * @param appointmentId The ID of the appointment.
     * @return The customer ID, or -1 if the appointment is not found.
     * @throws SQLException if a database access error occurs.
     * @throws ClassNotFoundException if the JDBC driver class is not found.
     */
    public int getCustomerIdForAppointment(int appointmentId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT customer_id FROM appointment WHERE appointment_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, appointmentId);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            return rst.getInt("customer_id");
        }
        return -1; // Indicate not found
    }

    /**
     * Counts the number of appointments a specific customer has.
     * @param customerId The ID of the customer.
     * @return The count of appointments for the customer.
     * @throws SQLException if a database access error occurs.
     * @throws ClassNotFoundException if the JDBC driver class is not found.
     */
    public int getAppointmentCountForCustomer(int customerId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM appointment WHERE customer_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, customerId);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            return rst.getInt(1); // Get the count from the first column
        }
        return 0;
    }

    /**
     * Deletes an appointment and, if it was the customer's last appointment,
     * also deletes the associated customer, all within a single transaction.
     * @param appointmentId The ID of the appointment to delete.
     * @return true if the operation (and conditional customer deletion) was successful, false otherwise.
     * @throws SQLException if a database access error occurs during the transaction.
     * @throws ClassNotFoundException if the JDBC driver class is not found.
     */
    public boolean deleteAppointmentAndCustomerIfNoOtherAppointments(int appointmentId) throws SQLException, ClassNotFoundException {
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            DBConnection.getInstance().startTransaction(); // ** Start transaction **

            // 1. Get the customer_id associated with this appointment
            int customerId = getCustomerIdForAppointment(appointmentId);
            if (customerId == -1) { // Appointment not found
                DBConnection.getInstance().rollback();
                return false;
            }

            // 2. Delete the appointment
            String deleteAppointmentSql = "DELETE FROM appointment WHERE appointment_id=?";
            PreparedStatement pstmAppointment = connection.prepareStatement(deleteAppointmentSql);
            pstmAppointment.setInt(1, appointmentId);
            int affectedRowsAppointment = pstmAppointment.executeUpdate();

            if (affectedRowsAppointment == 0) {
                DBConnection.getInstance().rollback(); // Appointment not deleted, rollback
                return false;
            }

            // 3. Check if the customer has any other appointments *after* this one is deleted
            // The count reflects the state *after* the previous DELETE statement due to the active transaction.
            int remainingAppointments = getAppointmentCountForCustomer(customerId);

            if (remainingAppointments == 0) {
                // If no other appointments, proceed to delete the customer
                String deleteCustomerSql = "DELETE FROM customer WHERE customer_id=?";
                PreparedStatement pstmCustomer = connection.prepareStatement(deleteCustomerSql);
                pstmCustomer.setInt(1, customerId);
                int affectedRowsCustomer = pstmCustomer.executeUpdate();

                if (affectedRowsCustomer == 0) {
                    // This scenario should ideally not happen if remainingAppointments was 0,
                    // but it's a safeguard.
                    DBConnection.getInstance().rollback(); // Customer not deleted, rollback
                    return false;
                }
            }

            DBConnection.getInstance().commit(); // ** Commit if all successful **
            return true;

        } catch (SQLException | ClassNotFoundException e) {
            // If any error occurs, rollback the transaction
            if (connection != null) {
                DBConnection.getInstance().rollback();
            }
            throw e; // Re-throw the exception to be handled by the controller
        } finally {
            // Ensure auto-commit is reset even if an exception occurs before rollback
            if (connection != null && !connection.getAutoCommit()) {
                connection.setAutoCommit(true);
            }
        }
    }

    // Methods to get customer and package names for display (might be better placed in their respective models)
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