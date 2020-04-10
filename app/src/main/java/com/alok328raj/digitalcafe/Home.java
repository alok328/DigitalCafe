package com.alok328raj.digitalcafe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alok328raj.digitalcafe.API.ApiClient;
import com.alok328raj.digitalcafe.API.Model.BalanceResponse;
import com.alok328raj.digitalcafe.API.RequestBody.BalanceRequestBody;
import com.alok328raj.digitalcafe.API.RequestBody.TransactionsRequestBody;
import com.alok328raj.digitalcafe.Animation.MyBounceInterpolator;
import com.alok328raj.digitalcafe.StringToJson.QRScanConverter;
import com.blikoon.qrcodescanner.QrCodeActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onurkagan.ksnack_lib.Animations.Slide;
import com.onurkagan.ksnack_lib.KSnack.KSnack;
import com.onurkagan.ksnack_lib.KSnack.KSnackBarEventListener;
import com.pd.chocobar.ChocoBar;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity {

    private static final int REQUEST_CODE_QR_SCAN = 101;
    private final String LOGTAG = "QRResult";
    String username, roll;
    Intent intent;
    TextView usernameTextView;
    Retrofit retrofit;
    ApiClient client;
    android.view.animation.Animation rotateAnimation;
    View view;
    DecimalFormat form = new DecimalFormat("0.00");

    public void scanQRButton(View v){
        view = v;
        view.setAnimation(rotateAnimation);
        v.setAnimation(rotateAnimation);
        Intent scanIntent = new Intent(Home.this, QrCodeActivity.class);
        startActivityForResult( scanIntent,REQUEST_CODE_QR_SCAN);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        showWelcomeMessage();

        usernameTextView = findViewById(R.id.userTextView);

        intent = getIntent();
        username = intent.getStringExtra("username");
        roll = intent.getStringExtra("roll");

        usernameTextView.setText(Character.toUpperCase(username.charAt(0)) + username.substring(1) + "'s Dashboard");

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.103:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        client = retrofit.create(ApiClient.class);

        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.08, 25);
        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        rotateAnimation.setInterpolator(interpolator);

    }

    public void viewProfile(final View v){
        v.startAnimation(rotateAnimation);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                v.clearAnimation();
            }
        };
        timer.schedule(timerTask, 10000);
    }

    public void viewBalance(final View v){
//        Toast.makeText(this, roll, Toast.LENGTH_SHORT).show();
        v.startAnimation(rotateAnimation);
        Call<BalanceResponse> getBalance = client.getBalance(roll);
        getBalance.enqueue(new Callback<BalanceResponse>() {
            @Override
            public void onResponse(Call<BalanceResponse> call, Response<BalanceResponse> response) {
                v.clearAnimation();
                if(response.code() == 200){
                    showSnackbar("Balance : " + form.format(response.body().getBal()), R.color.ksnack_success);
                }else{
                    showSnackbar("Error!", R.color.ksnack_error);
                }
            }

            @Override
            public void onFailure(Call<BalanceResponse> call, Throwable t) {
                v.clearAnimation();
                showSnackbar(t.getMessage(), R.color.ksnack_error);
            }
        });
    }

    private void showWelcomeMessage() {
        KSnack kSnack = new KSnack(Home.this);
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
                .setMessage("Login Successful") // message
                .setTextColor(R.color.white) // message text color
                .setBackColor(R.color.ksnack_success) // background color
                .setAnimation(Slide.Up.getAnimation(kSnack.getSnackView()), Slide.Down.getAnimation(kSnack.getSnackView()))
                .setDuration(4000) // you can use for auto close.
                .show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode != Activity.RESULT_OK)
        {
            Log.d(LOGTAG,"COULD NOT GET A GOOD RESULT.");
            if(data==null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if( result!=null)
            {

                KSnack kSnack = new KSnack(Home.this);
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
                        .setMessage("Your message.") // message
                        .setTextColor(R.color.white) // message text color
                        .setBackColor(R.color.ksnack_error) // background color
                        .setAnimation(Slide.Up.getAnimation(kSnack.getSnackView()), Slide.Down.getAnimation(kSnack.getSnackView()))
                        .setDuration(4000) // you can use for auto close.
                        .show();
            }
            return;

        }
        if(requestCode == REQUEST_CODE_QR_SCAN)
        {
            if(data==null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");

            Gson gson = new Gson();
            final QRScanConverter jsonScanResult = gson.fromJson(result, QRScanConverter.class);

            final String menu = jsonScanResult.getMenu();
            final Float price = jsonScanResult.getPrice();

            new FancyAlertDialog.Builder(this)
                    .setTitle("QR Scanned Successfully")
                    .setBackgroundColor(Color.parseColor("#F7941E"))  //Don't pass R.color.colorvalue
                    .setMessage("Menu : " + menu + "\nPrice : Rs. " + form.format(price))
                    .setNegativeBtnText("Cancel")
                    .setPositiveBtnBackground(Color.parseColor("#F7941E"))  //Don't pass R.color.colorvalue
                    .setPositiveBtnText("Confirm")
                    .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))  //Don't pass R.color.colorvalue
                    .setAnimation(Animation.POP)
                    .isCancellable(true)
                    .setIcon(R.drawable.ic_local_cafe_black_24dp, Icon.Visible)
                    .OnPositiveClicked(new FancyAlertDialogListener() {
                        @Override
                        public void OnClick() {
                            TransactionsRequestBody requestBody = new TransactionsRequestBody(menu, price);
                            Call<JSONObject> addTransaction = client.addTransaction(roll, requestBody);
                            addTransaction.enqueue(new Callback<JSONObject>() {
                                @Override
                                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                                    if(response.code() == 201){
                                        try {
                                            showSnackbar("Done, deducted amount Rs. " + form.format(price), R.color.ksnack_success);
                                            view.clearAnimation();
                                        } catch (Exception e) {
                                            showSnackbar(e.getMessage(), R.color.ksnack_error);
                                        }
                                    }else {
                                        view.clearAnimation();
                                        showSnackbar("Server error", R.color.ksnack_error);
                                    }
                                }

                                @Override
                                public void onFailure(Call<JSONObject> call, Throwable t) {
                                    showSnackbar(t.getMessage(), R.color.ksnack_error);
                                }
                            });
                        }
                    })
                    .OnNegativeClicked(new FancyAlertDialogListener() {
                        @Override
                        public void OnClick() {
                            view.clearAnimation();
                            showSnackbar("Transaction cancelled", R.color.ksnack_error);
                        }
                    })
                    .build();
        }
    }

    public void showSnackbar(String message, int color){
        KSnack kSnack = new KSnack(Home.this);
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
