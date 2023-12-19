package firebase.gopool.model.response;

import java.io.Serializable;

public class ScheduleResponse implements Serializable {

    private Double lat;
    private Double lng;
    private String passengerName;
    private String passengerPhone;
    private int locationId;
    private Double expectedTime;
    private String expectedTimeString;

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public ScheduleResponse(Double lat, Double lng, String passengerName, String passengerPhone,
                            int locationId, Double expectedTime, String expectedTimeString) {
        this.lat = lat;
        this.lng = lng;
        this.passengerName = passengerName;
        this.passengerPhone = passengerPhone;
        this.locationId = locationId;
        this.expectedTime = expectedTime;
        this.expectedTimeString = expectedTimeString;
    }

    public String getExpectedTimeString() {
        return expectedTimeString;
    }

    public void setExpectedTimeString(String expectedTimeString) {
        this.expectedTimeString = expectedTimeString;
    }

    public Double getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(Double expectedTime) {
        this.expectedTime = expectedTime;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerPhone() {
        return passengerPhone;
    }

    public void setPassengerPhone(String passengerPhone) {
        this.passengerPhone = passengerPhone;
    }

}
