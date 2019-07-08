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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static android.content.ContentValues.TAG;

public class QuotationDetails extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Model_Quotation_Item> list;

    private Button btnDashboard,btnLogout;
    public static final String KEY_ID = "id";
    public static final String KEY_REPLY = "reply";
    public String REPLY_VALUE;
    private String Q_ID,QID,RoleName,ParentID,ID,RoleID,UserID,Email;
    private TextView tv_recommend,tv_approve,tv_decline,txtSR,txtTicketID,txtTax,txtAmountBeforeTax,txtTotalAmount,txtID,txtSender,txtSite,txtBody,txtDate,txtStatus,txtReply;
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

        tv_recommend = findViewById(R.id.tv_recommend);
        tv_approve = findViewById(R.id.tv_approve);
        tv_decline = findViewById(R.id.tv_decline);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        txtAmountBeforeTax = findViewById(R.id.txtAmountBeforeTax);
        txtTax = findViewById(R.id.txtTax);
        txtTicketID = findViewById(R.id.txtTicketID);
        txtSR = findViewById(R.id.txtSR);
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
                new AlertDialog.Builder(QuotationDetails.this)
                        .setTitle("Are You Sure?")
                        .setMessage("You want to recommend this quotation to your Reporting Manager.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                QuotationRecommended();
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

        recyclerView = findViewById(R.id.recyclerViewItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false));
        list = new ArrayList<>();
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

        final AlertDialog progressDialog = new SpotsDialog(this,R.style.CustomProgress);
        progressDialog.show();
        progressDialog.setCancelable(false);

        final String url = ("http://"+Survey_Login.IP+"/api/get-quotation-detail/"+QID);
        StringRequest req = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response != null)
                        {
                            try {
                                progressDialog.dismiss();
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject innerObject = jsonObject.getJSONObject("quotation");
                                ID = innerObject.getString("id");
                                String sender = innerObject.getString("sender");
                                String site = innerObject.getString("site");
                                String body = innerObject.getString("body");
                                String Qdate = innerObject.getString("date");
                                String status = innerObject.getString("status");
                                String reply = innerObject.getString("reply");
                                String sr = innerObject.getString("sr_id");
                                String ticket_id = innerObject.getString("ticket_id");
                                String tax = innerObject.getString("tax") + "%";
                                String amount_before_tax = "Rs. " + innerObject.getString("before_tax_amount");
                                String total_amount = "Rs. " + innerObject.getString("total_amount");

                                if (RoleName.equals("reporting manager") && status.equals("recommended"))
                                {
                                    fabRecomm.setVisibility(View.GONE);
                                    tv_recommend.setVisibility(View.GONE);
                                }
                                if (RoleName.equals("manager") && status.equals("pending"))
                                {
                                    fabRecomm.setVisibility(View.VISIBLE);
                                    tv_recommend.setVisibility(View.VISIBLE);
                                    fabApp.setVisibility(View.VISIBLE);
                                    tv_approve.setVisibility(View.VISIBLE);
                                    fabDec.setVisibility(View.VISIBLE);
                                    tv_decline.setVisibility(View.VISIBLE);
                                }
                                if (RoleName.equals("manager") && (status.equals("approved") || status.equals("declined") ||
                                        status.equals("recommended")))
                                {
                                    fabRecomm.setVisibility(View.GONE);
                                    tv_recommend.setVisibility(View.GONE);
                                    fabApp.setVisibility(View.GONE);
                                    tv_approve.setVisibility(View.GONE);
                                    fabDec.setVisibility(View.GONE);
                                    tv_decline.setVisibility(View.GONE);
                                }
                                txtID.setText("Quotation ID: "+ ID);
                                txtSender.setText("Sender: "+sender);
                                txtSite.setText("Site: "+site);
                                txtBody.setText("Description: "+body);
                                txtDate.setText("Quotation Date: "+Qdate);
                                txtStatus.setText("Status: "+status);
                                txtReply.setText("Reply: "+reply);
                                txtSR.setText("SR: " + sr);
                                txtTicketID.setText("Ticket ID: " + ticket_id);
                                txtTax.setText("Tax: " + tax);
                                txtAmountBeforeTax.setText("Amount Before Tax: " + amount_before_tax);
                                txtTotalAmount.setText("Total Amount: " + total_amount);

                                JSONArray jsonArray = innerObject.getJSONArray("has_products");
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String product_name = object.getString("product_name");
                                    String quantity = object.getString("quantity");
                                    String cost = object.getString("cost");
                                    String sub_total = object.getString("sub_total");

                                    Model_Quotation_Item item = new Model_Quotation_Item(
                                            product_name,
                                            quantity,
                                            cost,
                                            sub_total
                                    );
                                    list.add(item);
                                }

                                adapter = new Adapter_Quotation_Item(list,QuotationDetails.this);
                                recyclerView.setAdapter(adapter);

                            } catch (final JSONException e) {
                                progressDialog.dismiss();
                                Log.e(TAG, "Json parsing error: " + e.getMessage());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),
                                                "Json parsing error: " + e.getMessage(),
                                                Toast.LENGTH_LONG)
                                                .show();
                                    }
                                });
                            }
                        }
                        else {
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
                        Toast.makeText(getApplicationContext(),error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
    }

    public void QuotationRecommended()
    {
        final AlertDialog progressDialog = new SpotsDialog(this,R.style.CustomProgress);
        progressDialog.show();
        progressDialog.setCancelable(false);

        final String url = "http://"+Survey_Login.IP+"/api/recommend-quotation/"+ID+"/"+ParentID;

        StringRequest req = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("success")) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Quotation Recommended",
                                        Toast.LENGTH_LONG).show();
                                finish();
                                Intent intent = new Intent(QuotationDetails.this, QuotationDashboard.class);
                                intent.putExtra("UserID", UserID);
                                startActivity(intent);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Quotation Recommended",
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });



        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req);
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
        final AlertDialog progressDialog = new SpotsDialog(this,R.style.CustomProgress);
        progressDialog.show();
        progressDialog.setCancelable(false);

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
                                progressDialog.dismiss();
                                finish();
                                Intent intent = new Intent(QuotationDetails.this,QuotationDashboard.class);
                                intent.putExtra("UserID",UserID);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(),"Quotation Approved",Toast.LENGTH_LONG).show();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),msg,
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),error.getMessage(),
                                Toast.LENGTH_LONG).show();
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
        final AlertDialog progressDialog = new SpotsDialog(this,R.style.CustomProgress);
        progressDialog.show();
        progressDialog.setCancelable(false);

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
                                progressDialog.dismiss();
                                finish();
                                Intent intent = new Intent(QuotationDetails.this,QuotationDashboard.class);
                                intent.putExtra("UserID",UserID);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(),"Quotation Declined",Toast.LENGTH_LONG).show();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),msg,
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),error.getMessage(),
                                Toast.LENGTH_LONG).show();
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
