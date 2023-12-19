package firebase.gopool;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import firebase.gopool.Common.Common;
import firebase.gopool.Map.CustomInfoWindowAdapter;
import firebase.gopool.Map.PlaceAutocompleteAdapter;
import firebase.gopool.Map.PlaceInfo;
import firebase.gopool.MapDirectionHelper.FetchURL;
import firebase.gopool.MapDirectionHelper.TaskLoadedCallback;
import firebase.gopool.api.API;
import firebase.gopool.model.LocationBooking;
import firebase.gopool.model.Trip;
import firebase.gopool.model.Waypoint;
import firebase.gopool.navigation.BottomNavigationViewHelperUser;
import firebase.gopool.saveLogin.SaveLogin;
import firebase.gopool.utils.UniversalImageLoader;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.compat.AutocompletePrediction;
import com.google.android.libraries.places.compat.GeoDataClient;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.PlaceBuffer;
import com.google.android.libraries.places.compat.PlaceBufferResponse;
import com.google.android.libraries.places.compat.PlaceDetectionClient;
import com.google.android.libraries.places.compat.Places;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, TaskLoadedCallback {
    private static final String TAG = "MapsActivity";
    private static final int ACTIVITY_NUMBER = 0;

    private Context mContext = MapsActivity.this;

    //Google map permissions
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));
    private Boolean mLocationPermissionsGranted = false;
    private Place To, From;
    private PlaceInfo placeInfoFrom, placeInfoTo;

    //Google map variables
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private PlaceInfo mPlace;
    private Marker mMarker;
    private double currentLatitude, currentLongtitude;
    private Polyline currentPolyline;
    private MarkerOptions place1, place2;
    private LatLng currentLocation, preLocation;

    private String directionsRequestUrl;
    private String userID;

    //Widgets
    private AutoCompleteTextView originTextview, destinationTextView;
    private Button mSearchBtn, mDirectionsBtn, mSwitchTextBtn, mStartTrip, mEndTrip;
    private RadioButton findButton, shareButton;
    private RadioGroup mRideSelectionRadioGroup;
    private ImageView mLocationBtn;
    private FloatingActionButton currentLocationFAB;

    private ScheduledExecutorService mExecutor;
    private Trip currentTrip;

    private String typeofaction;
    private SaveLogin saveLogin;

    // Data latitude, longitude of origin and destination
    private Double latOrigin = null, lngOrigin = null;
    private Double latDestination = null, lngDestination = null;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Log.d(TAG, "onCreate: starting.");

        saveLogin = new SaveLogin(this);
        findButton = (RadioButton) findViewById(R.id.findButton);
        shareButton = (RadioButton) findViewById(R.id.shareButton);

        typeofaction = "from";
        //Intitate widgets
        originTextview = (AutoCompleteTextView) findViewById(R.id.originTextview);
        originTextview.setOnFocusChangeListener((view, motionEvent) -> {
            typeofaction = "from";
        });
        destinationTextView = (AutoCompleteTextView) findViewById(R.id.destinationTextview);

        destinationTextView.setOnFocusChangeListener((view, motionEvent) -> {
            typeofaction = "to";
        });
        mSearchBtn = (Button) findViewById(R.id.searchBtn);
        mSwitchTextBtn = (Button) findViewById(R.id.switchTextBtn);
        mDirectionsBtn = (Button) findViewById(R.id.directionsBtn);
        mStartTrip = (Button) findViewById(R.id.btn_start_trip);
        mEndTrip = (Button) findViewById(R.id.btn_end_trip);
        mRideSelectionRadioGroup = (RadioGroup) findViewById(R.id.toggle);
        mLocationBtn = (ImageView) findViewById(R.id.locationImage);

        mLocationBtn.setOnClickListener(v -> getDeviceLocationAndAddMarker());

        mSwitchTextBtn.setOnClickListener(v -> {
            if (originTextview.getText().toString().trim().length() > 0 &&
                    destinationTextView.getText().toString().trim().length() > 0 &&
                    latOrigin != null && latDestination != null) {
                // 2023-09-23 ADD START
                Double lat = latOrigin, lng = lngOrigin;
                latOrigin = latDestination;
                lngOrigin = lngDestination;
                latDestination = lat;
                lngDestination = lng;
                // 2023-09-23 ADD END

                String tempDestination1 = originTextview.getText().toString();
                String tempDestination12 = destinationTextView.getText().toString();

                destinationTextView.setText(tempDestination1);
                originTextview.setText(tempDestination12);

                destinationTextView.dismissDropDown();
                originTextview.dismissDropDown();
            } else {
                Toast.makeText(mContext, "Please enter location and destination", Toast.LENGTH_SHORT).show();
            }
        });

        mSearchBtn.setOnClickListener(v -> {
            if (originTextview.getText().toString().trim().length() > 0 && destinationTextView.getText().toString().trim().length() > 0) {
                if (latOrigin == null || latDestination == null) {
                    Toast.makeText(mContext, "Location not found!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent findRideActivity = new Intent(mContext, BookingActivity.class);
                    Bundle bundleSend = new Bundle();
                    bundleSend.putString("addressOrigin", originTextview.getText().toString());
                    bundleSend.putString("addressDestination", destinationTextView.getText().toString());
                    bundleSend.putSerializable("location", new LocationBooking(latOrigin, lngOrigin, latDestination, lngDestination));
                    findRideActivity.putExtra("data", bundleSend);
                    startActivity(findRideActivity);
                }
            } else {
                Toast.makeText(mContext, "Pleas enter origin, destination!", Toast.LENGTH_SHORT).show();
            }
        });

        userLocationFAB();
        mRideSelectionRadioGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            if (checkedId == R.id.shareButton) {
                mStartTrip.setVisibility(View.VISIBLE);
                currentLocationFAB.show();
                mSearchBtn.setVisibility(View.INVISIBLE);

                if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                        FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                            COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                            currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        });
                        moveCameraNoMarker(currentLocation, 17f, "");
                    }
                }
            } else {
                mStartTrip.setVisibility(View.GONE);
                mSearchBtn.setVisibility(View.VISIBLE);
                mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                });
                moveCameraNoMarker(currentLocation, DEFAULT_ZOOM, "");
            }
        });

        mStartTrip.setOnClickListener(view -> {
            mStartTrip.setVisibility(View.GONE);
            mEndTrip.setVisibility(View.VISIBLE);
            Common.statusTrip = Common.START;
            createTrip();
            moveCameraNoMarker(currentLocation, 17f, "");

            findButton.setEnabled(false);
            findButton.setAlpha(.5f);
            findButton.setClickable(false);
        });

        mEndTrip.setOnClickListener(view -> {
            LayoutInflater inflater = MapsActivity.this.getLayoutInflater();
            new AlertDialog.Builder(MapsActivity.this)
                    .setView(inflater.inflate(R.layout.dialog_stop_trip, null))
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            mMap.clear();
                            mEndTrip.setVisibility(View.GONE);
                            mStartTrip.setVisibility(View.VISIBLE);
                            stopExecutor();
                            moveCameraNoMarker(currentLocation, 17f, "");

                            findButton.setEnabled(true);
                            findButton.setAlpha(1f);
                            findButton.setClickable(true);

                        }
                    })

                    .setNegativeButton("Cancel", null)
                    .show();
        });

        initImageLoader();
        setupBottomNavigationView();

        //If play services is true = ok, then check for permissions and setup google maps
        if (isServicesOk()) {
            getLocationPermission();
        }
    }

    // Get latitude and longitude from address
    private void goeLocate(boolean isOrigin) {
        Log.d(TAG, "goeLocate: goeLocating");

        String searchString = originTextview.getText().toString();
        if (!isOrigin) searchString = destinationTextView.getText().toString();

        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "goeLocate: IOException: " + e.getMessage());
        }
        if (list.size() > 0) {
            Address address = list.get(0);
            Log.e(TAG, "goeLocate: found a location: " + address.toString());
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
        }
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


    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    /**
     * Hides phone keyboard if clicked
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /** --------------------------- Setting up google maps / permissions and services ---------------------------- **/

    /**
     * Check if google play services is enabled or available for mobile device
     *
     * @return
     */
    public boolean isServicesOk() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MapsActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is ok and user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occurred but it can be resolved
            Log.d(TAG, "isServicesOK: an error occurred but it can be fixed");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MapsActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "App cannot make map requests current", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: called");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    /**
     * sets up map from the view
     */
    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

        init();
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Log.d(TAG, "onComplete: getting found location!");
                            Location currentLocation = (Location) task.getResult();
                            placeInfoTo = new PlaceInfo();
                            placeInfoTo.setName("My Location");
                            moveCameraNoMarker(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,
                                    "My location");
                            currentLatitude = currentLocation.getLatitude();
                            currentLongtitude = currentLocation.getLongitude();
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void getDeviceLocationAndAddMarker() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Log.d(TAG, "onComplete: getting found location!");
                            Location result = (Location) task.getResult();
                            currentLocation = new LatLng(result.getLatitude(), result.getLongitude());
                            mMap.clear();
                            To = null;

                            moveCamera(new LatLng(result.getLatitude(), result.getLongitude()),
                                    DEFAULT_ZOOM,
                                    "My location");
                            drawMapMarker(false, null);
                            currentLatitude = result.getLatitude();
                            currentLongtitude = result.getLongitude();

                            LatLng latLng = new LatLng(currentLatitude, currentLongtitude);
                            place1 = new MarkerOptions()
                                    .position(latLng)
                                    .title("My location");
                            // 2023-09-23 ADD START
                            latOrigin = currentLatitude;
                            lngOrigin = currentLongtitude;
                            // 2023-09-23 ADD END
                            geoDecoder(result);
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCameraNoMarker(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to: lat:" + latLng.latitude + ", lng: " + latLng.longitude);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        hideKeyboard(MapsActivity.this);
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to: lat:" + latLng.latitude + ", lng: " + latLng.longitude);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("My Location")) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(title)
                    .snippet("");


            mMap.addMarker(markerOptions);
        }

        hideKeyboard(MapsActivity.this);
    }


    /**
     * create marker on the given latlng
     *
     * @param latLng    lat lng of the given place
     * @param placeInfo
     */
    private void createMarker(LatLng latLng, PlaceInfo placeInfo) {
        String snippet = "Your Location";
        if (placeInfo != null) {
            snippet = "Address: " + placeInfo.getAddress() + "\n" +
                    "Phone Number: " + placeInfo.getPhoneNumber() + "\n" +
                    "Website: " + placeInfo.getWebsiteUri() + "\n" +
                    "Price Rating: " + placeInfo.getRating() + "\n";

            MarkerOptions marker = new MarkerOptions()
                    .position(latLng)
                    .title(placeInfo.getName())
                    .snippet(snippet);

            mMarker = mMap.addMarker(marker);
        } else {

            MarkerOptions marker = new MarkerOptions()
                    .position(latLng)
                    .title(snippet);

            mMarker = mMap.addMarker(marker);
        }

    }


    private void moveCamera(LatLng latLng, float zoom, PlaceInfo placeInfo) {
        Log.d(TAG, "moveCamera: moving the camera to: lat:" + latLng.latitude + ", lng: " + latLng.longitude);

        hideKeyboard(MapsActivity.this);

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(mContext));

        if (placeInfo != null) {
            try {
                String snippet = "Address: " + placeInfo.getAddress() + "\n" +
                        "Phone Number: " + placeInfo.getPhoneNumber() + "\n" +
                        "Website: " + placeInfo.getWebsiteUri() + "\n" +
                        "Price Rating: " + placeInfo.getRating() + "\n";

                if (originTextview.hasFocus()) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

                    place1 = new MarkerOptions()
                            .position(latLng)
                            .title(placeInfo.getName())
                            .snippet(snippet);

                    mMarker = mMap.addMarker(place1);

                } else {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));

                    place2 = new MarkerOptions()
                            .position(latLng)
                            .title(placeInfo.getName())
                            .snippet(snippet);

                    mMarker = mMap.addMarker(place2);

                    directionsRequestUrl = getUrl(place1.getPosition(), place2.getPosition(), "driving");

                    new FetchURL(MapsActivity.this, mMap).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
                }

            } catch (NullPointerException e) {
                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage());
            }
        } else {
            mMap.addMarker(new MarkerOptions().position(latLng));
        }
        hideKeyboard(MapsActivity.this);
    }

    private void geoDecoder(Location latLng) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.getLatitude(), latLng.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0);
        originTextview.setText(address);
        originTextview.dismissDropDown();
    }

    private void init() {
        Log.d(TAG, "init: initializing");

        mGeoDataClient = Places.getGeoDataClient(this, null);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);
        mGoogleApiClient = new GoogleApiClient.Builder(MapsActivity.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        originTextview.setOnItemClickListener(mAuotcompleteClickListener);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient, LAT_LNG_BOUNDS, null);

        originTextview.setAdapter(mPlaceAutocompleteAdapter);

        originTextview.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {

                    //execute our method for searching
                    goeLocate(false);
                }

                return false;
            }
        });

        destinationTextView.setOnItemClickListener(mAuotcompleteClickListener);

        destinationTextView.setAdapter(mPlaceAutocompleteAdapter);

        destinationTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    //execute our method for searching
                    goeLocate(true);
                }

                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

     /*
        ---------------------------- google places API autocomplete suggestions -------------------------------
     */

    private AdapterView.OnItemClickListener mAuotcompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            hideKeyboard(MapsActivity.this);

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(position);
            assert item != null;
            final String placeId = item.getPlaceId();

            Places.getGeoDataClient(MapsActivity.this)
                    .getPlaceById(placeId).addOnCompleteListener(place -> {
                        getPlaceDetails(place, typeofaction);

                    }).addOnFailureListener(e -> {
                        Log.e(MapsActivity.TAG, "Place can not be found", e);
                    });
        }
    };


    private void getPlaceDetails(Task<PlaceBufferResponse> places, String typeofaction) {
        mMap.clear();

        final Place place = places.getResult().get(0);
        try {
            mPlace = new PlaceInfo();
            mPlace.setName(place.getName().toString());
            Log.d(TAG, "onResult: name: " + place.getName());
            mPlace.setAddress(place.getAddress().toString());
            Log.d(TAG, "onResult: address: " + place.getAddress());
            // mPlace.setAttributions(place.getAttributions().toString());
            //Log.d(TAG, "onResult: attributions: " + place.getAttributions());
            mPlace.setId(place.getId());
            Log.d(TAG, "onResult: id: " + place.getId());
            mPlace.setLatLng(place.getLatLng());
            Log.d(TAG, "onResult: latLng: " + place.getLatLng());
            mPlace.setRating(place.getRating());
            Log.d(TAG, "onResult: rating: " + place.getRating());
            mPlace.setPhoneNumber(place.getPhoneNumber().toString());
            Log.d(TAG, "onResult: phoneNumber: " + place.getPhoneNumber());
            mPlace.setWebsiteUri(place.getWebsiteUri());
            Log.d(TAG, "onResult: websiteUri: " + place.getWebsiteUri());
            Log.d(TAG, "onResult: place: " + mPlace.toString());

            if (originTextview.isFocused()) {
                currentLocation = mPlace.getLatLng();
            }

            if (typeofaction.equals("from")) {
                From = place;
                placeInfoFrom = mPlace;

                // 2023-09-23 ADD START
                latOrigin = place.getLatLng().latitude;
                lngOrigin = place.getLatLng().longitude;
                // 2023-09-23 ADD END
            } else {
                To = place;
                placeInfoTo = mPlace;

                // 2023-09-23 ADD START
                latDestination = place.getLatLng().latitude;
                lngDestination = place.getLatLng().longitude;
                // 2023-09-23 ADD END
            }
            drawMapMarker(true, mPlace);

        } catch (NullPointerException e) {
            Log.e(TAG, "onResult: NullPointerException: " + e.getMessage());
            if (typeofaction.equals("from")) {
                // 2023-09-23 ADD START
                latOrigin = null;
                lngOrigin = null;
                // 2023-09-23 ADD END
            } else {
                // 2023-09-23 ADD START
                latDestination = null;
                lngDestination = null;
                // 2023-09-23 ADD END
            }
        }

        moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace);

    }

    private void drawMapMarker(Boolean status, PlaceInfo placeInfoFrom) {

        if (From != null) {
            createMarker(new LatLng(this.From.getViewport().getCenter().latitude,
                    this.From.getViewport().getCenter().longitude), this.placeInfoFrom);
        }

        if (To != null) {
            createMarker(new LatLng(To.getViewport().getCenter().latitude,
                    To.getViewport().getCenter().longitude), placeInfoTo);
        } else if (currentLocation != null) {
            moveCamera(currentLocation,
                    DEFAULT_ZOOM,
                    "My location");
        }


    }


    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);

            try {

                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                Log.d(TAG, "onResult: name: " + place.getName());
                mPlace.setAddress(place.getAddress().toString());
                Log.d(TAG, "onResult: address: " + place.getAddress());
                // mPlace.setAttributions(place.getAttributions().toString());
                //Log.d(TAG, "onResult: attributions: " + place.getAttributions());
                mPlace.setId(place.getId());
                Log.d(TAG, "onResult: id: " + place.getId());
                mPlace.setLatLng(place.getLatLng());
                Log.d(TAG, "onResult: latLng: " + place.getLatLng());
                mPlace.setRating(place.getRating());
                Log.d(TAG, "onResult: rating: " + place.getRating());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                Log.d(TAG, "onResult: phoneNumber: " + place.getPhoneNumber());
                mPlace.setWebsiteUri(place.getWebsiteUri());
                Log.d(TAG, "onResult: websiteUri: " + place.getWebsiteUri());
                Log.d(TAG, "onResult: place: " + mPlace.toString());

                if (originTextview.isFocused()) {
                    currentLocation = mPlace.getLatLng();
                }

            } catch (NullPointerException e) {
                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage());
            }

            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                    place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace);

            places.release();

        }
    };

    /*
     * Permission locations
     * */

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: get location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /***
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelperUser.enableNavigation(mContext, bottomNavigationView);

        //Change current highlighted icon
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUMBER);
        menuItem.setChecked(true);
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null) {
            currentPolyline.remove();
            currentPolyline = mMap.addPolyline((PolylineOptions) values[1]);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    private void userLocationFAB() {
        currentLocationFAB = (FloatingActionButton) findViewById(R.id.myLocationButton);
        currentLocationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mMap.getMyLocation() != null) { // Check to ensure coordinates aren't null, probably a better way of doing this...
                    moveCameraNoMarker(new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()),
                            17f,
                            "My location");
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void createTrip() {
        Trip trip = new Trip();
        trip.setAccountOwner(userID);

        SimpleDateFormat tf = new SimpleDateFormat("yyyy-MM-dd");
        String startDateStr = tf.format(new Date());
        trip.setDate(startDateStr);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == 1) day = 7;
        else day -= 1;
        trip.setWeekday(day);

        API.api.addTrip(saveLogin.getHeaderJwt(), trip).enqueue(new Callback<Trip>() {
            @Override
            public void onResponse(Call<Trip> call, Response<Trip> response) {
                currentTrip = response.body();
                updatedRoute();
            }

            @Override
            public void onFailure(Call<Trip> call, Throwable t) {
                Log.e("Fail add trip", t.getLocalizedMessage());
            }
        });
    }

    private void updatedRoute() {
        Context context = this;
        Runnable helloRunnable = new Runnable() {
            public void run() {
                if (ContextCompat.checkSelfPermission(context.getApplicationContext(),
                        FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(context.getApplicationContext(),
                            COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                            currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            if (preLocation == null) {
                                preLocation = currentLocation;
                                updateWaypoint();
                            } else if (preLocation.latitude != currentLocation.latitude || preLocation.longitude != currentLocation.longitude) {
                                updateWaypoint();
                                Polyline line = mMap.addPolyline(new PolylineOptions()
                                        .add(preLocation, currentLocation)
                                        .width(10)
                                        .color(Color.RED));
                                preLocation = currentLocation;
                            }
                        });
                    }
                }
            }
        };

        mExecutor = Executors.newScheduledThreadPool(1);
        mExecutor.scheduleAtFixedRate(helloRunnable, 0, 3, TimeUnit.SECONDS);

    }

    private void stopExecutor() {
        if (mExecutor != null) {
            mExecutor.shutdown();
        }
    }

    private void updateWaypoint() {
        Waypoint waypoint = new Waypoint();
        waypoint.setLatitude(mMap.getMyLocation().getLatitude());
        waypoint.setLongitude(mMap.getMyLocation().getLongitude());

        if (currentTrip != null) {
            waypoint.setOnTrip(currentTrip.getTripId());
        }

        SimpleDateFormat tf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        String startDateStr = tf.format(new Date());
        waypoint.setTime(startDateStr);

        API.api.addWaypoint(saveLogin.getHeaderJwt(), waypoint).enqueue(new Callback<Waypoint>() {
            @Override
            public void onResponse(Call<Waypoint> call, Response<Waypoint> response) {
            }

            @Override
            public void onFailure(Call<Waypoint> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                });
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopExecutor();
    }
}
