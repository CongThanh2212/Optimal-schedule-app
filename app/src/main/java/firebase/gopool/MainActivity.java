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
import firebase.gopool.model.response.LoginResponse;
import firebase.gopool.saveLogin.SaveLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText login;
    private TextView signUp;
    private TextView forget;
    private ImageView facebook;
    private TextView notification;

    private SaveLogin saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapping();
        checkLogin();
        onclickLogin();
        onclickSignUp();
        onclickForget();
    }

    private void mapping() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.sign_up);
        forget = findViewById(R.id.forget_password);
        facebook = findViewById(R.id.facebook);
        notification = findViewById(R.id.notification);

        saveLogin = new SaveLogin(this);
    }

    private void checkLogin() {
        int role = saveLogin.getRole();
        if (role == 0) return;
        Intent intent;
        if (role == 2) intent = new Intent(MainActivity.this, AccountDriverActivity.class);
        else intent = new Intent(MainActivity.this, AccountActivity.class);
        startActivity(intent);
    }

    private void onclickLogin() {
        login.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                // Check input
                if (email.getText().toString().equals("") || password.getText().toString().equals("")) {
                    notification.setText("Please fill complete information");

                    ViewGroup.LayoutParams params = notification.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    notification.setLayoutParams(params);
                    return;
                }

                // Send request login
                API.api.login(email.getText().toString(), password.getText().toString()).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.code() == 200) {
                            LoginResponse data = response.body();

                            switch (data.getRole()) {
                                case 1: {
                                    notification.setText("Not permission");
                                    ViewGroup.LayoutParams params = notification.getLayoutParams();
                                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                    notification.setLayoutParams(params);
                                    break;
                                }
                                case 2: {
                                    saveLogin.save(data.getRole(), data.getFullName(), data.getJwt());
                                    Intent intent = new Intent(MainActivity.this, AccountDriverActivity.class);
                                    startActivity(intent);
                                    break;
                                }
                                default: {
                                    saveLogin.save(data.getRole(), data.getFullName(), data.getJwt());
                                    Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                                    startActivity(intent);
                                }
                            }
                        } else {
                            notification.setText("Incorrect account");
                            ViewGroup.LayoutParams params = notification.getLayoutParams();
                            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                            notification.setLayoutParams(params);
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void onclickSignUp() {
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onclickForget() {
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}