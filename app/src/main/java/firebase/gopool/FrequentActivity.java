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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import firebase.gopool.adapter.FrequentAdapter;
import firebase.gopool.api.API;
import firebase.gopool.model.response.FrequentResponse;
import firebase.gopool.navigation.BottomNavigationViewHelperUser;
import firebase.gopool.saveLogin.SaveLogin;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class FrequentActivity extends AppCompatActivity {

    private static final int ACTIVITY_NUMBER = 1;

    //View variables
    private RelativeLayout mNoResultsFoundLayout;

    //Recycle View variables
    private final Context mContext = FrequentActivity.this;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mRecycleAdapter;
    private FrequentAdapter frequentAdapter;
    private ArrayList<FrequentResponse> frequents;

    private SaveLogin saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequent);
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
        frequents = new ArrayList<FrequentResponse>();

        mNoResultsFoundLayout = (RelativeLayout) findViewById(R.id.noResultsFoundLayout);

        API.api.getAllRouteFrequentByAccountId(saveLogin.getHeaderJwt()).enqueue(new Callback<ArrayList<FrequentResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<FrequentResponse>> call, Response<ArrayList<FrequentResponse>> response) {
                if (response.code() == 200) {
                    frequents = response.body();
                    if (!frequents.isEmpty()) {
                        frequentAdapter = new FrequentAdapter(FrequentActivity.this, frequents);
                        mRecyclerView.setAdapter(frequentAdapter);
                        mNoResultsFoundLayout.setVisibility(View.INVISIBLE);
                    } else {
                        mNoResultsFoundLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FrequentResponse>> call, Throwable t) {
                Toast.makeText(FrequentActivity.this, "Fail load list frequent!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelperUser.enableNavigation(mContext, bottomNavigationView);

        //Change current highlighted icon
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUMBER);
        menuItem.setChecked(true);
    }
}