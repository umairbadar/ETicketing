package com.example.lubna.eticketing.Ticketing.Quotation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.UnicodeSetSpanner;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lubna.eticketing.R;
import com.example.lubna.eticketing.Survey.Survey_Login;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class QuotationDetails extends AppCompatActivity {

    private Button btnDashboard,btnLogout;
    public static final String KEY_ID = "id";
    public static final String KEY_REPLY = "reply";
    public String REPLY_VALUE;
    private String Q_ID,QID,RoleName,ParentID,ID,RoleID,UserID,Email;
    private TextView txtID,txtSender,txtSite,txtBody,txtDate,txtStatus,txtReply;
    private FloatingActionButton fabRecomm,fabApp,fabDec;
    private SharedPreferences sharedp;
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation_details);

        sharedp = getSharedPreferences(Survey_Login.Pre, Context.MODE_PRIVATE);
        RoleName = sharedp.getString("role_name","");
        ParentID = sharedp.getString("parentid","");
        Email = sharedp.getString("email","");

        //Toast.makeText(getApplicationContext(),ParentID,Toast.LENGTH_LONG).show();

        Intent intent = getIntent();
        QID = intent.getStringExtra("QID");
        Q_ID = intent.getStringExtra("ID");
        UserID = intent.getStringExtra("UserID");

        //Toast.makeText(getApplicationContext(),Q_ID,Toast.LENGTH_LONG).show();

        txtID = findViewById(R.id.txtID);
        txtSender = findViewById(R.id.txtSender);
        txtSite = findViewById(R.id.txtSite);
        txtBody = findViewById(R.id.txtBody);
        txtDate = findViewById(R.id.txtDate);
        txtStatus = findViewById(R.id.txtStatus);
        txtReply = findViewById(R.id.txtReply);

        fabRecomm = findViewById(R.id.fabRecomm);
        fabApp = findViewById(R.id.fabApproved);
        fabDec = findViewById(R.id.fabDeclined);

        fabRecomm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuotationRecommended();
            }
        });

        fabApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(QuotationDetails.this);
                alert.setTitle("Approve Quotation");
                //alert.setMessage("Enter Reply:");

                input = new EditText(QuotationDetails.this);
                alert.setView(input);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        QuotationApproved();
                        QuotationApproved_ALi();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert.show();



            }
        });
        fabDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(QuotationDetails.this);
                alert.setTitle("Decline Quotation");
                //alert.setMessage("Enter Reply:");

                input = new EditText(QuotationDetails.this);
                alert.setView(input);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        QuotationDecline();
                        QuotationDecline_ALI();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert.show();
            }
        });

        btnDashboard = findViewById(R.id.btnDashboard);
        btnLogout = findViewById(R.id.btnlogout);

        btnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                Intent intent = new Intent(QuotationDetails.this,QuotationDashboard.class);
                intent.putExtra("UserID",UserID);
                startActivity(intent);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences pref =  getSharedPreferences(Survey_Login.Pre, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.apply();
                finish();
                startActivity(new Intent(QuotationDetails.this,Survey_Login.class));
            }
        });
        getQuotationDetails();
    }

    @Override
    public void onBackPressed() {
        finish();
        //startActivity(new Intent(QuotationDetails.this,QuotationDashboard.class));
        Intent intent = new Intent(getApplicationContext(),QuotationDashboard.class);
        startActivity(intent);
    }

    public void getQuotationDetails()
    {
        final String url = ("http://"+Survey_Login.IP+"/api/get-quotation-detail/"+QID);
        StringRequest req = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response != null)
                        {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject innerObject = jsonObject.getJSONObject("quotation");
                                ID = innerObject.getString("id");
                                String sender = innerObject.getString("sender");
                                String site = innerObject.getString("site");
                                String body = innerObject.getString("body");
                                String Qdate = innerObject.getString("date");
                                String status = innerObject.getString("status");
                                String reply = innerObject.getString("reply");

                                if (RoleName.equals("reporting manager") && status.equals("recommended"))
                                {
                                    fabRecomm.setVisibility(View.GONE);
                                }
                                if (RoleName.equals("manager") && status.equals("pending"))
                                {
                                    fabRecomm.setVisibility(View.VISIBLE);
                                    fabApp.setVisibility(View.VISIBLE);
                                    fabDec.setVisibility(View.VISIBLE);
                                }
                                if (RoleName.equals("manager") && (status.equals("approved") || status.equals("declined") ||
                                        status.equals("recommended")))
                                {
                                    fabRecomm.setVisibility(View.GONE);
                                    fabApp.setVisibility(View.GONE);
                                    fabDec.setVisibility(View.GONE);
                                }
                                txtID.setText("Quotation ID: "+ID);
                                txtSender.setText("Sender: "+sender);
                                txtSite.setText("Site: "+site);
                                txtBody.setText("Description: "+body);
                                txtDate.setText("Quotation Date: "+Qdate);
                                txtStatus.setText("Status: "+status);
                                txtReply.setText("Reply: "+reply);

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

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
    }

    public void QuotationRecommended()
    {
        new AlertDialog.Builder(this)
                .setTitle("Are You Sure?")
                .setMessage("You want to recommend this quotation to your Reporting Manager.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String url = "http://"+Survey_Login.IP+"/api/recommend-quotation/"+ID+"/"+ParentID;

                        StringRequest req = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String msg = jsonObject.getString("msg");
                                            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                                            finish();
                                            Intent intent = new Intent(QuotationDetails.this,QuotationDashboard.class);
                                            intent.putExtra("UserID",UserID);
                                            startActivity(intent);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });



                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        requestQueue.add(req);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();
    }

    public void QuotationApproved()
    {
                final String url = "http://"+Survey_Login.IP+"/api/approve-quotation";

                StringRequest req = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String msg = jsonObject.getString("msg");
                                    if (msg.equals("success"))
                                    {
                                        /*finish();
                                        Intent intent = new Intent(QuotationDetails.this,QuotationDashboard.class);
                                        intent.putExtra("UserID",UserID);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(),"Quotation Approved",Toast.LENGTH_LONG).show();*/
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put(KEY_ID, ID);
                        map.put(KEY_REPLY, input.getText().toString());
                        return map;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(req);

    }

    public void QuotationApproved_ALi()
    {
        final String url = "http://"+Survey_Login.IP_ALI+"/api/reply_quotation/" + Q_ID;

        StringRequest req = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("success"))
                            {
                                finish();
                                Intent intent = new Intent(QuotationDetails.this,QuotationDashboard.class);
                                intent.putExtra("UserID",UserID);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(),"Quotation Approved",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("status", "approved");
                map.put(KEY_REPLY, input.getText().toString());
                map.put("ticket-email", Email);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req);

    }

    public void QuotationDecline()
    {
                final String url = "http://"+Survey_Login.IP+"/api/decline-quotation";

                StringRequest req = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    //String msg = jsonObject.getString("msg");
                                   /* if (msg.equals("success"))
                                    {
                                        *//*finish();
                                        Intent intent = new Intent(QuotationDetails.this,QuotationDashboard.class);
                                        intent.putExtra("UserID",UserID);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(),"Quotation Declined",Toast.LENGTH_LONG).show();*//*
                                    }*/
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put(KEY_ID, ID);
                        map.put(KEY_REPLY, input.getText().toString());
                        return map;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(req);

    }
    public void QuotationDecline_ALI()
    {
        final String url = "http://"+Survey_Login.IP_ALI+"/api/reply_quotation/"+Q_ID;

        StringRequest req = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("success"))
                            {
                                finish();
                                Intent intent = new Intent(QuotationDetails.this,QuotationDashboard.class);
                                intent.putExtra("UserID",UserID);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(),"Quotation Declined",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("status", "declined");
                map.put(KEY_REPLY, input.getText().toString());
                map.put("ticket-email", Email);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req);

    }
}
