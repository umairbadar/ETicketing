package com.example.lubna.eticketing.Ticketing.Quotation;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lubna.eticketing.R;
import com.example.lubna.eticketing.Survey.Survey_Login;

import org.json.JSONException;
import org.json.JSONObject;

import dmax.dialog.SpotsDialog;

public class QuotationDashboard extends AppCompatActivity {

    private TextView txtRecomm,txtApp,txtDec,txtPen;
    private String DepartID,UserID;
    private String TAG = QuotationDashboard.class.getSimpleName();
    private LinearLayout Recomm,App,Dec,Pen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation_dashboard);

        txtRecomm = (TextView) findViewById(R.id.txtRecomm);
        txtApp = (TextView) findViewById(R.id.txtApp);
        txtDec = (TextView) findViewById(R.id.txtDec);
        txtPen = (TextView) findViewById(R.id.txtPen);

        Intent intent = getIntent();
        DepartID = intent.getStringExtra("DepartID");
        UserID = intent.getStringExtra("UserID");

        Recomm = (LinearLayout) findViewById(R.id.layoutRecomm);
        App = (LinearLayout) findViewById(R.id.layoutApp);
        Dec = (LinearLayout) findViewById(R.id.layoutDec);
        Pen = (LinearLayout) findViewById(R.id.layoutPen);

        Recomm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(QuotationDashboard.this,QuotationListing.class);
                intent1.putExtra("A","Recommended");
                intent1.putExtra("userid",UserID);
                startActivity(intent1);
                finish();
            }
        });

        App.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(QuotationDashboard.this,QuotationListing.class);
                intent1.putExtra("A","Approved");
                intent1.putExtra("userid",UserID);
                startActivity(intent1);
                finish();
            }
        });

        Dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(QuotationDashboard.this,QuotationListing.class);
                intent1.putExtra("A","Declined");
                intent1.putExtra("userid",UserID);
                startActivity(intent1);
                finish();
            }
        });

        Pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(QuotationDashboard.this,QuotationListing.class);
                intent1.putExtra("A","Pending");
                intent1.putExtra("userid",UserID);
                startActivity(intent1);
                finish();
            }
        });

        getCount();
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(QuotationDashboard.this,QuotationDepartments.class));
    }

    public void getCount()
    {
        final AlertDialog progressDialog = new SpotsDialog(this,R.style.CustomProgress);
        progressDialog.show();
        progressDialog.setCancelable(false);

        final String url = "http://"+Survey_Login.IP+"/api/count-quotations/"+UserID;

        StringRequest req = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response != null)
                        {
                            try {
                                progressDialog.dismiss();
                                JSONObject jsonObject = new JSONObject(response);
                                String pending = jsonObject.getString("pending_quotations");
                                String approved = jsonObject.getString("approved_quotations");
                                String declined = jsonObject.getString("declined_quotations");
                                String recommended = jsonObject.getString("recommended_quotations");

                                txtApp.setText(approved);
                                txtDec.setText(declined);
                                txtRecomm.setText(recommended);
                                txtPen.setText(pending);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
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
                        Toast.makeText(QuotationDashboard.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
    }
}
