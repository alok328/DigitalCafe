package com.alok328raj.digitalcafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alok328raj.digitalcafe.API.ApiClient;
import com.alok328raj.digitalcafe.API.Model.LoginResponse;
import com.alok328raj.digitalcafe.API.RequestBody.LoginRequestBody;
import com.alok328raj.digitalcafe.API.RequestBody.SignupRequestBody;
import com.libizo.CustomEditText;
import com.onurkagan.ksnack_lib.Animations.Slide;
import com.onurkagan.ksnack_lib.KSnack.KSnack;
import com.onurkagan.ksnack_lib.KSnack.KSnackBarEventListener;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    CustomEditText rollET, passwordET;
    CustomEditText rollSingupET, nameET, emailET, passwortSignupET;
    LinearLayout loginLinearLayout, signupLinearLayout;
    Retrofit retrofit;
    ApiClient client;

    public void signUpButton(View v){
        String roll = String.valueOf(rollSingupET.getText());
        String name = String.valueOf(nameET.getText());
        String email = String.valueOf(emailET.getText());
        String password = String.valueOf(passwortSignupET.getText());
        if(roll.length()<1 || name.length()<1 || email.length()<1 || password.length()<1){
            showSnackbar("Please enter valid data", R.color.ksnack_error);
        }else {

            SignupRequestBody signupRequestBody = new SignupRequestBody(roll, name, email, password);

            final Call<JSONObject> signup = client.signup(signupRequestBody);
            signup.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                    if (response.code() == 201) {
                        loginLinearLayout.setVisibility(View.VISIBLE);
                        signupLinearLayout.setVisibility(View.INVISIBLE);
                        showSnackbar("Sign up successful", R.color.ksnack_success);
                    } else {
                        showSnackbar("Roll already registered", R.color.ksnack_error);
                    }
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    showSnackbar(t.getMessage(), R.color.ksnack_error);
                }
            });
        }
    }

    public void loginButton(View v){

        String roll = String.valueOf(rollET.getText());
        String password = String.valueOf(passwordET.getText());

        if(roll.length()<1 || password.length()<1){
            showSnackbar("Please enter valid data", R.color.ksnack_error);
        }else {

            LoginRequestBody loginRequestBody = new LoginRequestBody(roll, password);

            final Call<LoginResponse> login = client.login(loginRequestBody);
            login.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                    if (response.code() == 200) {
                        Intent homeIntent = new Intent(getApplicationContext(), Home.class);
                        Toast.makeText(Login.this, response.body().getName(), Toast.LENGTH_SHORT).show();
                        startActivity(homeIntent);
                        finish();
                    } else {
                        showSnackbar("Incorrect roll/password", R.color.ksnack_error);
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    showSnackbar(t.getMessage(), R.color.ksnack_error);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginLinearLayout = this.findViewById(R.id.login_linear_Layout);
        signupLinearLayout = this.findViewById(R.id.signup_linear_Layout);
        rollET = this.findViewById(R.id.usernameET);
        passwordET = this.findViewById(R.id.passwordET);
        rollSingupET = this.findViewById(R.id.rollSignup);
        nameET = this.findViewById(R.id.nameSignup);
        emailET = this.findViewById(R.id.emailSignup);
        passwortSignupET = this.findViewById(R.id.passwordSignUp);

        int val = getIntent().getIntExtra("button", 1);

        if(val==1){
            loginLinearLayout.setVisibility(View.VISIBLE);
            signupLinearLayout.setVisibility(View.INVISIBLE);
        }else{
            signupLinearLayout.setVisibility(View.VISIBLE);
            loginLinearLayout.setVisibility(View.INVISIBLE);
        }

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.103:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        client = retrofit.create(ApiClient.class);
    }

    public void showSnackbar(String message, int color){
        KSnack kSnack = new KSnack(Login.this);
        kSnack
                .setListener(new KSnackBarEventListener() { // listener
                    @Override
                    public void showedSnackBar() {
                        System.out.println("Showed");
                    }

                    @Override
                    public void stoppedSnackBar() {
                        System.out.println("Stopped");
                    }
                })
                .setMessage(message) // message
                .setTextColor(R.color.white) // message text color
                .setBackColor(color) // background color
                .setAnimation(Slide.Up.getAnimation(kSnack.getSnackView()), Slide.Down.getAnimation(kSnack.getSnackView()))
                .setDuration(4000) // you can use for auto close.
                .show();
    }
}
