package firebase.gopool;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import firebase.gopool.dialog.LeaveRideDialog;
import firebase.gopool.model.response.RideResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class InfoRideActivity extends AppCompatActivity {

    private Context mContext = InfoRideActivity.this;
    private RideResponse ride;

    //Widgets
    private ImageView mBack;
    private TextView driverName, carNameAndSeat, licensePlate, cost, bookingDate, expectedOrigin,
            expectedDes, origin, destination, phone;
    private RelativeLayout originTime, desTime, relBookingDate;

//    private RelativeLayout relLayoutLeave;
//    private FloatingActionButton unJoin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_ride);

        mapping();
        back();
        //leave();
    }

    private void mapping() {
        // Get data
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        ride = (RideResponse) bundle.getSerializable("ride");

        // Mapping
        driverName = findViewById(R.id.driverName);
        carNameAndSeat = findViewById(R.id.carNameAndSeat);
        licensePlate = findViewById(R.id.licensePlate);
        cost = findViewById(R.id.cost);
        bookingDate = findViewById(R.id.bookingDate);
        expectedOrigin = findViewById(R.id.expectedOrigin);
        expectedDes = findViewById(R.id.expectedDes);
        origin = findViewById(R.id.origin);
        destination = findViewById(R.id.destination);
        originTime = findViewById(R.id.originTime);
        desTime = findViewById(R.id.desTime);
        mBack = findViewById(R.id.backArrowPickupRide);
//        relLayoutLeave = findViewById(R.id.relLayoutLeave);
//        unJoin = findViewById(R.id.unJoin);
        relBookingDate = findViewById(R.id.relBookingDate);
        phone = findViewById(R.id.phone);

        // set data
        driverName.setText(ride.getNameDriver());
        carNameAndSeat.setText(ride.getNameCar() + " | " + ride.getSeat() + " seats");
        licensePlate.setText(ride.getLicensePlate());
        cost.setText(ride.getCost().intValue() + " VND");
        phone.setText(ride.getPhoneDriver());

        if (ride.isFrequent()) relBookingDate.setVisibility(View.GONE);
        else {
            String[] arrTime = ride.getBookingDate().split("-");
            String time = arrTime[2] + "/" + arrTime[1] + "/" + arrTime[0];
            bookingDate.setText(time);
        }
        if (ride.isFrequent() || ride.getStatusId() == 1) {
            // origin
            double originTime = ride.getExpectedTimeOrigin();
            int originHour = (int) originTime;
            int originMinute = (int) ((originTime - originHour) * 60);
            String originTimeToText = "";
            if (originHour < 10) originTimeToText += "0";
            originTimeToText += originHour + ":";
            if (originMinute < 10) originTimeToText += "0";
            originTimeToText += originMinute;
            expectedOrigin.setText(originTimeToText);

            // destination
            double desTime = ride.getExpectedTimeDestination();
            int desHour = (int) desTime;
            int desMinute = (int) ((desTime - desHour) * 60);
            String desTimeToText = "";
            if (desHour < 10) desTimeToText += "0";
            desTimeToText += desHour + ":";
            if (desMinute < 10) desTimeToText += "0";
            desTimeToText += desMinute;
            expectedDes.setText(desTimeToText);
        } else {
            originTime.setVisibility(View.GONE);
            desTime.setVisibility(View.GONE);
        }
//        if (ride.isFrequent() || ride.getStatusId() != 1) {
//            relLayoutLeave.setVisibility(View.GONE);
//        }
        origin.setText(ride.getAddressStart());
        destination.setText(ride.getAddressEnd());
    }

    private void back() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RidesActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

//    private void leave() {
//        unJoin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Confirmation to delete the ride dialog
//                LeaveRideDialog dialog = new LeaveRideDialog(mContext, ride.getRouteOrRequestId(), ride.isFrequent());
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.show();
//            }
//        });
//    }

}