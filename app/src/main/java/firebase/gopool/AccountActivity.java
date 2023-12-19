package firebase.gopool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import firebase.gopool.navigation.BottomNavigationViewHelperUser;
import firebase.gopool.saveLogin.SaveLogin;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AccountActivity extends AppCompatActivity {

    private static final int ACTIVITY_NUMBER = 3;

    private final Context mContext = AccountActivity.this;

    private TextView icon;
    private TextView name;
    private RelativeLayout changeInfo;
    private RelativeLayout feedBack;
    private RelativeLayout logOut;

    private SaveLogin saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mapping();
        getData();
        setupBottomNavigationView();
        onclickChangeInfo();
        onclickLogOut();
        onclickFeedBack();
    }

    private void mapping() {
        icon = findViewById(R.id.icon);
        name = findViewById(R.id.name);
        changeInfo = findViewById(R.id.changeInfoAccount);
        feedBack = findViewById(R.id.feedback);
        logOut = findViewById(R.id.logOut);

        saveLogin = new SaveLogin(this);
    }

    private void getData() {
        // Create notation
        String[] elementName = saveLogin.getName().split(" ");
        String notation = "";
        int count = 0;
        for (int i = 0; i < elementName.length; i++) {
            notation += elementName[i].charAt(0);
            count++;
            if (count == 2) break;
        }

        icon.setText(notation);
        name.setText(saveLogin.getName());
    }

    private void onclickChangeInfo() {
        changeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, InfoUserActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onclickLogOut() {
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveLogin.clear();
                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onclickFeedBack() {
        feedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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