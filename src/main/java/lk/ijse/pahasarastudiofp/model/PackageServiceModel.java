package lk.ijse.pahasarastudiofp.model;

import lk.ijse.pahasarastudiofp.db.DBConnection;
import lk.ijse.pahasarastudiofp.dto.PackageDTO;
import lk.ijse.pahasarastudiofp.dto.PackageServiceDTO;
import lk.ijse.pahasarastudiofp.dto.ServiceDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PackageServiceModel {

    public boolean savePackageService(PackageServiceDTO dto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO package_service (package_id, service_id) VALUES (?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, dto.getPackageId());
        pstm.setInt(2, dto.getServiceId());
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public boolean deletePackageService(int packageId, int serviceId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM package_service WHERE package_id=? AND service_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, packageId);
        pstm.setInt(2, serviceId);
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public ArrayList<PackageServiceDTO> getAllPackageServices() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM package_service";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet rst = pstm.executeQuery();
        ArrayList<PackageServiceDTO> packageServices = new ArrayList<>();
        while (rst.next()) {
            packageServices.add(new PackageServiceDTO(
                    rst.getInt("package_service_id"),
                    rst.getInt("package_id"),
                    rst.getInt("service_id")
            ));
        }
        return packageServices;
    }

    public ArrayList<PackageServiceDTO> getServicesForPackage(int packageId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM package_service WHERE package_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, packageId);
        ResultSet rst = pstm.executeQuery();
        ArrayList<PackageServiceDTO> packageServices = new ArrayList<>();
        while (rst.next()) {
            packageServices.add(new PackageServiceDTO(
                    rst.getInt("package_service_id"),
                    rst.getInt("package_id"),
                    rst.getInt("service_id")
            ));
        }
        return packageServices;
    }

    public ArrayList<PackageServiceDTO> getPackagesForService(int serviceId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM package_service WHERE service_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, serviceId);
        ResultSet rst = pstm.executeQuery();
        ArrayList<PackageServiceDTO> packageServices = new ArrayList<>();
        while (rst.next()) {
            packageServices.add(new PackageServiceDTO(
                    rst.getInt("package_service_id"),
                    rst.getInt("package_id"),
                    rst.getInt("service_id")
            ));
        }
        return packageServices;
    }

}