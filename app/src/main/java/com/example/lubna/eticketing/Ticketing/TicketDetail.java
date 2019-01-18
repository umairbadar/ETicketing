package com.example.lubna.eticketing.Ticketing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
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

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static android.content.ContentValues.TAG;

public class TicketDetail extends AppCompatActivity {
    private TextView txtAssignBY,txtHead,txtChck,tvtittle, tvid, tvdescription, tvsite, tv_status, tv_created, tv_complete, tvassign, tvopendate, tvcompany;
    private Button BnDashboard,BnLogout,BnAssignedTickets;
    SharedPreferences sharedp;
    private static final String TAG_RESULTS = "ticket_details";
    public static final  String Pre = "MyPre";
    String TID = null;
    private Intent intent;
    private String UserRoleName,Rejected_Tic_ID = "null";
    private AlertDialog progressDialog;
    private String A,Ticket_Log_ID,DepartID;
    private Button BnAccept,BnReject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        BnAccept = findViewById(R.id.btnAccept);
        BnReject = findViewById(R.id.btnReject);

        txtHead = findViewById(R.id.txtHead);
        txtAssignBY = findViewById(R.id.txtAssignBy);

        progressDialog = new SpotsDialog(this,R.style.CustomProgress);
        progressDialog.show();
        progressDialog.setCancelable(false);

        sharedp = getSharedPreferences(Survey_Login.Pre, Context.MODE_PRIVATE);
        UserRoleName = sharedp.getString("role_name","");
        DepartID = sharedp.getString("userdepartid","");

        intent = getIntent();
        TID = intent.getStringExtra("Id");
        A = intent.getStringExtra("A");



        //Toast.makeText(getApplicationContext(), TID.toString(), Toast.LENGTH_SHORT).show();
        tvid = (TextView) findViewById(R.id.ticketid);
        tvid.setText("Ticket ID: " + TID);
        tvtittle = (TextView) findViewById(R.id.tickettittle);
        tvdescription = (TextView) findViewById(R.id.ticketdescrptn);
        tvsite = (TextView) findViewById(R.id.tvticketsite);
        tv_status = (TextView) findViewById(R.id.ticketstatus);
        tv_created = (TextView) findViewById(R.id.createdate);
        tv_complete = (TextView) findViewById(R.id.compldate);
        tvassign = (TextView) findViewById(R.id.assigdate);
//        tvopendate = (TextView) findViewById(R.id.opendate);
        txtChck = (TextView) findViewById(R.id.txtChck);
        tvcompany = (TextView) findViewById(R.id.ticketcompany);
        BnDashboard = (Button) findViewById(R.id.btnDashboard);
        BnLogout = (Button) findViewById(R.id.btnLogout);
        BnAssignedTickets = findViewById(R.id.btnAssignTickets);

        if (A.equals("Rejected"))
        {
            tv_status.setText("Status: Rejected");
            tvcompany.setText(" None");
        }
        else if (A.equals("Accepted"))
        {
            tv_status.setText("Status: Accepted");
            tvcompany.setText(" None");
        }

        BnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(TicketDetail.this)
                        .setTitle("Are You Sure?")
                        .setMessage("You want to accept this ticket.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                TicketAccepted();
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

        BnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getApplicationContext(),"Rejected!",Toast.LENGTH_LONG).show();
                TicketRejected();
            }
        });

        BnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                startActivity(new Intent(TicketDetail.this,Dashboard.class));
            }
        });
        BnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences pref =  getSharedPreferences(Pre, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.apply();
                finish();
                startActivity(new Intent(TicketDetail.this,Survey_Login.class));
            }
        });

        BnAssignedTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                Intent intent = new Intent(getApplicationContext(),AssignTickets.class);
                intent.putExtra("T_ID",TID);
                intent.putExtra("ID",Rejected_Tic_ID);
                startActivity(intent);
            }
        });

        SurveyList();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        startActivity(new Intent(TicketDetail.this,Dashboard.class));
    }

    public void SurveyList() {

            final String url = ("http://" + Survey_Login.IP + "/api/employee-ticket-detail/" + TID);
            StringRequest req = new StringRequest(url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response != null) {
                                progressDialog.dismiss();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONObject innerObj = jsonObject.getJSONObject("ticket_detail");

                                    tvsite.setText("Site Name: " + innerObj.getString("ticket_site_name"));
                                    //tv_status.setText("   Status:" + innerObj.getString("status"));
                                    tv_status.setTextColor(Color.parseColor("#ff669900"));
                                    tvtittle.setText("Tittle: " + innerObj.getString("ticket_title"));
                                    String description = innerObj.getString("ticket_message");
                                    description = Html.fromHtml(description).toString().
                                            replace("\r", "");
                                    tvdescription.setText(description);
                                    tvdescription.setTextColor(Color.parseColor("#233588"));
                                    tv_created.setText("Created Date: " + innerObj.getString("created_at"));
                                    tv_complete.setText("Completed Date " + innerObj.getString("complete_date"));
                                    tvassign.setText("Assigned on: " + innerObj.getString("updated_at"));

                                    String status = innerObj.getString("status");
                                    String assignTo = innerObj.getString("assigned_to");

                                    if (status.equals("pending") || status.equals("accepted")) {

                                        if (A.equals("pending")) {
                                            if (UserRoleName.equals("manager")) {
                                                BnAssignedTickets.setVisibility(View.VISIBLE);
                                            } else {
                                                BnAssignedTickets.setVisibility(View.GONE);
                                            }
                                            tvcompany.setVisibility(View.GONE);
                                            txtChck.setVisibility(View.GONE);
                                            tv_status.setText("Status: Pending");
                                            txtHead.setText("Unassigned Ticket Detail");
                                        }
                                    }
                                    else if (A.equals("Rejected"))
                                    {
                                        JSONArray array1 = innerObj.getJSONArray("rejected_tickets");
                                        for (int i = 0; i < array1.length(); i++)
                                        {
                                            JSONObject jo = array1.getJSONObject(i);
                                            Rejected_Tic_ID = jo.getString("id");
                                        }
                                        if (UserRoleName.equals("manager"))
                                        {
                                            BnAssignedTickets.setText("Re-Assign");
                                            BnAssignedTickets.setVisibility(View.VISIBLE);
                                        }
                                        else
                                        {
                                            BnAssignedTickets.setVisibility(View.GONE);
                                        }
                                    }
                                    else if (status.equals("assigned")) {
                                        txtHead.setText("Assigned Ticket Detail");
                                        if (assignTo.equals("departments")) {
                                            JSONObject Object = innerObj.getJSONObject("assigned_departments");
                                            String dep_name = Object.getString("dep_name");
                                            tv_status.setText("Assigned to Department");
                                            tvcompany.setText(dep_name);
                                            tvcompany.setTextColor(Color.parseColor("#233588"));

                                            JSONObject innerObject = innerObj.getJSONObject("0");
                                            JSONObject object1 = innerObject.getJSONObject("assigned_by");
                                            String ManagerName = object1.getString("name");
                                            txtAssignBY.setVisibility(View.VISIBLE);
                                            txtAssignBY.setText("Assigned by: "+ManagerName);
                                            txtAssignBY.setTextColor(Color.RED);

                                            JSONArray array = innerObj.getJSONArray("ticket_assign_log");
                                            for (int i = 0; i < array.length(); i++)
                                            {
                                                JSONObject jo = array.getJSONObject(i);
                                                String status1 = jo.getString("status");
                                                Ticket_Log_ID = jo.getString("id");
                                                if (status1.equals("pending"))
                                                {
                                                    if (A.equals("transferred") && UserRoleName.equals("manager")) {
                                                        BnAccept.setVisibility(View.VISIBLE);
                                                        BnReject.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        BnAccept.setVisibility(View.GONE);
                                                        BnReject.setVisibility(View.GONE);
                                                    }
                                                }
                                            }

                                        } else if (assignTo.equals("third-parties")) {
                                            JSONObject Object = innerObj.getJSONObject("assigned_thirdparties");
                                            String name = Object.getString("name");
                                            tv_status.setText("Assigned to Third Party");
                                            tvcompany.setText(name);
                                            tvcompany.setTextColor(Color.parseColor("#233588"));
                                        } else if (assignTo.equals("own-employees")) {
                                            JSONObject object = innerObj.getJSONObject("0");
                                            JSONArray array = object.getJSONArray("assigned_to");
                                            for (int i = 0; i < array.length(); i++) {
                                                JSONObject arrayObj = array.getJSONObject(i);
                                                String names = arrayObj.getString("name");

                                                tv_status.setText("Assigned to Employee");
                                                tvcompany.append(names);
                                                tvcompany.append(",");
                                                tvcompany.setTextColor(Color.parseColor("#233588"));
                                            }
                                            String text = tvcompany.getText().toString();
                                            if (text.endsWith(","))
                                            {
                                                text = text.substring(0, text.length() - 1);
                                                tvcompany.setText(text);
                                            }
                                            JSONObject innerObject = object.getJSONObject("assigned_by");
                                            String ManagerName = innerObject.getString("name");
                                            txtAssignBY.setVisibility(View.VISIBLE);
                                            txtAssignBY.setText("Assign by: "+ManagerName);
                                            txtAssignBY.setTextColor(Color.RED);
                                        }
                                    }
                                    else if (status.equals("complete")) {
                                        txtHead.setText("Complete Ticket Detail");
                                        tvcompany.setVisibility(View.GONE);
                                        txtChck.setVisibility(View.GONE);
                                        tv_status.setText("Status: Complete");
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
                            Toast.makeText(TicketDetail.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
            );
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(req);
    }

    public void TicketAccepted()
    {
        String Url = "http://"+Survey_Login.IP+"/api/accept-ticket";

        StringRequest req = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("success"))
                            {
                                Toast.makeText(getApplicationContext(),"Accepted!",Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(),Dashboard.class));
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Not Accepted!",Toast.LENGTH_LONG).show();
                            }
                        } catch (final JSONException e) {
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TicketDetail.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("ticket_id", TID);
                map.put("ticket_log_id",Ticket_Log_ID);
                map.put("department_id", DepartID);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
    }

    public void TicketRejected()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Reject Ticket");

        final EditText input = new EditText(this);
        input.setSingleLine(false);
        input.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                final String url = "http://"+Survey_Login.IP+"/api/reject-ticket";

                StringRequest req = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String msg = jsonObject.getString("msg");
                                    if (msg.equals("success"))
                                    {
                                        Toast.makeText(getApplicationContext(),"Rejected!",Toast.LENGTH_LONG).show();
                                        finish();
                                        startActivity(new Intent(getApplicationContext(),Dashboard.class));
                                    }
                                } catch (final JSONException e) {
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
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(TicketDetail.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("ticket_id", TID);
                        map.put("ticket_log_id", Ticket_Log_ID);
                        map.put("reject_reason", input.getText().toString());
                        map.put("department_id", DepartID);
                        return map;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(req);

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }
}
