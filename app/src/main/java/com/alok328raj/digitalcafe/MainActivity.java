package com.alok328raj.digitalcafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.alok328raj.digitalcafe.API.ApiClient;
import com.alok328raj.digitalcafe.API.Model.LoginResponse;
import com.alok328raj.digitalcafe.API.RequestBody.LoginRequestBody;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public void loginActivity(View v){

        Intent loginIntent = new Intent(this, Login.class);
        loginIntent.putExtra("button", 1);
        startActivity(loginIntent);
//        finish();
    }

    public void signupActivity(View v){
        Intent signUPIntent = new Intent(this, Login.class);
        signUPIntent.putExtra("button", 0);
        startActivity(signUPIntent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, 100);
        }

        try{
            SQLiteDatabase database = this.openOrCreateDatabase("TEST", MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS users(username VARCAHR, password VARCHAR, balance INT(6))");
            database.execSQL("INSERT INTO users (username, password, balance) VALUES ('alok', 'raj', 18000)");
            database.execSQL("INSERT INTO users (username, password, balance) VALUES ('test', 'test', 15000)");
            //String str = "UPDATE users SET balance = 10000 WHERE username = 'raj'";
            //database.execSQL(str);

        }catch (Exception e){
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }

    }
}
