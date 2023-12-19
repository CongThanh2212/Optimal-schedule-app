package firebase.gopool;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import firebase.gopool.api.API;
import firebase.gopool.model.LocationBooking;
import firebase.gopool.model.request.BookOnlineRequest;
import firebase.gopool.model.response.RideResponse;
import firebase.gopool.saveLogin.SaveLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingActivity extends AppCompatActivity {

    private final Context mContext = BookingActivity.this;

    //Fragment view
    private View view;

    private TextView originTextView, destinationTextView, notification;
    private NumberPicker capacity;
    private TimePicker time;
    private EditText booking;
    private ImageView backArrow;

    // Data from MapsActivity
    private String addressStart, addressEnd;
    private double length;
    private LocationBooking locationBooking;

    private SaveLogin saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        mapping();
        onClickBack();
        onClickBooking();
    }

    private void mapping() {
        originTextView = findViewById(R.id.locationEditText);
        destinationTextView = findViewById(R.id.destinationEditText);
        capacity = findViewById(R.id.capacity);
        time = findViewById(R.id.time);
        notification = findViewById(R.id.notification);
        booking = findViewById(R.id.booking);
        backArrow = findViewById(R.id.backArrowFindRide);

        // Setup capacity
        capacity.setMinValue(1);
        capacity.setMaxValue(10);
        capacity.setWrapSelectorWheel(true);

        // Get data from MapsActivity
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        addressStart = bundle.getString("addressOrigin");
        addressEnd = bundle.getString("addressDestination");
        locationBooking = (LocationBooking) bundle.getSerializable("location");

        length = locationBooking.distance(addressStart, addressEnd, getResources());

        originTextView.setText(addressStart);
        destinationTextView.setText(addressEnd);

        saveLogin = new SaveLogin(this);
    }

    private void onClickBack() {
        // Back
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent findRideActivity = new Intent(mContext, MapsActivity.class);
                startActivity(findRideActivity);
            }
        });
    }

    private void onClickBooking() {
        booking.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                // Setup data for send
                double pickUpTime = (double) time.getHour() + ((double) time.getMinute()) / 60;
                BookOnlineRequest data = new BookOnlineRequest(pickUpTime, capacity.getValue(),
                        locationBooking.getLatOrigin(), locationBooking.getLngOrigin(),
                        locationBooking.getLatDestination(), locationBooking.getLngDestination(),
                        addressStart, addressEnd, length);

                API.api.bookingLinear(saveLogin.getHeaderJwt(), data).enqueue(new Callback<RideResponse>() {
                    @Override
                    public void onResponse(Call<RideResponse> call, Response<RideResponse> response) {
                        if (response.code() == 200) {
                            RideResponse ride = response.body();
                            Intent intent = new Intent(mContext, InfoRideActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("ride", ride);
                            intent.putExtra("data", bundle);
                            startActivity(intent);
                        } else {
                            notification.setText("No matching trips! Try again in a few minutes");

                            ViewGroup.LayoutParams params = notification.getLayoutParams();
                            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                            notification.setLayoutParams(params);
                        }
                    }

                    @Override
                    public void onFailure(Call<RideResponse> call, Throwable t) {

                    }
                });
            }
        });
    }
}