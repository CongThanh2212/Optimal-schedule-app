package firebase.gopool;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import firebase.gopool.api.API;
import firebase.gopool.model.request.MessageResponse;
import firebase.gopool.model.response.DriverResponse;
import firebase.gopool.saveLogin.SaveLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoDriverActivity extends AppCompatActivity {

    private ImageView back;
    private EditText email, nameAndSeat, licensePlate, name, phone, password, repassword, change;
    private TextView notification;

    private SaveLogin saveLogin;

    // Response from server
    private DriverResponse driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_driver);

        mapping();
        getData();
        onclickChange();
        onclickBack();
    }

    private void mapping() {
        back = findViewById(R.id.back);
        email = findViewById(R.id.email);
        nameAndSeat = findViewById(R.id.nameAndSeatCar);
        licensePlate = findViewById(R.id.licensePlate);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        change = findViewById(R.id.change);
        notification = findViewById(R.id.notification);

        saveLogin = new SaveLogin(this);
    }

    private void getData() {
        API.api.infoDriver(saveLogin.getHeaderJwt()).enqueue(new Callback<DriverResponse>() {
            @Override
            public void onResponse(Call<DriverResponse> call, Response<DriverResponse> response) {
                driver = response.body();
                email.setText(driver.getEmail());
                nameAndSeat.setText(driver.getNameCar() + " | " + driver.getSeat() + " seats");
                licensePlate.setText(driver.getLicensePlate());
                name.setText(driver.getFullName());
                phone.setText(driver.getPhone());
            }

            @Override
            public void onFailure(Call<DriverResponse> call, Throwable t) {

            }
        });
    }

    private void onclickChange() {
        change.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                // Test input part1
                if (name.getText().toString().equals("") || password.getText().toString().equals("")
                        || repassword.getText().toString().equals("") || phone.getText().toString().equals("")) {
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
                driver.setFullName(name.getText().toString());
                driver.setPhone(phone.getText().toString());
                driver.setPassword(password.getText().toString());

                // Request change
                API.api.changeInfoDriver(saveLogin.getHeaderJwt(), driver).enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                        int role = saveLogin.getRole();
                        String jwt = saveLogin.getJwt();
                        saveLogin.clear();
                        saveLogin.save(role, name.getText().toString(), jwt);
                        Intent intent = new Intent(InfoDriverActivity.this, AccountDriverActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {
                        Toast.makeText(InfoDriverActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void onclickBack() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoDriverActivity.this, AccountDriverActivity.class);
                startActivity(intent);
            }
        });
    }

}