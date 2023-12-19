package firebase.gopool.model.response;

import java.io.Serializable;

public class FrequentResponse implements Serializable {

    private int id;
    private String accountId;
    private String frequentRouteId;
    private String addressStart;
    private String addressEnd;
    private double latStart;
    private double lngStart;
    private double latEnd;
    private double lngEnd;
    private String timeStart;
    private String timeEnd;
    private boolean shared;
    private double lengthRoute;
    private int weekday;
    private int groupId;
    private double cost;

    public FrequentResponse(int id, String accountId, String frequentRouteId, String addressStart,
                            String addressEnd, double latStart, double lngStart, double latEnd,
                            double lngEnd, String timeStart, String timeEnd, boolean shared,
                            double lengthRoute, int weekday, int groupId, double cost) {
        this.id = id;
        this.accountId = accountId;
        this.frequentRouteId = frequentRouteId;
        this.addressStart = addressStart;
        this.addressEnd = addressEnd;
        this.latStart = latStart;
        this.lngStart = lngStart;
        this.latEnd = latEnd;
        this.lngEnd = lngEnd;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.shared = shared;
        this.lengthRoute = lengthRoute;
        this.weekday = weekday;
        this.groupId = groupId;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getFrequentRouteId() {
        return frequentRouteId;
    }

    public void setFrequentRouteId(String frequentRouteId) {
        this.frequentRouteId = frequentRouteId;
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

    public double getLatStart() {
        return latStart;
    }

    public void setLatStart(double latStart) {
        this.latStart = latStart;
    }

    public double getLngStart() {
        return lngStart;
    }

    public void setLngStart(double lngStart) {
        this.lngStart = lngStart;
    }

    public double getLatEnd() {
        return latEnd;
    }

    public void setLatEnd(double latEnd) {
        this.latEnd = latEnd;
    }

    public double getLngEnd() {
        return lngEnd;
    }

    public void setLngEnd(double lngEnd) {
        this.lngEnd = lngEnd;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public double getLengthRoute() {
        return lengthRoute;
    }

    public void setLengthRoute(double lengthRoute) {
        this.lengthRoute = lengthRoute;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

}
