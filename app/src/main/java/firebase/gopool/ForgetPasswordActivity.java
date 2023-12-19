package firebase.gopool;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.appcompat.app.AppCompatActivity;

import firebase.gopool.api.API;
import firebase.gopool.model.request.MessageResponse;

public class ForgetPasswordActivity extends AppCompatActivity {

    private ImageView back;
    private EditText email;
    private EditText sendOtp;
    private TextView notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        mapping();
        onclickBack();
        onclickSendOtp();
    }

    private void mapping() {
        back = findViewById(R.id.back);
        email = findViewById(R.id.email);
        sendOtp = findViewById(R.id.send_otp);
        notification = findViewById(R.id.notification);
    }

    private void onclickBack() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgetPasswordActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onclickSendOtp() {
        sendOtp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                // Test input
                if (email.getText().toString().equals("")) {
                    notification.setText("Please fill complete information");

                    ViewGroup.LayoutParams params = notification.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    notification.setLayoutParams(params);
                    return;
                }

                Intent intent = new Intent(ForgetPasswordActivity.this, OTPActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("email", email.getText().toString());
                intent.putExtra("data", bundle);
                startActivity(intent);

                // Send request forget password
                API.api.requireForget(email.getText().toString()).enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {

                    }
                });
            }
        });
    }
}