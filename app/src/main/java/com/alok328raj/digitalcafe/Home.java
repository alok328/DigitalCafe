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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.onurkagan.ksnack_lib.Animations.Slide;
import com.onurkagan.ksnack_lib.KSnack.KSnack;
import com.onurkagan.ksnack_lib.KSnack.KSnackBarEventListener;
import com.pd.chocobar.ChocoBar;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

public class Home extends AppCompatActivity {

    private static final int REQUEST_CODE_QR_SCAN = 101;
    private final String LOGTAG = "QRResult";
    String username;
    int balance;
    Intent intent;
    TextView usernameTextView;

    public void scanQRButton(View v){
        Intent scanIntent = new Intent(Home.this, QrCodeActivity.class);
        startActivityForResult( scanIntent,REQUEST_CODE_QR_SCAN);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        showWelcomeMessage();

//        usernameTextView = findViewById(R.id.userTextView);
//
//        intent = getIntent();
//        username = intent.getStringExtra("username");
//        balance = intent.getIntExtra("balance", -1000);
//
//        usernameTextView.setText("Welcome " + username);
//
        ImageButton viewBalanceButton = findViewById(R.id.viewBalButton);
        viewBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this, "Balance -> Rs. " + String.valueOf(balance), Toast.LENGTH_SHORT).show();
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

            new FancyAlertDialog.Builder(this)
                    .setTitle("QR Scanned Successfully")
                    .setBackgroundColor(Color.parseColor("#F7941E"))  //Don't pass R.color.colorvalue
                    .setMessage(result)
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
                                    .setMessage("Transaction Done") // message
                                    .setTextColor(R.color.white) // message text color
                                    .setBackColor(R.color.ksnack_success) // background color
                                    .setAnimation(Slide.Up.getAnimation(kSnack.getSnackView()), Slide.Down.getAnimation(kSnack.getSnackView()))
                                    .setDuration(4000) // you can use for auto close.
                                    .show();
                        }
                    })
                    .OnNegativeClicked(new FancyAlertDialogListener() {
                        @Override
                        public void OnClick() {
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
                                    .setMessage("Transaction Cancelled") // message
                                    .setTextColor(R.color.white) // message text color
                                    .setBackColor(R.color.ksnack_error) // background color
                                    .setAnimation(Slide.Up.getAnimation(kSnack.getSnackView()), Slide.Down.getAnimation(kSnack.getSnackView()))
                                    .setDuration(4000) // you can use for auto close.
                                    .show();
                        }
                    })
                    .build();
        }
    }
}
