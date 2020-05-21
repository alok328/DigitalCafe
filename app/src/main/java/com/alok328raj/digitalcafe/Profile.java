package com.alok328raj.digitalcafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Float.valueOf;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String firstName = this.getIntent().getStringExtra("fname");
        String lastName = this.getIntent().getStringExtra("lname");
        String email = this.getIntent().getStringExtra("email");
        String roll= this.getIntent().getStringExtra("roll");
        String bal = this.getIntent().getStringExtra("bal");
        String hostel= this.getIntent().getStringExtra("hostel");

        Float baln = valueOf(bal);

        TextView nameText, emailText, rollText, hostelText, balanceText;
        nameText = findViewById(R.id.nameTV);
        emailText = findViewById(R.id.emailTV);
        rollText = findViewById(R.id.rollTV);
        balanceText = findViewById(R.id.balanceTV);
        hostelText = findViewById(R.id.hostelTV);

        nameText.setText(String.format("%s %s", firstName, lastName));
        emailText.setText(email);
        rollText.setText(roll);
        balanceText.setText(String.format("  Rs. %1.2f", baln));
        hostelText.setText(String.format("Hostel %s", hostel));

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.oneDark));
        }
    }

    public void goBack(View v){
        finish();
    }
}
