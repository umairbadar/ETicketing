package com.example.lubna.eticketing.Survey;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lubna.eticketing.Others.MainMenu;
import com.example.lubna.eticketing.Others.MyProfile;
import com.example.lubna.eticketing.R;
import com.example.lubna.eticketing.Ticketing.Dashboard;

import org.json.JSONException;
import org.json.JSONObject;

import dmax.dialog.SpotsDialog;
import me.leolin.shortcutbadger.ShortcutBadger;

import static android.content.ContentValues.TAG;

public class DashboardSurvey extends Activity {
    SharedPreferences sharedp;
    TextView tvincompelet,tvcompelet,tvalrt,tvwarning,tvexpird;
    public static final  String Pre = "MyPre";
    private String UserID,RoleName,DID;
    private ScrollView mainLayout;
    private LinearLayout assigned,expired,Completesurvey,Alerts,Warning,Layout1;
    private RelativeLayout Layout2;
    private TextView Error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_survey);

        Error = findViewById(R.id.textViewError);

        Layout1 = findViewById(R.id.DataLayout);
        Layout2 = findViewById(R.id.ErrorLayout);

        mainLayout = findViewById(R.id.mainLayout);

        sharedp = getSharedPreferences(Survey_Login.Pre, Context.MODE_PRIVATE);
        UserID = sharedp.getString("userid","");
        RoleName = sharedp.getString("role_name","");
        /*DID = sharedp.getString("parentid","");

        Toast.makeText(getApplicationContext(),DID,Toast.LENGTH_LONG).show();*/

        tvincompelet=(TextView)findViewById(R.id.incompeletetxt);
        tvcompelet=(TextView)findViewById(R.id.compeletetxt);
        tvexpird=(TextView)findViewById(R.id.expiredtxt);
        tvalrt=(TextView)findViewById(R.id.alrttxt);
        tvwarning=(TextView)findViewById(R.id.txtwarning);



        Button refresh= (Button) findViewById(R.id.btnrefresh);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
        Button logout= (Button) findViewById(R.id.btnlogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref =  getSharedPreferences(Pre, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.apply();
                finish();
                startActivity(new Intent(DashboardSurvey.this,Survey_Login.class));
            }
        });
        Button profile= (Button) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MyProfile.class);
                startActivity(i);
                finish();
            }
        });
        assigned = (LinearLayout) findViewById(R.id.btnincomp);
        assigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardSurvey.this,SurveyList.class);
                i.putExtra("A","Assignedsurvey");
                startActivity(i);
                finish();
            }
        });
        expired= (LinearLayout) findViewById(R.id.btnexpired);
        expired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SurveyList.class);
                i.putExtra("A","expired");
                startActivity(i);
                finish();
            }
        });
        Completesurvey= (LinearLayout) findViewById(R.id.btncomp);
        Completesurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SurveyList.class);
                i.putExtra("A","completesurvey");
                startActivity(i);
                finish();
            }
        });
        Alerts= (LinearLayout) findViewById(R.id.btnalerts);
        Alerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SurveyList.class);
                i.putExtra("A","Alerts");
                startActivity(i);
                finish();
            }
        });
        Warning= (LinearLayout) findViewById(R.id.btnwarning);
        Warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SurveyList.class);
                i.putExtra("A","warning");
                startActivity(i);
                finish();
            }
        });

        isNetworkAvailable();

    }
    @Override
    public void onBackPressed () {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
        finish();
    }
    private void refresh() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
    public void surveys() {
        final AlertDialog progressDialog = new SpotsDialog(this,R.style.CustomProgress);
        progressDialog.show();
        progressDialog.setCancelable(false);
        final String url = "http://"+Survey_Login.IP+"/api/all-counts/"+UserID;
        StringRequest req = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response != null) {
                            progressDialog.dismiss();
                            try {

                                JSONObject jsonObj = new JSONObject(response);
                                tvincompelet.setText(jsonObj.getString("total_incomplete_surveys"));
                                tvcompelet.setText(jsonObj.getString("total_complete_surveys"));
                                tvexpird.setText(jsonObj.getString("total_expired"));
                                tvalrt.setText(jsonObj.getString("total_alerts"));
                                tvwarning.setText(jsonObj.getString("total_warnings"));


                                int incomp = jsonObj.getInt("total_incomplete_surveys");
                                int warn = jsonObj.getInt("total_warnings");
                                int alert = jsonObj.getInt("total_alerts");

                                int totalCount = incomp + warn + alert;
                                ShortcutBadger.applyCount(DashboardSurvey.this, totalCount); //for 1.1.4+

                            } catch (final JSONException e) {
                                Log.e(TAG, "Json parsing error: " + e.getMessage());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),
                                                "Json parsing error: " + e.getMessage(),
                                                Toast.LENGTH_LONG)
                                                .show();
                                    }
                                });

                            }
                        } else {
                            progressDialog.dismiss();
                            Log.e(TAG, "Couldn't get json from server.");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            "Couldn't get json from server. Check LogCat for possible errors!",
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            });

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Layout1.setVisibility(View.GONE);
                        Layout2.setVisibility(View.VISIBLE);

                            /*Snackbar snackbar = Snackbar
                                    .make(mainLayout, "Server isn't working. Please try again later.", Snackbar.LENGTH_LONG);

                            snackbar.show();*/

                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);


    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null, otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {

            if (RoleName.equals("reporting manager"))
            {
                tvincompelet.setText("Click here");
                tvcompelet.setText("Click here");
                tvexpird.setText("Click here");
                tvalrt.setText("Click here");
                tvwarning.setText("Click here");
            }
            else
            {
                surveys();
            }
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
