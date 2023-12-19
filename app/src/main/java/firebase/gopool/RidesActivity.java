package firebase.gopool;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import firebase.gopool.adapter.RideAdapter;
import firebase.gopool.api.API;
import firebase.gopool.model.response.RideResponse;
import firebase.gopool.navigation.BottomNavigationViewHelperUser;
import firebase.gopool.saveLogin.SaveLogin;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class RidesActivity extends AppCompatActivity {

    private static final int ACTIVITY_NUMBER = 2;

    //View variables
    private RelativeLayout mNoResultsFoundLayout;

    //Recycle View variables
    private final Context mContext = RidesActivity.this;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mRecycleAdapter;
    private RideAdapter rideAdapter;
    private ArrayList<RideResponse> rides;

    private SaveLogin saveLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rides);
        setupBottomNavigationView();

        saveLogin = new SaveLogin(this);
        setupListRide();
    }

    private void setupListRide() {
        //Setup recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mRecycleAdapter);

        mNoResultsFoundLayout = (RelativeLayout) findViewById(R.id.noResultsFoundLayout);

        API.api.getAllRideByAccountId(saveLogin.getHeaderJwt()).enqueue(new Callback<ArrayList<RideResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<RideResponse>> call, Response<ArrayList<RideResponse>> response) {
                if (response.code() == 200) {
                    rides = response.body();
                    System.out.println(rides);
                    if (!rides.isEmpty()) {
                        rideAdapter = new RideAdapter(RidesActivity.this, rides);
                        mRecyclerView.setAdapter(rideAdapter);
                        mNoResultsFoundLayout.setVisibility(View.INVISIBLE);
                    } else {
                        mNoResultsFoundLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RideResponse>> call, Throwable t) {
                Toast.makeText(RidesActivity.this, "Fail load list frequent!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBottomNavigationView(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelperUser.enableNavigation(mContext, bottomNavigationView);

        //Change current highlighted icon
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUMBER);
        menuItem.setChecked(true);
    }

}