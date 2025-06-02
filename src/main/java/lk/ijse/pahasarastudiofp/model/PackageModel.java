package lk.ijse.pahasarastudiofp.model;

import lk.ijse.pahasarastudiofp.db.DBConnection;
import lk.ijse.pahasarastudiofp.dto.PackageDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PackageModel {

    private static String packageId;

    public static List<PackageDTO> getAll() {
        return List.of();
    }

    public static PackageDTO getPackageById(String packageId) {
        PackageModel.packageId = packageId;
        return null;
    }

    public boolean savePackage(PackageDTO dto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO package (name, description) VALUES (?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, dto.getName());
        pstm.setString(2, dto.getDescription());
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public boolean updatePackage(PackageDTO dto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "UPDATE package SET name=?, description=? WHERE package_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, dto.getName());
        pstm.setString(2, dto.getDescription());
        pstm.setInt(3, dto.getPackageId());
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public boolean deletePackage(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM package WHERE package_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, id);
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public ArrayList<PackageDTO> getAllPackages() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM package";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet rst = pstm.executeQuery();
        ArrayList<PackageDTO> packages = new ArrayList<>();
        while (rst.next()) {
            packages.add(new PackageDTO(
                    rst.getInt("package_id"),
                    rst.getString("name"),
                    rst.getString("description")
            ));
        }
        return packages;
    }

    public PackageDTO getPackage(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM package WHERE package_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, id);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            return new PackageDTO(
                    rst.getInt("package_id"),
                    rst.getString("name"),
                    rst.getString("description")
            );
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