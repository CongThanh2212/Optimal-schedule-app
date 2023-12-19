package firebase.gopool.model.request;

import java.io.Serializable;

public class BookOnlineRequest implements Serializable {

    private Double pickUpTime;
    private Integer capacity;
    private Double latOrigin;
    private Double lngOrigin;
    private Double latDestination;
    private Double lngDestination;
    private String addressStart;
    private String addressEnd;
    private Double length;

    public BookOnlineRequest(Double pickUpTime, Integer capacity, Double latOrigin, Double lngOrigin,
                             Double latDestination, Double lngDestination, String addressStart, String addressEnd,
                             Double length) {
        this.pickUpTime = pickUpTime;
        this.capacity = capacity;
        this.latOrigin = latOrigin;
        this.lngOrigin = lngOrigin;
        this.latDestination = latDestination;
        this.lngDestination = lngDestination;
        this.addressStart = addressStart;
        this.addressEnd = addressEnd;
        this.length = length;
    }

    public Double getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(Double pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Double getLatOrigin() {
        return latOrigin;
    }

    public void setLatOrigin(Double latOrigin) {
        this.latOrigin = latOrigin;
    }

    public Double getLngOrigin() {
        return lngOrigin;
    }

    public void setLngOrigin(Double lngOrigin) {
        this.lngOrigin = lngOrigin;
    }

    public Double getLatDestination() {
        return latDestination;
    }

    public void setLatDestination(Double latDestination) {
        this.latDestination = latDestination;
    }

    public Double getLngDestination() {
        return lngDestination;
    }

    public void setLngDestination(Double lngDestination) {
        this.lngDestination = lngDestination;
    }

    public String getAddressStart() {
        return addressStart;
    }

    public void setAddressStart(String addressStart) {
        this.addressStart = addressStart;
    }

    public String getAddressEnd() {
        return addressEnd;
    }

    public void setAddressEnd(String addressEnd) {
        this.addressEnd = addressEnd;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }
}
