package com.alok328raj.digitalcafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alok328raj.digitalcafe.API.Item;
import com.alok328raj.digitalcafe.API.ItemModel;
import com.alok328raj.digitalcafe.API.ApiClient;
import com.alok328raj.digitalcafe.API.PostBody;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public void loginActivity(View v){


        //retrofitTest
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiClient client = retrofit.create(ApiClient.class);
        Call<Item> call = client.createItem("banana", new PostBody(119.99));
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                Log.d("LogOutput", response.body().getName() + " " + response.body().getPrice());
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Log.d("logError", t.getMessage());
            }
        });
        //retrofit

//        Intent loginIntent = new Intent(this, Login.class);
//        startActivity(loginIntent);
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
