package com.example.lubna.eticketing.Ticketing;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.example.lubna.eticketing.Survey.Survey_Login;
import com.example.lubna.eticketing.Ticketing.Quotation.QuotationDepartments;

import org.json.JSONException;
import org.json.JSONObject;

import dmax.dialog.SpotsDialog;

import static android.content.ContentValues.TAG;


public class Dashboard extends Activity {

    private SharedPreferences sharedp;
    public static final  String Pre = "MyPre";
    private String UserID,UserRoleID,UserRoleName;
    private TextView tvpending,tvinprocess,tvcomplete,tvtransfer,txtpendings,txtMyAssignments,txtAccepted,txtRejected;
    private LinearLayout Layout1,MyAssignments,Emp_Tic_Rejected,Emp_Tic_Accepted;
    private RelativeLayout Layout2;
    private String PendingTransfer,AcceptedTransfer,RejectedTransfer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        txtAccepted = findViewById(R.id.txtAccepted);
        txtRejected = findViewById(R.id.txtRejected);

        MyAssignments = findViewById(R.id.btnMyAssignment);
        Emp_Tic_Rejected = findViewById(R.id.btnRejected);
        Emp_Tic_Accepted = findViewById(R.id.btnAccepted);

        txtpendings = findViewById(R.id.txtpendings);

        Layout1 = findViewById(R.id.DataLayout);
        Layout2 = findViewById(R.id.ErrorLayout);

        sharedp = getSharedPreferences(Survey_Login.Pre, Context.MODE_PRIVATE);
        UserID = sharedp.getString("userid","");
        UserRoleID = sharedp.getString("role_id","");
        UserRoleName = sharedp.getString("role_name","");

        tvpending = (TextView)findViewById(R.id.tvpending);
        tvinprocess =( TextView)findViewById(R.id.tvinprocess);
        tvcomplete = (TextView)findViewById(R.id.tvcomplete);
        tvtransfer = (TextView)findViewById(R.id.transferedtxt);
        txtMyAssignments = (TextView)findViewById(R.id.myAssignmenttxt);


        LinearLayout Nticket= (LinearLayout) findViewById(R.id.btnnewticket);
        Nticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateTicket.class);
                startActivity(i);
                finish();
            }
        });
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
                startActivity(new Intent(Dashboard.this,Survey_Login.class));
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
        LinearLayout pending= (LinearLayout) findViewById(R.id.btnpending);
        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TicketListing.class);
                i.putExtra("A","pending");
                startActivity(i);
                finish();
            }
        });
        LinearLayout Inprocess= (LinearLayout) findViewById(R.id.btninprocess);
        Inprocess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TicketListing.class);
                i.putExtra("A","inprocess");
                startActivity(i);
                finish();
            }
        });
        LinearLayout Complete= (LinearLayout) findViewById(R.id.btncomplete);
        Complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TicketListing.class);
                i.putExtra("A","complete");
                startActivity(i);
                finish();
            }
        });

        MyAssignments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), TicketListing.class);
                i.putExtra("A","myAssignments");
                startActivity(i);
                finish();
            }
        });

        Emp_Tic_Rejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), TicketListing.class);
                i.putExtra("A","Rejected");
                startActivity(i);
                finish();
            }
        });

        Emp_Tic_Accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), TicketListing.class);
                i.putExtra("A","Accepted");
                startActivity(i);
                finish();
            }
        });

        LinearLayout Transfered = findViewById(R.id.btntransfered);

        LinearLayout Quotation= (LinearLayout) findViewById(R.id.btnQuotation);

        if (UserRoleName.equals("manager") || UserRoleName.equals("reporting manager"))
        {
            txtpendings.setText("Unassigned");
            Transfered.setVisibility(View.VISIBLE);
            Quotation.setVisibility(View.VISIBLE);
            Quotation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                    startActivity(new Intent(Dashboard.this,QuotationDepartments.class));
                }
            });
        }
        else
        {
            Emp_Tic_Rejected.setVisibility(View.VISIBLE);
            Emp_Tic_Accepted.setVisibility(View.VISIBLE);
            Quotation.setVisibility(View.GONE);
            MyAssignments.setVisibility(View.VISIBLE);
            txtpendings.setText("Pending");
            Transfered.setVisibility(View.GONE);
        }

        Transfered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), TransferredTickets.class);
                i.putExtra("PendingCount",PendingTransfer);
                i.putExtra("AcceptedCount",AcceptedTransfer);
                i.putExtra("RejectedCount",RejectedTransfer);
                startActivity(i);
                finish();

            }
        });

        /*if (UserRoleName.equals("employee"))
        {
            Quotation.setVisibility(View.GONE);
            MyAssignments.setVisibility(View.VISIBLE);
        }
        else
        {
            Quotation.setVisibility(View.VISIBLE);
            Quotation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                    startActivity(new Intent(Dashboard.this,QuotationDepartments.class));
                }
            });
        }*/

        pendingticket();
    }
    @Override
    public void onBackPressed () {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
        finish();
    }

    private void refresh() {
        finish();
        startActivity(getIntent());
    }

    public void pendingticket() {
        final AlertDialog progressDialog = new SpotsDialog(this,R.style.CustomProgress);
        progressDialog.show();
        progressDialog.setCancelable(false);

        if (UserRoleName.equals("reporting manager"))
        {
            progressDialog.dismiss();
            tvpending.setText("Click here");
            tvinprocess.setText("Click here");
            tvcomplete.setText("Click here");
            tvtransfer.setText("Click here");
        }
        else {
            final String url = "http://" + Survey_Login.IP + "/api/count-tickets/" + UserID;
            StringRequest req = new StringRequest(url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response != null) {
                                progressDialog.dismiss();
                                try {

                                    JSONObject jsonObj = new JSONObject(response);
                                    tvpending.setText(jsonObj.getString("total_pending"));
                                    tvinprocess.setText(jsonObj.getString("total_inprocess"));
                                    tvcomplete.setText(jsonObj.getString("total_complete"));
                                    PendingTransfer = jsonObj.getString("pending_transferred");
                                    AcceptedTransfer = jsonObj.getString("accepted_transferred");
                                    RejectedTransfer = jsonObj.getString("rejected_transferred");
                                    tvtransfer.setText("Click here");
                                    txtMyAssignments.setText(jsonObj.getString("my_assignments"));
                                    txtAccepted.setText(AcceptedTransfer);
                                    txtRejected.setText(RejectedTransfer);

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

                            progressDialog.dismiss();
                            Layout1.setVisibility(View.GONE);
                            Layout2.setVisibility(View.VISIBLE);
                        }
                    }
            );
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(req);

        }

    }
}
