package lk.ijse.pahasarastudiofp.model;

import lk.ijse.pahasarastudiofp.db.DBConnection;
import lk.ijse.pahasarastudiofp.dto.ServiceDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceModel {

    //  Get Service IDs
    public static List<String> getServiceIds() throws SQLException, ClassNotFoundException {
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<String> ids = new ArrayList<>();
        try {
            connection = DBConnection.getInstance().getConnection();
            String sql = "SELECT service_id FROM service";
            pstm = connection.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                ids.add(rs.getString("service_id"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception to be handled by the caller
        } finally {
            closeResources(connection, pstm, rs);
        }
        return ids;
    }

    //  Get Service Name
    public static String getServiceName(String serviceId) throws SQLException, ClassNotFoundException {
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String serviceName = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            String sql = "SELECT name FROM service WHERE service_id = ?";
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, serviceId);
            rs = pstm.executeQuery();
            if (rs.next()) {
                serviceName = rs.getString("name");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        } finally {
            closeResources(connection, pstm, rs);
        }
        return serviceName;
    }

    //  Save Service
    public boolean saveService(ServiceDTO dto) throws SQLException, ClassNotFoundException {
        Connection connection = null;
        PreparedStatement pstm = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            String sql = "INSERT INTO service (package_id, name, price) VALUES (?, ?, ?)";
            pstm = connection.prepareStatement(sql);
            pstm.setInt(1, dto.getPackageId());
            pstm.setString(2, dto.getName());
            pstm.setDouble(3, dto.getPrice());
            int affectedRows = pstm.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        } finally {
            closeResources(connection, pstm, null);
        }
    }

    // Update Service
    public boolean updateService(ServiceDTO dto) throws SQLException, ClassNotFoundException {
        Connection connection = null;
        PreparedStatement pstm = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            String sql = "UPDATE service SET package_id=?, name=?, price=? WHERE service_id=?";
            pstm = connection.prepareStatement(sql);
            pstm.setInt(1, dto.getPackageId());
            pstm.setString(2, dto.getName());
            pstm.setDouble(3, dto.getPrice());
            pstm.setInt(4, dto.getServiceId());
            int affectedRows = pstm.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        } finally {
            closeResources(connection, pstm, null);
        }
    }

    //  Delete Service
    public boolean deleteService(int id) throws SQLException, ClassNotFoundException {
        Connection connection = null;
        PreparedStatement pstm = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            String sql = "DELETE FROM service WHERE service_id=?";
            pstm = connection.prepareStatement(sql);
            pstm.setInt(1, id);
            int affectedRows = pstm.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        } finally {
            closeResources(connection, pstm, null);
        }
    }

    //  Get All Services
    public ArrayList<ServiceDTO> getAllServices() throws SQLException, ClassNotFoundException {
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet rst = null;
        List<ServiceDTO> services = new ArrayList<>();
        try {
            connection = DBConnection.getInstance().getConnection();
            String sql = "SELECT * FROM service";
            pstm = connection.prepareStatement(sql);
            rst = pstm.executeQuery();
            while (rst.next()) {
                services.add(new ServiceDTO(
                        rst.getInt("service_id"),
                        rst.getInt("package_id"),
                        rst.getString("name"),
                        rst.getDouble("price")
                ));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        } finally {
            closeResources(connection, pstm, rst);
        }
        return (ArrayList<ServiceDTO>) services;
    }

    //  Get Service
    public ServiceDTO getService(int id) throws SQLException, ClassNotFoundException {
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet rst = null;
        ServiceDTO serviceDTO = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            String sql = "SELECT * FROM service WHERE service_id=?";
            pstm = connection.prepareStatement(sql);
            pstm.setInt(1, id);
            rst = pstm.executeQuery();
            if (rst.next()) {
                serviceDTO = new ServiceDTO(
                        rst.getInt("service_id"),
                        rst.getInt("package_id"),
                        rst.getString("name"),
                        rst.getDouble("price")
                );
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        } finally {
            closeResources(connection, pstm, rst);
        }
        return serviceDTO;
    }

    //  Helper method to close resources
    private static void closeResources(Connection connection, PreparedStatement pstm, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pstm != null) {
                pstm.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

