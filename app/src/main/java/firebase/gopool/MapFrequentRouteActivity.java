package firebase.gopool;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.appcompat.app.AppCompatActivity;

import firebase.gopool.MapDirectionHelper.FetchURL;
import firebase.gopool.MapDirectionHelper.TaskLoadedCallback;
import firebase.gopool.api.API;
import firebase.gopool.model.request.MessageResponse;
import firebase.gopool.model.request.ShareRequest;
import firebase.gopool.model.response.FrequentResponse;
import firebase.gopool.saveLogin.SaveLogin;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFrequentRouteActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap;
    private FrequentResponse route;
    private Marker markerStart, markerEnd;

    private ImageView back;
    private Button btn_share, btn_cancel_share;

    private SaveLogin saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_frequent_route);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapping();
        init();
        onClickBack();
    }

    private void mapping() {
        btn_share = (Button) findViewById(R.id.btn_share);
        btn_cancel_share = (Button) findViewById(R.id.btn_cancel_share);
        back = findViewById(R.id.back);

        saveLogin = new SaveLogin(this);
    }

    private void onClickBack() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapFrequentRouteActivity.this, FrequentActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init() {
        if (getIntent() != null) {
            route = (FrequentResponse) getIntent().getSerializableExtra("data");
        }

        if (route.isShared()) {
            btn_cancel_share.setVisibility(View.VISIBLE);
            btn_share.setVisibility(View.GONE);
        } else {
            btn_cancel_share.setVisibility(View.GONE);
            btn_share.setVisibility(View.VISIBLE);
        }

        setOnclick();
    }

    private void setOnclick() {
        btn_share.setOnClickListener(view -> {
            API.api.shareFrequent(saveLogin.getHeaderJwt(), new ShareRequest(true, route.getId())).enqueue(new Callback<MessageResponse>() {
                @Override
                public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                    Toast.makeText(MapFrequentRouteActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MapFrequentRouteActivity.this, FrequentActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<MessageResponse> call, Throwable t) {
                    Toast.makeText(MapFrequentRouteActivity.this, "Fail!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btn_cancel_share.setOnClickListener(view -> {
            API.api.shareFrequent(saveLogin.getHeaderJwt(), new ShareRequest(false, route.getId())).enqueue(new Callback<MessageResponse>() {
                @Override
                public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                    Toast.makeText(MapFrequentRouteActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MapFrequentRouteActivity.this, FrequentActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<MessageResponse> call, Throwable t) {
                    Toast.makeText(MapFrequentRouteActivity.this, "Fail!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void zoomMap(LatLng startPoint, LatLng endPoint) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(startPoint);
        builder.include(endPoint);
        LatLngBounds bounds = builder.build();

        int padding = 100;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
    }

    private void createMarkerStart(LatLng startPoint) {
        String snippet = route.getAddressStart() + "\n" +
                "Starting time: " + route.getTimeStart() + "\n";

        MarkerOptions marker = new MarkerOptions()
                .position(startPoint)
                .title("Origin")
                .snippet(snippet);

        markerStart = mMap.addMarker(marker);
    }

    private void createMarkerEnd(LatLng endPoint) {
        String snippet = route.getAddressEnd() + "\n" +
                "Arrival time: " + route.getTimeEnd() + "\n";

        MarkerOptions marker = new MarkerOptions()
                .position(endPoint)
                .title("Destination")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .snippet(snippet);

        markerEnd = mMap.addMarker(marker);
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        if (route != null) {
            LatLng startPoint = new LatLng(route.getLatStart(), route.getLngStart());
            LatLng endPoint = new LatLng(route.getLatEnd(), route.getLngEnd());
            createMarkerStart(startPoint);
            createMarkerEnd(endPoint);
            new FetchURL(MapFrequentRouteActivity.this, mMap).execute(getUrl(markerStart.getPosition(), markerEnd.getPosition(), "driving"), "driving");

            mMap.setOnMapLoadedCallback(() -> {
                zoomMap(startPoint, endPoint);
            });
        }

    }

    @Override
    public void onTaskDone(Object... values) {

    }
}