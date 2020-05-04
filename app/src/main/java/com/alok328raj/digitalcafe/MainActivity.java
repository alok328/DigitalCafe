package com.alok328raj.digitalcafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Request user permissions for camera access in the app
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, 100);
        }
    }

    //Start login activity
    public void loginActivity(View v){

        Intent loginIntent = new Intent(this, Login.class);
        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(v, 0,
                0, v.getWidth(), v.getHeight());
        loginIntent.putExtra("button", 1);
        startActivity(loginIntent, options.toBundle());
    }

    //Start sign up activity
    public void signupActivity(View v){
        Intent signUPIntent = new Intent(this, Login.class);
        signUPIntent.putExtra("button", 0);
        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(v, 0,
                0, v.getWidth(), v.getHeight());
        startActivity(signUPIntent, options.toBundle());
    }

}
