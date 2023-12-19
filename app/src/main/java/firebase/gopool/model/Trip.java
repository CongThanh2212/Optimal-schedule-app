package firebase.gopool.model;

public class Trip {
    private int tripId;

    private String accountOwner;
    private String  date;

    private int weekday;

    public Trip() {
    }

    public Trip(int tripId, String accountOwner, String date, int weekday) {
        this.tripId = tripId;
        this.accountOwner = accountOwner;
        this.date = date;
        this.weekday = weekday;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(String accountOwner) {
        this.accountOwner = accountOwner;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }
}
