package com.example.lubna.eticketing.Survey;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lubna.eticketing.Others.MyProfile;
import com.example.lubna.eticketing.Others.PdfView;
import com.example.lubna.eticketing.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static android.content.ContentValues.TAG;

public class SurveyDetail extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 1;
    private TextView txtProgress, txtHead, tvtittle, tvnro, tvmanager, tvaddress, tv_site, tv_dealer, tv_insptdby, tvdate, tvstatus;
    FloatingActionButton fabLoc, fabStartSurvey, fabImgUpload;
    SharedPreferences sp;
    private Button btnDashboard, btnProfile,btnPdfView;
    Intent intent;
    String tittle = "";
    String site = "";
    String CID, RoleName,UserID,DepartID,ParentID;
    private Context context = this;
    SharedPreferences sharedp;
    ProgressDialog pDialog;
    private static final String TAG_RESULTS = "publish";
    String cond;
    ProgressBar progressBar;
    LinearLayout LayoutAction;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationManager locationManager;
    boolean isGPSEnabled = false;
    private String A;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_detail);

        btnPdfView = findViewById(R.id.btnPdfView);

        sharedp = getSharedPreferences(Survey_Login.Pre, Context.MODE_PRIVATE);
        RoleName = sharedp.getString("role_name", "");
        UserID = sharedp.getString("userid", "");
        DepartID = sharedp.getString("userdepartid", "");
        ParentID = sharedp.getString("parentid", "");

        intent = getIntent();
        CID = intent.getStringExtra("Id");
        A = intent.getStringExtra("A");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        LayoutAction = (LinearLayout) findViewById(R.id.LayoutAction);


        fabLoc = (FloatingActionButton) findViewById(R.id.fabLocation);
        fabLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fetchLocation();
            }
        });

        fabStartSurvey = (FloatingActionButton) findViewById(R.id.fabStartSurvey);

        fabImgUpload = (FloatingActionButton) findViewById(R.id.fabImgUpload);
        fabImgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                Intent intent = new Intent(getApplicationContext(),ImageUpload.class);
                intent.putExtra("SurveyID", CID);
                startActivity(intent);
            }
        });

        if (RoleName.equals("employee")) {
            fabLoc.setVisibility(View.VISIBLE);
            fabStartSurvey.setVisibility(View.VISIBLE);
            fabImgUpload.setVisibility(View.VISIBLE);
        } else {
            fabLoc.setVisibility(View.GONE);
            fabStartSurvey.setVisibility(View.GONE);
            fabImgUpload.setVisibility(View.GONE);
        }


        sp = getSharedPreferences(Survey_Login.Pre, Context.MODE_PRIVATE);
        sharedp = getSharedPreferences(Survey_Login.Pre, Context.MODE_PRIVATE);
        tittle = intent.getStringExtra("sury");
        tvtittle = (TextView) findViewById(R.id.tvtittle);
        tvnro = (TextView) findViewById(R.id.tvnro);
        tvmanager = (TextView) findViewById(R.id.tvmanger);
        tvaddress = (TextView) findViewById(R.id.tvadd);
        tv_site = (TextView) findViewById(R.id.tvsite);
        tv_dealer = (TextView) findViewById(R.id.tvdeal);
        tv_insptdby = (TextView) findViewById(R.id.tvinspctby);
        tvstatus = (TextView) findViewById(R.id.tvstatus);
        tvdate = (TextView) findViewById(R.id.tvdate);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtHead = (TextView) findViewById(R.id.txtHead);
        txtProgress = (TextView) findViewById(R.id.txtProgress);


        if (sharedp.getString("ROLE", "").equals("2")) {
            tvtittle.setText("Tittle: " + tittle);
            tv_insptdby.setText("Inspected By: " + sp.getString("NAME", ""));
            // PublicDetail();
            tv_dealer.setVisibility(View.GONE);
            tv_site.setVisibility(View.GONE);
            tvaddress.setVisibility(View.GONE);
            tvmanager.setVisibility(View.GONE);
            tvnro.setVisibility(View.GONE);
        } else {

            tvtittle.setText("Title: " + tittle);
            tv_insptdby.setText("Inspected By: " + sp.getString("NAME", ""));
            cond = intent.getStringExtra("status");
            SurveyList();
        }
        // cond=sp.getString("Status", "");

        btnDashboard = (Button) findViewById(R.id.btnDashboard);
        btnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallDashboard();
            }
        });
        btnProfile = (Button) findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Business();

                finish();
                startActivity(new Intent(SurveyDetail.this, MyProfile.class));
            }
        });

        fabStartSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(),TakeSurvey.class);
                intent.putExtra("SurveyID", CID);
                intent.putExtra("UserID", UserID);
                intent.putExtra("DeptID", DepartID);
                intent.putExtra("ParentID", ParentID);
                startActivity(intent);
            }
        });


        btnPdfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(getApplicationContext(),PdfView.class);
                intent.putExtra("SiteName",site);
                startActivity(intent);
            }
        });

    }

    public void SurveyList() {
        final AlertDialog pDialog = new SpotsDialog(this,R.style.CustomProgress);
        pDialog.show();
        pDialog.setCancelable(false);
        final String url = ("http://" + Survey_Login.IP + "/api/surveydetail/" + CID);
        //Toast.makeText(getApplicationContext(),url , Toast.LENGTH_SHORT).show();
        StringRequest req = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response != null) {
                            try {
                                pDialog.dismiss();
                                String condi2;
                                JSONObject json = new JSONObject(response);
                                JSONObject c = (JSONObject) json.getJSONObject("publish_survey");
                                tv_site.setText("Site Name: " + c.getString("site_name"));
                                site = c.getString("site_name");
                                tvaddress.setText("Location:" + c.getString("location"));
                                tv_dealer.setText("Dealer: " + c.getString("dealer_name"));
                                tvnro.setText("NRO Number: " + c.getString("nro_num"));
                                tvmanager.setText("Manager: " + c.getString("manager_name"));
                                condi2 = c.getString("status");
                                String exp = c.getString("expired");
                                String alert = c.getString("alert");
                                String warn = c.getString("warning");
                                if (condi2.equals("In Process")) {

                                    if (exp.equals("yes")) {
//                                        fabLoc.setVisibility(View.GONE);
//                                        fabStartSurvey.setVisibility(View.GONE);
//                                        fabImgUpload.setVisibility(View.GONE);
                                        progressBar.setProgress(0);
                                        txtProgress.setText("Expired");
                                        txtProgress.setTextColor(Color.WHITE);
                                        txtHead.setText("Pending Survey Detail");
                                        tvdate.setText("Created Date: " + c.getString("created_at"));
                                        tvstatus.setTextColor(Color.parseColor("#ff669900"));
                                        LayoutAction.setVisibility(View.VISIBLE);
                                    } else if (alert.equals("yes")) {
                                        /*fabLoc.setVisibility(View.GONE);
                                        fabStartSurvey.setVisibility(View.GONE);
                                        fabImgUpload.setVisibility(View.GONE);*/
                                        progressBar.setProgress(0);
                                        txtProgress.setText("Alert");
                                        txtProgress.setTextColor(Color.WHITE);
                                        txtHead.setText("Pending Survey Detail");
                                        tvdate.setText("Created Date: " + c.getString("created_at"));
                                        tvstatus.setTextColor(Color.parseColor("#ff669900"));
                                        LayoutAction.setVisibility(View.VISIBLE);
                                    } else if (warn.equals("yes")) {
                                        /*fabLoc.setVisibility(View.GONE);
                                        fabStartSurvey.setVisibility(View.GONE);
                                        fabImgUpload.setVisibility(View.GONE);*/
                                        progressBar.setProgress(0);
                                        txtProgress.setText("Warning");
                                        txtProgress.setTextColor(Color.WHITE);
                                        txtHead.setText("Pending Survey Detail");
                                        tvdate.setText("Created Date: " + c.getString("created_at"));
                                        tvstatus.setTextColor(Color.parseColor("#ff669900"));
                                        LayoutAction.setVisibility(View.VISIBLE);
                                    } else {
                                        progressBar.setProgress(0);
                                        txtProgress.setText("Incomplete");
                                        txtProgress.setTextColor(Color.WHITE);
                                        txtHead.setText("Pending Survey Detail");
                                        tvdate.setText("Created Date: " + c.getString("created_at"));
                                        tvstatus.setTextColor(Color.parseColor("#ff669900"));
                                        LayoutAction.setVisibility(View.VISIBLE);
                                    }

                                } else if (condi2.equals("Completed")) {
                                    progressBar.setProgress(100);
                                    txtProgress.setText("Complete");
                                    txtProgress.setTextColor(Color.WHITE);
                                    txtHead.setText("Complete Survey Detail");
                                    tvdate.setText("Submit Date: " + c.getString("updated_at"));
                                    tvstatus.setTextColor(Color.parseColor("#ff669900"));
                                    //LayoutAction.setVisibility(View.GONE);
                                    fabLoc.setVisibility(View.GONE);
                                    fabStartSurvey.setVisibility(View.GONE);
                                    fabImgUpload.setVisibility(View.GONE);
                                    btnPdfView.setVisibility(View.VISIBLE);
                                }

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
                        Toast.makeText(SurveyDetail.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);


    }

    public void PublicDetail() {
        final String url = "http://survey.petrotechhbsl.com.pk/api/getpublish/" + sharedp.getString("userid", "");
        StringRequest req = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response != null) {
                            try {
                                String condi2;
                                // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                JSONObject json = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                                JSONArray contacts = json.getJSONArray("surveylist");
                                String status, valid;
                                for (int i = 0; i < contacts.length(); i++) {
                                    JSONObject c = contacts.getJSONObject(i);
                                    JSONObject obj1 = c.getJSONObject("detail");
                                    status = obj1.getString("status");
                                    valid = obj1.getString("valid_till");
                                    tvstatus.setText("   Status: " + status);
                                    tvdate.setText("   Valid Till: " + valid);
                                    tvstatus.setTextColor(Color.parseColor("#0091EA"));
                                    //  Toast.makeText(getApplicationContext(),  status.toString(), Toast.LENGTH_SHORT).show();

                                }


                                //  Toast.makeText(getApplicationContext(),  survey, Toast.LENGTH_SHORT).show();


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
                        Toast.makeText(SurveyDetail.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, DashboardSurvey.class);
        intent.putExtra("A",A);
        startActivity(intent);
        finish();
    }

    private void CallDashboard() {
        Intent intent = new Intent(this, DashboardSurvey.class);
        startActivity(intent);
        finish();
    }

    private void fetchLocation() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(SurveyDetail.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SurveyDetail.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(this)
                        .setTitle("Required Location Permission")
                        .setMessage("You have to give this permission to access the location")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(SurveyDetail.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_COARSE_LOCATION);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(SurveyDetail.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_COARSE_LOCATION);
                checkGPSorNetwork();
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();

                                final String lat = String.valueOf(latitude);
                                final String lng = String.valueOf(longitude);

                                final String url = "http://"+Survey_Login.IP+"/api/employee-acknowledge";

                                StringRequest req = new StringRequest(Request.Method.POST, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    String msg = jsonObject.getString("msg");
                                                    if (msg.equals("success"))
                                                    {
                                                        Toast.makeText(getApplicationContext(), "Data Sent", Toast.LENGTH_LONG).show();
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("employee_id", UserID);
                                        map.put("department_id", DepartID);
                                        map.put("latitude", lat);
                                        map.put("longitude", lng);
                                        map.put("department_parent_id", ParentID);
                                        map.put("publish_survey_id", CID);
                                        return map;
                                    }
                                };

                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                requestQueue.add(req);

                                //Toast.makeText(getApplicationContext(), "Latitude: " + String.valueOf(latitude) + "\n" + "Longitude: " + String.valueOf(longitude), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "GPS is not able to dectect your coordinates", Toast.LENGTH_LONG).show();
                            }
                        }
                    });


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_COARSE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

            }
        }
    }

    public void checkGPSorNetwork() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        isGPSEnabled = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);

        String gpsMsg = "GPS is disabled in your device.\nWould you like to enable it?";

        if (!isGPSEnabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Use Location?");
            dialog.setMessage(gpsMsg);

            dialog.setPositiveButton("GPS Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Intent gpsintent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(gpsintent);
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            dialog.show();
        }

    }

}