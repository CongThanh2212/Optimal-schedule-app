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
import firebase.gopool.model.request.SignupRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private ImageView back;
    private EditText email, name, phone, password, repassword, signUp;
    private TextView notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mapping();
        onclickBack();
        onclickSignUp();
    }

    private void mapping() {
        back = findViewById(R.id.back);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        signUp = findViewById(R.id.sign_up);
        notification = findViewById(R.id.notification);
    }

    private void onclickBack() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onclickSignUp() {
        signUp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String emailString = email.getText().toString();
                String nameString = name.getText().toString();
                String phoneString = phone.getText().toString();
                String passwordString = password.getText().toString();
                String repasswordString = repassword.getText().toString();
                // Test input part1
                if (emailString.equals("") || nameString.equals("") || phoneString.equals("")
                        || passwordString.equals("") || repasswordString.equals("")) {
                    notification.setText("Please fill complete information");

                    ViewGroup.LayoutParams params = notification.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    notification.setLayoutParams(params);
                    return;
                }

                // Test input part2
                if (!passwordString.equals(repasswordString)) {
                    notification.setText("Repassword incorrect");

                    ViewGroup.LayoutParams params = notification.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    notification.setLayoutParams(params);

                    return;
                }

                // Setup data for send
                SignupRequest passenger = new SignupRequest(emailString, nameString, phoneString, passwordString);

                // Send request require create account
                API.api.signupPassenger(passenger).enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                        if (response.code() == 200) {
                            Toast.makeText(SignUpActivity.this, "Sign up success!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                            return;
                        }
                        notification.setText("Email registered!");

                        ViewGroup.LayoutParams params = notification.getLayoutParams();
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        notification.setLayoutParams(params);
                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {

                    }
                });
            }
        });
    }
}