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
import firebase.gopool.model.request.MessageResponse;
import firebase.gopool.saveLogin.SaveLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity {

    private ImageView back;
    private EditText otp, next;
    private TextView notification;

    private SaveLogin saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);

        /*
            Intent receive from:
                + ForgetPasswordActivity about: String email
         */
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");

        mapping();
        onclickBack();
        onclickNext(bundle);
    }

    private void mapping() {
        back = findViewById(R.id.back);
        otp = findViewById(R.id.otp);
        next = findViewById(R.id.next);
        notification = findViewById(R.id.notification);

        saveLogin = new SaveLogin(this);
    }

    private void onclickBack() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OTPActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onclickNext(Bundle bundle) {
        next.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                // Test input
                if (otp.getText().toString().equals("")) {
                    notification.setText("Please fill complete information");

                    ViewGroup.LayoutParams params = notification.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    notification.setLayoutParams(params);
                    return;
                }

                // Send request forget password
                API.api.sendOTP(Integer.parseInt(otp.getText().toString()), bundle.getString("email")).enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                        if (response.code() == 200) {
                            Intent intent = new Intent(OTPActivity.this, ChangePasswordActivity.class);
                            Bundle bundleSend = new Bundle();
                            bundleSend.putString("email", bundle.getString("email"));
                            intent.putExtra("data", bundleSend);
                            startActivity(intent);
                        } else {
                            notification.setText("OTP incorrect");

                            ViewGroup.LayoutParams params = notification.getLayoutParams();
                            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                            notification.setLayoutParams(params);
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {

                    }
                });
            }
        });
    }
}