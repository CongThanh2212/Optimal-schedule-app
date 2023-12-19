package firebase.gopool;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import firebase.gopool.api.API;
import firebase.gopool.model.request.PasswordRequest;
import firebase.gopool.model.response.LoginResponse;
import firebase.gopool.saveLogin.SaveLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private ImageView back;
    private EditText password;
    private EditText repassword;
    private EditText change;
    private TextView notification;

    private SaveLogin saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        /*
            Intent receive from:
                + OTPActivity about: String email
         */
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");

        mapping();
        onclickBack();
        onclickChange(bundle);
    }

    private void mapping() {
        back = findViewById(R.id.back);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        change = findViewById(R.id.change);
        notification = findViewById(R.id.notification);

        saveLogin = new SaveLogin(this);
    }

    private void onclickBack() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangePasswordActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onclickChange(Bundle bundle) {
        change.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                // Test input part1
                if (password.getText().toString().equals("") || repassword.getText().toString().equals("")) {
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

                API.api.changePassword(new PasswordRequest(bundle.getString("email"), password.getText().toString())).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        LoginResponse data = response.body();
                        saveLogin.save(data.getRole(), data.getFullName(), data.getJwt());

                        switch (data.getRole()) {
                            case 1: {
                                notification.setText("Not permission");
                                ViewGroup.LayoutParams params = notification.getLayoutParams();
                                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                notification.setLayoutParams(params);
                                break;
                            }
                            case 2: {
                                Intent intent = new Intent(ChangePasswordActivity.this, AccountDriverActivity.class);
                                startActivity(intent);
                                break;
                            }
                            default: {
                                Intent intent = new Intent(ChangePasswordActivity.this, AccountActivity.class);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {

                    }
                });
            }
        });
    }
}