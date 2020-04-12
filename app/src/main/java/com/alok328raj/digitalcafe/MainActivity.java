package com.alok328raj.digitalcafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import com.alok328raj.digitalcafe.API.ApiClient;
import com.alok328raj.digitalcafe.API.Model.LoginResponse;
import com.alok328raj.digitalcafe.API.RequestBody.LoginRequestBody;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public void loginActivity(View v){

        Intent loginIntent = new Intent(this, Login.class);
        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(v, 0,
                0, v.getWidth(), v.getHeight());
        loginIntent.putExtra("button", 1);
        startActivity(loginIntent, options.toBundle());
    }

    public void signupActivity(View v){
        Intent signUPIntent = new Intent(this, Login.class);
        signUPIntent.putExtra("button", 0);
        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(v, 0,
                0, v.getWidth(), v.getHeight());
        startActivity(signUPIntent, options.toBundle());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, 100);
        }
    }
}
