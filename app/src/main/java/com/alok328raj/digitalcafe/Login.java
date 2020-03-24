package com.alok328raj.digitalcafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.libizo.CustomEditText;
import com.onurkagan.ksnack_lib.Animations.Slide;
import com.onurkagan.ksnack_lib.KSnack.KSnack;
import com.onurkagan.ksnack_lib.KSnack.KSnackBarEventListener;

public class Login extends AppCompatActivity {

    CustomEditText usernameET, passwordET;

    public void loginButton(View v){

        try{

            usernameET = findViewById(R.id.usernameET);
            passwordET = findViewById(R.id.passwordET);

            SQLiteDatabase database = this.openOrCreateDatabase("TEST", MODE_PRIVATE, null);
            String username = String.valueOf(usernameET.getText());
            String password = String.valueOf(passwordET.getText());

            Cursor c = database.rawQuery("SELECT * FROM users", null);

            int usernameIdx = c.getColumnIndex("username");
            int passwordIdx = c.getColumnIndex("password");
            int balanceIdx = c.getColumnIndex("balance");

            c.moveToFirst();
            while(c != null){
                if(c.getString(usernameIdx).equals(username)){
                    break;
                }else{
                    c.moveToNext();
                }
            }
            if(c == null){
                Toast.makeText(this, "User not registered", Toast.LENGTH_SHORT).show();
            }else if(c.getString(usernameIdx).equals(username)){
                if(c.getString(passwordIdx).equals(password)){
                    Toast.makeText(this, "login successful", Toast.LENGTH_SHORT).show();
                    Intent homeIntent = new Intent(this, Home.class);
                    int balance = c.getInt(balanceIdx);
                    System.out.println(balance);
                    homeIntent.putExtra("username", username);
                    homeIntent.putExtra("balance", balance);
                    startActivity(homeIntent);
                    finish();
                }else{
                    Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "User not registered!", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
