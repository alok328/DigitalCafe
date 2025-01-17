package com.alok328raj.digitalcafe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.alok328raj.digitalcafe.API.ApiClient;
import com.alok328raj.digitalcafe.API.Model.LoginResponse;
import com.alok328raj.digitalcafe.API.RequestBody.LoginRequestBody;
import com.alok328raj.digitalcafe.API.RequestBody.SignupRequestBody;
import com.alok328raj.digitalcafe.Animation.MyBounceInterpolator;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.libizo.CustomEditText;
import com.onurkagan.ksnack_lib.Animations.Slide;
import com.onurkagan.ksnack_lib.KSnack.KSnack;
import com.onurkagan.ksnack_lib.KSnack.KSnackBarEventListener;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    CustomEditText rollET, passwordET;
    CustomEditText rollSingupET, nameET, lastnameET, emailET, passwortSignupET;
    LinearLayout loginLinearLayout, signupLinearLayout;
    Retrofit retrofit;
    ApiClient client;
    Animation rotateAnimation, loginAnimation;
    String hostel = "";
    Spinner hostelSpinner;

    private static final String SHARED_PREF = "user";
    private static final String KEY_ROLL = "key_roll";
    private static final String KEY_PASS= "key_pass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        checkLoginOrSignupViews();
        initilizeAnimations();
        setRollAndPass();
    }

    public void signUpButton(final View v){
        String roll = String.valueOf(rollSingupET.getText());
        String firstName = String.valueOf(nameET.getText());
        String lastName = String.valueOf(lastnameET.getText());
        String email = String.valueOf(emailET.getText());
        String password = String.valueOf(passwortSignupET.getText());
        if(hostel.length()<1 || hostel.length()>2){
            showSnackbar("Please select your hostel!", R.color.ksnack_error);
        }else if(roll.contains("/") || roll.contains("-") || roll.contains(".")){
            showSnackbar("Please do not use characters like '/' in roll", R.color.ksnack_error);
        }else if(roll.length()<1 || firstName.length()<1 || lastName.length()<1 || email.length()<1 || password.length()<1){
            showSnackbar("Please enter valid data", R.color.ksnack_error);
        }else {
            v.startAnimation(rotateAnimation);
            callSignupRoute(roll, hostel, firstName, lastName, email, password, v);
        }
    }

    public void loginButton(final View v){
        String roll = String.valueOf(rollET.getText());
        String password = String.valueOf(passwordET.getText());
        if(roll.length()<1 || password.length()<1){
            showSnackbar("Please enter valid data", R.color.ksnack_error);
        }else if(roll.contains("/") || roll.contains(".") || roll.contains("-")){
            showSnackbar("Please do not use characters like '/' in roll", R.color.ksnack_error);
        }else{
            callLoginRoute(roll, password, v);
        }
    }

    private void callLoginRoute(String roll, String password, final View v){
        sharePref(roll, password);
        v.startAnimation(rotateAnimation);
        LoginRequestBody loginRequestBody = new LoginRequestBody(roll, password);

        final Call<LoginResponse> login = client.login(loginRequestBody);
        login.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                v.clearAnimation();
                if (response.code() == 200) {
                    Intent homeIntent = new Intent(getApplicationContext(), Home.class);
//                        Toast.makeText(Login.this, response.body().getName(), Toast.LENGTH_SHORT).show();
                    homeIntent.putExtra("token", response.body().getToken());
                    homeIntent.putExtra("roll", response.body().getRoll());
                    homeIntent.putExtra("username", response.body().getName());
                    ActivityOptions options = ActivityOptions.makeScaleUpAnimation(v, 0,
                            0, v.getWidth(), v.getHeight());
                    startActivity(homeIntent);
                    finish();
                } else if(response.code() == 401){
                    showSnackbar("Incorrect roll/password", R.color.ksnack_error);
                } else if(response.code() == 404){
                    showSnackbar("user not registered", R.color.ksnack_error);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                v.clearAnimation();
                showSnackbar(t.getMessage(), R.color.ksnack_error);
            }
        });
    }

    private void callSignupRoute(String roll, String hostel, String firstName, String lastName,
                                 String email, String password, final View v){
        SignupRequestBody signupRequestBody = new SignupRequestBody(roll, hostel, firstName,
                lastName, email, password);
        final Call<JSONObject> signup = client.signup(signupRequestBody);
        signup.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                v.clearAnimation();
                if (response.code() == 201) {
                    loginLinearLayout.setVisibility(View.VISIBLE);
                    signupLinearLayout.setVisibility(View.INVISIBLE);
                    loginLinearLayout.setAnimation(loginAnimation);
                    showSnackbar("Sign up successful", R.color.ksnack_success);
                } else if(response.code() == 409){
                    loginLinearLayout.setVisibility(View.VISIBLE);
                    signupLinearLayout.setVisibility(View.INVISIBLE);
                    loginLinearLayout.setAnimation(loginAnimation);
                    showSnackbar( "user already registered", R.color.ksnack_error);
                }else if(response.code() == 500){
                    showSnackbar("Internal error", R.color.ksnack_error);
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                v.clearAnimation();
                showSnackbar(t.getMessage(), R.color.ksnack_error);
            }
        });
    }

    private void sharePref(String roll, String pass) {
        SharedPreferences sp = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_ROLL, roll);
        editor.putString(KEY_PASS, pass);
        editor.apply();
    }

    private void setRollAndPass(){
        SharedPreferences sp = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        if(sp.contains(KEY_ROLL) && sp.contains(KEY_PASS)){
            rollET.setText(sp.getString(KEY_ROLL, ""));
            passwordET.setText(sp.getString(KEY_PASS, ""));
        }
    }

    private void initializeViews(){
        loginLinearLayout = this.findViewById(R.id.login_linear_Layout);
        signupLinearLayout = this.findViewById(R.id.signup_linear_Layout);
        rollET = this.findViewById(R.id.usernameET);
        passwordET = this.findViewById(R.id.passwordET);
        rollSingupET = this.findViewById(R.id.rollSignup);
        nameET = this.findViewById(R.id.nameSignup);
        lastnameET = this.findViewById(R.id.lnameSignup);
        emailET = this.findViewById(R.id.emailSignup);
        passwortSignupET = this.findViewById(R.id.passwordSignUp);
        hostelSpinner = this.findViewById(R.id.hostelSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Hostels, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hostelSpinner.setAdapter(adapter);
        hostelSpinner.setOnItemSelectedListener(this);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://digitalcafe.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        client = retrofit.create(ApiClient.class);
    }

    private void initilizeAnimations(){
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        rotateAnimation.setInterpolator(interpolator);
        loginAnimation = AnimationUtils.loadAnimation(this, R.anim.side_in);
    }


    private void checkLoginOrSignupViews() {
        int val = getIntent().getIntExtra("button", 1);
        if(val==1){
            loginLinearLayout.setVisibility(View.VISIBLE);
            signupLinearLayout.setVisibility(View.INVISIBLE);
        }else{
            signupLinearLayout.setVisibility(View.VISIBLE);
            loginLinearLayout.setVisibility(View.INVISIBLE);
        }
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        hostel = parent.getItemAtPosition(position).toString();
        hostel = hostel.substring(7);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}