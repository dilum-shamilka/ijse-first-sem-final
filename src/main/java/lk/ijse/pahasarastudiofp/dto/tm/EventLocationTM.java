package lk.ijse.pahasarastudiofp.dto.tm;

public class EventLocationTM {
    private int eventId;
    private String address;
    private String locationType;
    private String venueType;

    public EventLocationTM() {
    }

    public EventLocationTM(int eventId, String address, String locationType, String venueType) {
        this.eventId = eventId;
        this.address = address;
        this.locationType = locationType;
        this.venueType = venueType;
    }

    // Getters and Setters
    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getVenueType() {
        return venueType;
    }

    public void setVenueType(String venueType) {
        this.venueType = venueType;
    }

    @Override
    public String toString() {
        return "EventLocationTM{" +
                "eventId=" + eventId +
                ", address='" + address + '\'' +
                ", locationType='" + locationType + '\'' +
                ", venueType='" + venueType + '\'' +
                '}';
    }
}