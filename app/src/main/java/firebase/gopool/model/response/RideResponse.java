package firebase.gopool.model.response;

import java.io.Serializable;

public class RideResponse implements Serializable {

    private int routeOrRequestId;
    private String addressStart;
    private String addressEnd;
    private Double expectedTimeOrigin;
    private Double expectedTimeDestination;
    private String nameDriver;
    private String licensePlate;
    private String nameCar;
    private int seat;
    private String phoneDriver;
    private Double cost;
    private int groupId;
    private boolean frequent;
    private int statusId;
    private String bookingDate;

    public RideResponse() {
    }

    public RideResponse(int routeOrRequestId, String addressStart, String addressEnd,
                        Double expectedTimeOrigin, Double expectedTimeDestination,
                        String nameDriver, String licensePlate, String nameCar, int seat,
                        String phoneDriver, Double cost, int groupId, boolean frequent,
                        int status, String bookingDate) {
        this.routeOrRequestId = routeOrRequestId;
        this.addressStart = addressStart;
        this.addressEnd = addressEnd;
        this.expectedTimeOrigin = expectedTimeOrigin;
        this.expectedTimeDestination = expectedTimeDestination;
        this.nameDriver = nameDriver;
        this.licensePlate = licensePlate;
        this.nameCar = nameCar;
        this.seat = seat;
        this.phoneDriver = phoneDriver;
        this.cost = cost;
        this.groupId = groupId;
        this.frequent = frequent;
        this.statusId = status;
        this.bookingDate = bookingDate;
    }

    public int getRouteOrRequestId() {
        return routeOrRequestId;
    }

    public void setRouteOrRequestId(int routeOrRequestId) {
        this.routeOrRequestId = routeOrRequestId;
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

    public Double getExpectedTimeOrigin() {
        return expectedTimeOrigin;
    }

    public void setExpectedTimeOrigin(Double expectedTimeOrigin) {
        this.expectedTimeOrigin = expectedTimeOrigin;
    }

    public Double getExpectedTimeDestination() {
        return expectedTimeDestination;
    }

    public void setExpectedTimeDestination(Double expectedTimeDestination) {
        this.expectedTimeDestination = expectedTimeDestination;
    }

    public String getNameDriver() {
        return nameDriver;
    }

    public void setNameDriver(String nameDriver) {
        this.nameDriver = nameDriver;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getNameCar() {
        return nameCar;
    }

    public void setNameCar(String nameCar) {
        this.nameCar = nameCar;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public String getPhoneDriver() {
        return phoneDriver;
    }

    public void setPhoneDriver(String phoneDriver) {
        this.phoneDriver = phoneDriver;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public boolean isFrequent() {
        return frequent;
    }

    public void setFrequent(boolean frequent) {
        this.frequent = frequent;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }
}
