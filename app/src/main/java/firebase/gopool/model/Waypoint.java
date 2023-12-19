package firebase.gopool.model;

public class Waypoint {
    private int id;

    private int onTrip;

    private Double latitude;

    private Double longitude;

    private String time;

    public Waypoint() {
    }

    public Waypoint(int id, int onTrip, Double latitude, Double longitude, String time) {
        this.id = id;
        this.onTrip = onTrip;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOnTrip() {
        return onTrip;
    }

    public void setOnTrip(int onTrip) {
        this.onTrip = onTrip;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
