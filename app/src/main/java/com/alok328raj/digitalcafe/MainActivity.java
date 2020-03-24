package com.alok328raj.digitalcafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public void loginActivity(View v){
        Intent loginIntent = new Intent(this, Login.class);
        startActivity(loginIntent);
        //finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
