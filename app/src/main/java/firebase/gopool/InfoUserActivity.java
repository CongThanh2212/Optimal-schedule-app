package firebase.gopool;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.appcompat.app.AppCompatActivity;

import firebase.gopool.api.API;
import firebase.gopool.model.request.MessageResponse;
import firebase.gopool.model.response.PassengerResponse;
import firebase.gopool.saveLogin.SaveLogin;

public class InfoUserActivity extends AppCompatActivity {

    private ImageView back;
    private EditText email, name, password, repassword, change, phone;
    private TextView notification;

    private SaveLogin saveLogin;

    // Response from server
    private PassengerResponse passenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);

        mapping();
        getData();
        onclickChange();
        onclickBack();
    }

    private void mapping() {
        back = findViewById(R.id.back);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        change = findViewById(R.id.change);
        phone = findViewById(R.id.phone);
        notification = findViewById(R.id.notification);

        saveLogin = new SaveLogin(this);
    }

    private void getData() {
        API.api.infoPassenger(saveLogin.getHeaderJwt()).enqueue(new Callback<PassengerResponse>() {
            @Override
            public void onResponse(Call<PassengerResponse> call, Response<PassengerResponse> response) {
                passenger = response.body();
                email.setText(passenger.getEmail());
                name.setText(passenger.getFullName());
                phone.setText(passenger.getPhone());
            }

            @Override
            public void onFailure(Call<PassengerResponse> call, Throwable t) {

            }
        });
    }

    private void onclickChange() {
        change.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                // Test input part1
                if (name.getText().toString().equals("") || password.getText().toString().equals("")
                        || repassword.getText().toString().equals("")
                        || phone.getText().toString().equals("")) {
                    notification.setText("Please fill complete information");

                    ViewGroup.LayoutParams params = notification.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    notification.setLayoutParams(params);
                    return;
                }

                // Test input part2
                if (!password.getText().toString().equals(repassword.getText().toString())) {
                    notification.setText("Repassword incorrect");

                    ViewGroup.LayoutParams params = notification.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    notification.setLayoutParams(params);

                    return;
                }

                // Change data of request
                passenger.setFullName(name.getText().toString());
                passenger.setPhone(phone.getText().toString());
                passenger.setPassword(password.getText().toString());

                // Request change
                API.api.changeInfoPassenger(saveLogin.getHeaderJwt(), passenger).enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                        int role = saveLogin.getRole();
                        String jwt = saveLogin.getJwt();
                        saveLogin.clear();
                        saveLogin.save(role, name.getText().toString(), jwt);
                        Intent intent = new Intent(InfoUserActivity.this, AccountActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {
                        Toast.makeText(InfoUserActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void onclickBack() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoUserActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });
    }
}