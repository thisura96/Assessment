package com.avantrio.assessment.ui;

import static com.avantrio.assessment.Utils.getValidEmail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.avantrio.assessment.R;
import com.avantrio.assessment.api.ApiService;
import com.avantrio.assessment.api.RetrofitClient;
import com.avantrio.assessment.model.LoginRequest;
import com.avantrio.assessment.model.LoginResponse;
import com.avantrio.assessment.ui.home.HomeActivity;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashScreenActivity extends AppCompatActivity implements TextWatcher {
    TextView btn_Login;
    TextInputEditText et_email,et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        btn_Login = (TextView) findViewById(R.id.btn_splashScreen_next);
        et_email =(TextInputEditText) findViewById(R.id.et_email);
        et_password =(TextInputEditText) findViewById(R.id.et_password);


        et_email.addTextChangedListener(this);
        et_password.addTextChangedListener(this);


        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                callLogin(email,password);

            }
        });
     }



    public void controlLoginButton() {

        try {
            String email = et_email.getText().toString();
            String password = et_password.getText().toString();

            if (!email.equals("") && !password.equals("")) {
                btn_Login.setEnabled(true);
                btn_Login.setBackgroundResource(R.color.btn_login_color);
            } else {
                btn_Login.setEnabled(false);
                btn_Login.setBackgroundResource(R.color.btn_disable_login_color);
            }
        } catch (Exception e) {

            Toast.makeText(SplashScreenActivity.this, "error" + e.toString(), Toast.LENGTH_SHORT).show();

        }
    }


    public void callLogin(String email, String password){


        ApiService apiService = RetrofitClient.createService(ApiService.class);
        Call<LoginResponse> loginResponseCall = apiService.loginUser(new LoginRequest(email,password));
        loginResponseCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == 200) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences((getApplicationContext()));
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", response.body().getToken());
                    editor.apply();
                    Intent myIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    SplashScreenActivity.this.startActivity(myIntent);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Log.d("onFailure", "callLogin: " + jObjError.getString("message"));
                        Toast.makeText(getApplicationContext(), "There seems to be an error processing you request. Please try again later.", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Log.d("Exception", "callLogin: " + e.getMessage());
                        Toast.makeText(getApplicationContext(), "There seems to be an error processing you request. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d("onResponseFailure", "callLogin: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "There seems to be an error processing you request. Please try again later.", Toast.LENGTH_SHORT).show();

            }
        });



    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        controlLoginButton();
    }
}