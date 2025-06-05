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

    public static List<Integer> getServiceIds() throws SQLException, ClassNotFoundException {

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = connection.prepareStatement("SELECT service_id FROM service");
             ResultSet rs = pstm.executeQuery()) {

            List<Integer> ids = new ArrayList<>();
            while (rs.next()) {
                ids.add(rs.getInt("service_id"));
            }
            return ids;
        }
    }

    public static String getServiceName(String serviceId) throws SQLException, ClassNotFoundException {

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = connection.prepareStatement("SELECT name FROM service WHERE service_id = ?")) {

            pstm.setInt(1, Integer.parseInt(serviceId));
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        }
        return null;
    }

    public boolean saveService(ServiceDTO dto) throws SQLException, ClassNotFoundException {

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = connection.prepareStatement("INSERT INTO service (package_id, name, price) VALUES (?, ?, ?)")) {

            pstm.setInt(1, dto.getPackageId());
            pstm.setString(2, dto.getName());
            pstm.setDouble(3, dto.getPrice());
            int affectedRows = pstm.executeUpdate();
            return affectedRows > 0;
        }
    }

    public boolean updateService(ServiceDTO dto) throws SQLException, ClassNotFoundException {

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = connection.prepareStatement("UPDATE service SET package_id=?, name=?, price=? WHERE service_id=?")) {

            pstm.setInt(1, dto.getPackageId());
            pstm.setString(2, dto.getName());
            pstm.setDouble(3, dto.getPrice());
            pstm.setInt(4, dto.getServiceId());
            int affectedRows = pstm.executeUpdate();
            return affectedRows > 0;
        }
    }

    public boolean deleteService(int id) throws SQLException, ClassNotFoundException {

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = connection.prepareStatement("DELETE FROM service WHERE service_id=?")) {

            pstm.setInt(1, id);
            int affectedRows = pstm.executeUpdate();
            return affectedRows > 0;
        }
    }

    public ArrayList<ServiceDTO> getAllServices() throws SQLException, ClassNotFoundException {

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = connection.prepareStatement("SELECT * FROM service");
             ResultSet rst = pstm.executeQuery()) {

            ArrayList<ServiceDTO> services = new ArrayList<>();
            while (rst.next()) {
                services.add(new ServiceDTO(
                        rst.getInt("service_id"),
                        rst.getInt("package_id"),
                        rst.getString("name"),
                        rst.getDouble("price")
                ));
            }
            return services;
        }
    }

    public ServiceDTO getService(int id) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = connection.prepareStatement("SELECT * FROM service WHERE service_id=?")) {

            pstm.setInt(1, id);
            try (ResultSet rst = pstm.executeQuery()) {
                if (rst.next()) {
                    return new ServiceDTO(
                            rst.getInt("service_id"),
                            rst.getInt("package_id"),
                            rst.getString("name"),
                            rst.getDouble("price")
                    );
                }
            }
        }
        return null;
    }


}