package firebase.gopool;

import static firebase.gopool.MapsActivity.hideKeyboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import firebase.gopool.MapDirectionHelper.FetchURL;
import firebase.gopool.MapDirectionHelper.TaskLoadedCallback;
import firebase.gopool.api.API;
import firebase.gopool.model.response.ScheduleResponse;
import firebase.gopool.navigation.BottomNavigationViewHelperDriver;
import firebase.gopool.saveLogin.SaveLogin;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripNowActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {
    private static final String TAG = "TripNowActivity";
    private static final int ACTIVITY_NUMBER = 0;

    private Context mContext = TripNowActivity.this;

    private GoogleMap mMap;
    private static final float DEFAULT_ZOOM = 15f;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Marker markerCurrent;

    private SaveLogin saveLogin;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_now);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_trip_now);
        mapFragment.getMapAsync(this);

        mapping();
        setupBottomNavigationView();
    }

    private void mapping() {
        saveLogin = new SaveLogin(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        initialize();
    }

    private void initialize() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            final Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Log.d(TAG, "onComplete: getting found location!");
                        Location result = (Location) task.getResult();
                        mMap.clear();

                        moveCamera(new LatLng(result.getLatitude(), result.getLongitude()),
                                DEFAULT_ZOOM,
                                "My location");
                        createMarkerCurrent(new LatLng(result.getLatitude(), result.getLongitude()));
                        initializeSchedule();
                    } else {
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(TripNowActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void initializeSchedule() {
        API.api.scheduleOfDriver(saveLogin.getHeaderJwt()).enqueue(new Callback<ArrayList<ScheduleResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<ScheduleResponse>> call, Response<ArrayList<ScheduleResponse>> response) {
                if (response.code() == 200) {
                    ArrayList<ScheduleResponse> schedules = response.body();
                    Marker markerStart = markerCurrent;
                    for (ScheduleResponse schedule : schedules) {
                        Marker markerEnd;
                        // origin
                        if (schedule.getLocationId() == 1) markerEnd = createMarkerStart(schedule);
                        // destination
                        else markerEnd = createMarkerEnd(schedule);

                        new FetchURL(TripNowActivity.this, mMap).execute(getUrl(markerStart.getPosition(), markerEnd.getPosition(), "driving"), "driving");
                        markerStart = markerEnd;
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ScheduleResponse>> call, Throwable t) {

            }
        });
    }

    private void createMarkerCurrent(LatLng startPoint) {
        // Load ảnh từ tài nguyên trong project
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_car_point);

        // Tạo BitmapDescriptor từ ảnh
        BitmapDescriptor imageBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(imageBitmap);

        MarkerOptions marker = new MarkerOptions()
                .position(startPoint)
                .icon(BitmapDescriptorFactory.fromBitmap(imageBitmap));

        markerCurrent = mMap.addMarker(marker);
    }

    private Marker createMarkerStart(ScheduleResponse schedule) {
        String snippet = schedule.getPassengerPhone() + " Time:" + schedule.getExpectedTimeString();
        LatLng startPoint = new LatLng(schedule.getLat(), schedule.getLng());
        MarkerOptions marker = new MarkerOptions()
                .position(startPoint)
                .title("Origin")
                .snippet(snippet);

        Marker addMarker = mMap.addMarker(marker);
        addMarker.showInfoWindow();
        return addMarker;
    }

    private Marker createMarkerEnd(ScheduleResponse schedule) {
        String[] arrName = schedule.getPassengerName().split(" ");
        String snippet = arrName[arrName.length - 1] + " Time:" + schedule.getExpectedTimeString();
        LatLng endPoint = new LatLng(schedule.getLat(), schedule.getLng());
        MarkerOptions marker = new MarkerOptions()
                .position(endPoint)
                .title("Destination")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .snippet(snippet);

        Marker addMarker = mMap.addMarker(marker);
        addMarker.showInfoWindow();
        return addMarker;
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to: lat:" + latLng.latitude + ", lng: " + latLng.longitude);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        hideKeyboard(TripNowActivity.this);
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String google_api = getResources().getString(R.string.google_maps_api_key);
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + google_api;
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {

    }

    /***
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBarDriver);
        BottomNavigationViewHelperDriver.enableNavigation(mContext, bottomNavigationView);

        //Change current highlighted icon
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUMBER);
        menuItem.setChecked(true);
    }
}