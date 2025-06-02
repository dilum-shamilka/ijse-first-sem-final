package lk.ijse.pahasarastudiofp.model;

import lk.ijse.pahasarastudiofp.db.DBConnection;
import lk.ijse.pahasarastudiofp.dto.EventLocationDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventLocationModel {

    public boolean saveLocation(EventLocationDTO dto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO event_location (address, location_type, venue_type) VALUES (?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, dto.getAddress());
        pstm.setString(2, dto.getLocationType());
        pstm.setString(3, dto.getVenueType());
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public boolean updateLocation(EventLocationDTO dto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "UPDATE event_location SET address=?, location_type=?, venue_type=? WHERE event_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, dto.getAddress());
        pstm.setString(2, dto.getLocationType());
        pstm.setString(3, dto.getVenueType());
        pstm.setInt(4, dto.getEventId());
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public boolean deleteLocation(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM event_location WHERE event_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, id);
        int affectedRows = pstm.executeUpdate();
        return affectedRows > 0;
    }

    public ArrayList<EventLocationDTO> getAllLocations() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM event_location";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet rst = pstm.executeQuery();
        ArrayList<EventLocationDTO> locations = new ArrayList<>();
        while (rst.next()) {
            locations.add(new EventLocationDTO(
                    rst.getInt("event_id"),
                    rst.getString("address"),
                    rst.getString("location_type"),
                    rst.getString("venue_type")
            ));
        }
        return locations;
    }

    public EventLocationDTO getLocation(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM event_location WHERE event_id=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, id);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            return new EventLocationDTO(
                    rst.getInt("event_id"),
                    rst.getString("address"),
                    rst.getString("location_type"),
                    rst.getString("venue_type")
            );
        }
        return null;
    }

    public List<Object> getAllEventLocations() {
        return List.of();
    }

    public Object getEventLocation(int eventId) {
        return null;
    }
}