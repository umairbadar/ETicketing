package com.example.lubna.eticketing.Others;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.lubna.eticketing.R;
import com.example.lubna.eticketing.Survey.DashboardSurvey;
import com.example.lubna.eticketing.Ticketing.Dashboard;


public class MainMenu extends AppCompatActivity {

    private LinearLayout Eticket,Business,Layout1;
    private boolean doubleBackToExitPressedOnce = false;
    private RelativeLayout mainLayout,Layout2;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Layout1 = findViewById(R.id.DataLayout);
        Layout2 = findViewById(R.id.ErrorLayout);

        Eticket = (LinearLayout) findViewById(R.id.btneticket);
        Business = (LinearLayout) findViewById(R.id.btnbusiness);
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);

        isNetworkAvailable();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null, otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {

            Business.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), DashboardSurvey.class);
                    startActivity(i);
                    finish();
                }
            });
            Eticket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), Dashboard.class);
                    startActivity(i);
                    finish();
                }
            });
            return true;
        }
        else if(networkInfo == null)
        {
            /*Toast.makeText(MainMenu.this,"No internet Connection",Toast.LENGTH_LONG).show();
            Snackbar snackbar = Snackbar
                    .make(mainLayout, "No Internet Connection", Snackbar.LENGTH_LONG);

            snackbar.show();*/

            Layout1.setVisibility(View.GONE);
            Layout2.setVisibility(View.VISIBLE);
            return false;
        }
        return false;

    }

}

