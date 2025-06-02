package lk.ijse.pahasarastudiofp.model;

import lk.ijse.pahasarastudiofp.db.DBConnection;
import lk.ijse.pahasarastudiofp.dto.DiscountDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DiscountModel {

    public DiscountDTO getDiscount(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM discount WHERE discount_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, id);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            return new DiscountDTO(
                    rst.getInt("discount_id"),
                    rst.getString("discount_name"),
                    rst.getDouble("discount_percentage"),
                    rst.getDouble("discount_amount") // Added to constructor
            );
        }
        return null;
    }

    public ArrayList<DiscountDTO> getAllDiscounts() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM discount";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet rst = pstm.executeQuery();
        List<DiscountDTO> discounts = new ArrayList<>();
        while (rst.next()) {
            discounts.add(new DiscountDTO(
                    rst.getInt("discount_id"),
                    rst.getString("discount_name"),
                    rst.getDouble("discount_percentage"),
                    rst.getDouble("discount_amount") // Added to constructor
            ));
        }
        return (ArrayList<DiscountDTO>) discounts;
    }

    public boolean saveDiscount(DiscountDTO discountDTO) throws SQLException, ClassNotFoundException { // Added Exception
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO discount (discount_name, discount_percentage, discount_amount) VALUES (?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, discountDTO.getName());
        pstm.setDouble(2, discountDTO.getPercentage());
        pstm.setDouble(3, discountDTO.getAmount()); // Corrected to setDouble
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public boolean updateDiscount(DiscountDTO discountDTO) throws SQLException, ClassNotFoundException{ // Added Exception
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "UPDATE discount SET discount_name=?, discount_percentage=?, discount_amount=? WHERE discount_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, discountDTO.getName());
        pstm.setDouble(2, discountDTO.getPercentage());
        pstm.setDouble(3, discountDTO.getAmount()); // Corrected to setDouble
        pstm.setInt(4, discountDTO.getDiscountId());
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public boolean deleteDiscount(int discountId) throws SQLException, ClassNotFoundException{ // Added Exception
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM discount WHERE discount_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, discountId);
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }
}