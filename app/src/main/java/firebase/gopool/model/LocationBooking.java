package firebase.gopool.model;

import android.content.res.Resources;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import java.io.Serializable;

import firebase.gopool.R;

public class LocationBooking implements Serializable {

    private Double latOrigin;
    private Double lngOrigin;
    private Double latDestination;
    private Double lngDestination;

    public LocationBooking(Double latOrigin, Double lngOrigin, Double latDestination, Double lngDestination) {
        this.latOrigin = latOrigin;
        this.lngOrigin = lngOrigin;
        this.latDestination = latDestination;
        this.lngDestination = lngDestination;
    }

    // Khoảng cách euclid
//    public double distance() {
//        float[] results = new float[1];
//        Location.distanceBetween(latOrigin, lngOrigin, latDestination, lngDestination, results);
//        return results[0];
//    }

    public double distance(String origin, String destination, Resources resources) {
        try {
            String google_api = resources.getString(R.string.google_maps_api_key);

            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(google_api)
                    .build();

            DirectionsResult directionsResult = DirectionsApi.newRequest(context)
                    .mode(TravelMode.DRIVING)
                    .origin(origin)
                    .destination(destination)
                    .await();

            // Lấy khoảng cách từ kết quả
            return directionsResult.routes[0].legs[0].distance.inMeters / 1000.0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Trả về giá trị âm để chỉ ra lỗi
        }
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
}
