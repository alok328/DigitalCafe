package com.alok328raj.digitalcafe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alok328raj.digitalcafe.API.ApiClient;
import com.alok328raj.digitalcafe.API.Model.BalanceResponse;
import com.alok328raj.digitalcafe.API.Model.ProfileResponse;
import com.alok328raj.digitalcafe.API.Model.TransactionResponse;
import com.alok328raj.digitalcafe.API.Model.transaction.Transaction;
import com.alok328raj.digitalcafe.API.RequestBody.BalanceRequestBody;
import com.alok328raj.digitalcafe.API.RequestBody.TransactionsRequestBody;
import com.alok328raj.digitalcafe.Adapter.HeaderItem;
import com.alok328raj.digitalcafe.Adapter.ListItem;
import com.alok328raj.digitalcafe.Adapter.TransactionItem;
import com.alok328raj.digitalcafe.Adapter.TransactionsAdapter;
import com.alok328raj.digitalcafe.Animation.MyBounceInterpolator;
import com.alok328raj.digitalcafe.StringToJson.QRScanConverter;
import com.blikoon.qrcodescanner.QrCodeActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onurkagan.ksnack_lib.Animations.Fade;
import com.onurkagan.ksnack_lib.Animations.Slide;
import com.onurkagan.ksnack_lib.KSnack.KSnack;
import com.onurkagan.ksnack_lib.KSnack.KSnackBarEventListener;
import com.pd.chocobar.ChocoBar;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

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
    android.view.animation.Animation rotateAnimation, animation;
    View view;
    DecimalFormat form = new DecimalFormat("0.00");
    LinearLayout dashboardLinearLayout, transactionLinearLayout;
    Boolean showTransaction = false;
    int backCount = 0;

    public void scanQRButton(View v){
        view = v;
        view.setAnimation(rotateAnimation);
        v.setAnimation(rotateAnimation);
        Intent scanIntent = new Intent(Home.this, QrCodeActivity.class);
        startActivityForResult( scanIntent,REQUEST_CODE_QR_SCAN);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        showSnackbar("Login successful", R.color.ksnack_success);

        usernameTextView = findViewById(R.id.userTextView);

        intent = getIntent();
        username = intent.getStringExtra("username");
        roll = intent.getStringExtra("roll");

        usernameTextView.setText(Character.toUpperCase(username.charAt(0)) + username.substring(1) + "'s Dashboard");

        retrofit = new Retrofit.Builder()
                .baseUrl("https://digital-cafe.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        client = retrofit.create(ApiClient.class);

        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.08, 25);
        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        rotateAnimation.setInterpolator(interpolator);

        animation = AnimationUtils.loadAnimation(this, R.anim.side_in);

        dashboardLinearLayout = this.findViewById(R.id.dashboardLinearLayout);
        transactionLinearLayout = this.findViewById(R.id.transactionLinearLayout);

    }

    public void viewProfile(final View v){
        v.startAnimation(rotateAnimation);
        Call<ProfileResponse> getProfile = client.getProfile(roll);
        getProfile.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    Intent profileIntent = new Intent(Home.this, Profile.class);
                    profileIntent.putExtra("fname", response.body().getFirstName());
                    profileIntent.putExtra("lname", response.body().getLastName());
                    profileIntent.putExtra("email", response.body().getEmail());
                    profileIntent.putExtra("roll", response.body().getRoll());
                    profileIntent.putExtra("bal", response.body().getBalance());
                    profileIntent.putExtra("hostel", response.body().getHostel());
                    v.clearAnimation();
                    startActivity(profileIntent);
                }else{
                    v.clearAnimation();
                    showSnackbar("user not found", R.color.ksnack_error);
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                v.clearAnimation();
                showSnackbar(t.getMessage(), R.color.ksnack_error);
            }
        });
    }

    public void viewMenu(View v){
        Intent menuIntent = new Intent(this, MenuActivity.class);
        startActivity(menuIntent);
    }

    public void viewTransactions(final View v) {
        v.startAnimation(rotateAnimation);
        Call<List<Transaction>> getTransaction = client.getTransaction(roll);
        @NonNull final List<ListItem> items = new ArrayList<>();
        getTransaction.enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                List<Transaction> list = new ArrayList<>();
                SimpleDateFormat spf = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                for(Transaction transaction : response.body()){
                    Date date = transaction.getDate();
                    String res = spf.format(date);
                    try {
                        Date newDate=spf.parse(res);
                        String id = transaction.get_id();
                        String menu = transaction.getMenu();
                        Float price = transaction.getPrice();
                        Float balance = 0.0F;
                        try{
                           balance = transaction.getBalance();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        list.add(new Transaction(newDate, id, menu, price, balance));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                Map<Date, List<Transaction>> transactions = toMap(list);
                ArrayList<Date> keys = new ArrayList<Date>(transactions.keySet());
                for(int i=keys.size()-1; i>=0;i--){
                    HeaderItem headerItem = new HeaderItem(keys.get(i));
                    items.add(headerItem);
                    List<Transaction> temp = transactions.get(keys.get(i));
                    for(int j = Objects.requireNonNull(temp).size()-1; j>=0; j--){
                        TransactionItem transactionItem = new TransactionItem(temp.get(j));
                        items.add(transactionItem);
                    }
                }


//                v.clearAnimation();
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lst_items);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(new TransactionsAdapter(items));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.clearAnimation();
                        transactionLinearLayout.setVisibility(View.VISIBLE);
                        dashboardLinearLayout.setVisibility(View.INVISIBLE);
                        showTransaction = true;
                    }
                }, 2500);
            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {
                v.clearAnimation();
                showSnackbar("error", R.color.ksnack_error);
            }
        });
    }

    @NonNull
    private Map<Date, List<Transaction>> toMap(@NonNull List<Transaction> transactions) {
        Map<Date, List<Transaction>> map =new TreeMap<>();
        for(Transaction transaction: transactions){
            List<Transaction> value = map.get(transaction.getDate());
            if(value == null){
                value = new ArrayList<>();
                map.put(transaction.getDate(), value);
            }
            value.add(transaction);
        }
        return map;
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
                    showSnackbar("Balance : Rs. " + form.format(response.body().getBal()), R.color.ksnack_success);
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

    @Override
    public void onBackPressed() {

        if(showTransaction){
            showTransaction = false;
            backCount = 0;
            dashboardLinearLayout.setVisibility(View.VISIBLE);
            transactionLinearLayout.setVisibility(View.INVISIBLE);
        }else{
            backCount++;
            Toast.makeText(this, "Back again to log out!", Toast.LENGTH_SHORT).show();
        }
        if (backCount >= 2) {
            super.onBackPressed();
            return;
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                backCount=0;
            }
        }, 2000);

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
            final String hostel = jsonScanResult.getHostel();

            new FancyAlertDialog.Builder(this)
                    .setTitle("QR Scanned Successfully")
                    .setBackgroundColor(Color.parseColor("#F7941E"))  //Don't pass R.color.colorvalue
                    .setMessage("Hostel : " + hostel + "\nMenu : " + menu + "\nPrice : Rs. " + form.format(price))
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
                            TransactionsRequestBody requestBody = new TransactionsRequestBody(menu, price, hostel);
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
                                    }else if(response.code() == 400) {
                                        view.clearAnimation();
                                        showSnackbar("Insufficient balance", R.color.ksnack_error);
                                    }else if(response.code()==401){
                                        view.clearAnimation();
                                        showSnackbar("This is not your hostel!", R.color.ksnack_error);
                                    }else{
                                        view.clearAnimation();;
                                        showSnackbar("Server error, Please try again", R.color.ksnack_error);
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