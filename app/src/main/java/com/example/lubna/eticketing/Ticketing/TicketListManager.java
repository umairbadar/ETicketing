package com.example.lubna.eticketing.Ticketing;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lubna.eticketing.R;
import com.example.lubna.eticketing.Survey.SurveyDetail;
import com.example.lubna.eticketing.Survey.SurveyListManager;
import com.example.lubna.eticketing.Survey.Survey_Login;
import com.example.lubna.eticketing.Ticketing.Model;
import com.example.lubna.eticketing.Ticketing.TicketListing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class TicketListManager extends AppCompatActivity {

    private String TAG = TicketListing.class.getSimpleName();
    private ProgressDialog pDialog;
    private ArrayList<Model> Ticketlist;
    SharedPreferences sharedp;
    SharedPreferences.Editor edit;
    ListView lview;
    private int pendingcount=0;
    String  count , compcount;
    private   String  NRO="";
    private   String  Sta="";
    private   String  Manager1="manager_name";
    Intent intent;
    public String A = "";
    String UserID,DepartID,RoleName;
    TextView txt,txtHeading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list_manager);

        lview = (ListView) findViewById(R.id.lv);
        sharedp = getSharedPreferences(Survey_Login.Pre, Context.MODE_PRIVATE);
        UserID = sharedp.getString("userid","");
        RoleName = sharedp.getString("role_name","");
        //Toast.makeText(getApplicationContext(),UserID,Toast.LENGTH_LONG).show();


        Ticketlist = new ArrayList<Model>();
        intent = getIntent();
        A = intent.getStringExtra("A");
        DepartID = intent.getStringExtra("DepartID");

        txt = (TextView) findViewById(R.id.txtFailed);
        txtHeading = (TextView) findViewById(R.id.txtHeading);

        TicketList();

        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String sno = ((TextView) view.findViewById(R.id.sNo)).getText().toString();
                String site = ((TextView) view.findViewById(R.id.sitename)).getText().toString();
                String sur = ((TextView) view.findViewById(R.id.survey)).getText().toString();
                Intent i = new Intent(TicketListManager.this, TicketDetail.class);
                i.putExtra("Id", sno);
                i.putExtra("sender", sur);
                i.putExtra("status", site);
                i.putExtra("A", A);
                startActivity(i);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed () {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
        finish();
    }

    public void TicketList()
    {
        final AlertDialog progressDialog = new SpotsDialog(this,R.style.CustomProgress);
        progressDialog.show();
        progressDialog.setCancelable(false);

        if (A.equals("pending")) {
            final String url = "http://" + Survey_Login.IP + "/api/employee-pending-tickets/" + UserID + "/" + DepartID;
            StringRequest req = new StringRequest(url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response != null) {
                                try {
                                    txtHeading.setText("Pending Tickets");
                                    progressDialog.dismiss();
                                    JSONObject jsonObj = new JSONObject(response);
                                    String msg = jsonObj.getString("msg");
                                    if (msg.equals("failed")) {
                                        lview.setVisibility(View.GONE);
                                        txt.setVisibility(View.VISIBLE);
                                        txt.setText("NO Record Available");
                                    } else {
                                        JSONArray contacts = jsonObj.getJSONArray("employee_tickets");
                                        String id, ticket, sitename, status;
                                        for (int i = 0; i < contacts.length(); i++) {
                                            JSONObject c = contacts.getJSONObject(i);
                                            id = c.getString("id");
                                            ticket = c.getString("ticket_title");
                                            sitename = c.getString("ticket_site_name");
                                            status = c.getString("status");
                                            if (status.equals("pending")) {
                                                pendingcount++;
                                                Model item = new Model(id, ticket, sitename);
                                                Ticketlist.add(item);
                                            }

                                        }
                                        listviewAdapter adapter = new listviewAdapter(TicketListManager.this, Ticketlist);
                                        lview.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
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
                            Toast.makeText(TicketListManager.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
            );
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(req);
        }
        else if (A.equals("inprocess")) {
            final String url = "http://" + Survey_Login.IP + "/api/employee-inprocess-tickets/" + UserID + "/" + DepartID;
            StringRequest req = new StringRequest(url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response != null) {
                                try {
                                    txtHeading.setText("Assigned Tickets");
                                    progressDialog.dismiss();
                                    JSONObject jsonObj = new JSONObject(response);
                                    String msg = jsonObj.getString("msg");
                                    if (msg.equals("failed")) {
                                        lview.setVisibility(View.GONE);
                                        txt.setVisibility(View.VISIBLE);
                                        txt.setText("NO Record Available");
                                    } else {
                                        JSONArray contacts = jsonObj.getJSONArray("inprocess_tickets");
                                        String id, ticket, sitename, status;
                                        for (int i = 0; i < contacts.length(); i++) {
                                            JSONObject c = contacts.getJSONObject(i);
                                            id = c.getString("id");
                                            ticket = c.getString("ticket_title");
                                            sitename = c.getString("ticket_site_name");
                                            status = c.getString("status");
                                            if (status.equals("assigned")) {
                                                pendingcount++;
                                                Model item = new Model(id, ticket, sitename);
                                                Ticketlist.add(item);
                                            }


                                        }
                                        listviewAdapter adapter = new listviewAdapter(TicketListManager.this, Ticketlist);
                                        lview.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
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
                            Toast.makeText(TicketListManager.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
            );
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(req);
        } else if (A.equals("complete")) {
            final String url = "http://" + Survey_Login.IP + "/api/employee-complete-tickets/" + UserID + "/" + DepartID;
            StringRequest req = new StringRequest(url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response != null) {
                                try {
                                    txtHeading.setText("Complete Tickets");
                                    progressDialog.dismiss();
                                    JSONObject jsonObj = new JSONObject(response);
                                    String msg = jsonObj.getString("msg");
                                    if (msg.equals("failed")) {
                                        lview.setVisibility(View.GONE);
                                        txt.setVisibility(View.VISIBLE);
                                        txt.setText("NO Record Available");
                                    } else {
                                        JSONArray contacts = jsonObj.getJSONArray("complete_tickets");
                                        String id, ticket, sitename, status;
                                        for (int i = 0; i < contacts.length(); i++) {
                                            JSONObject c = contacts.getJSONObject(i);
                                            id = c.getString("id");
                                            ticket = c.getString("ticket_title");
                                            sitename = c.getString("ticket_site_name");
                                            status = c.getString("status");
                                            if (status.equals("complete")) {
                                                pendingcount++;
                                                Model item = new Model(id, ticket, sitename);
                                                Ticketlist.add(item);
                                            }

                                        }
                                        listviewAdapter adapter = new listviewAdapter(TicketListManager.this, Ticketlist);
                                        lview.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
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
                            Toast.makeText(TicketListManager.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
            );
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(req);


        }
        else if (A.equals("transferred"))
        {
            final String url = "http://" + Survey_Login.IP + "/api/employee-transferred-tickets/" + UserID + "/" + DepartID;

            StringRequest req = new StringRequest(url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                progressDialog.dismiss();
                                JSONObject jsonObject = new JSONObject(response);
                                String msg = jsonObject.getString("msg");
                                if (msg.equals("fail")) {
                                    lview.setVisibility(View.GONE);
                                    txt.setVisibility(View.VISIBLE);
                                    txt.setText("NO Record Available");
                                }
                                else
                                {
                                    JSONArray jsonArray = jsonObject.getJSONArray("transferred_tickets");
                                    String id, ticket_title, sitename;
                                    for (int i = 0; i < jsonArray.length(); i++)
                                    {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        id = object.getString("ticket_id");
                                        JSONObject innerObject = object.getJSONObject("find_ticket");
                                        sitename = innerObject.getString("ticket_site_name");
                                        ticket_title = innerObject.getString("ticket_title");
                                        Model item = new Model(id, ticket_title, sitename);
                                        Ticketlist.add(item);
                                    }
                                    listviewAdapter adapter = new listviewAdapter(TicketListManager.this, Ticketlist);
                                    lview.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
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

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(TicketListManager.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(req);
        }
        else if (A.equals("Rejected"))
        {
            final String url = "http://" + Survey_Login.IP + "/api/employee-rejected-tickets/" + UserID + "/" + DepartID;

            StringRequest req = new StringRequest(url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                progressDialog.dismiss();
                                JSONObject jsonObject = new JSONObject(response);
                                String msg = jsonObject.getString("msg");
                                if (msg.equals("fail")) {
                                    lview.setVisibility(View.GONE);
                                    txt.setVisibility(View.VISIBLE);
                                    txt.setText("NO Record Available");
                                }
                                else
                                {
                                    JSONArray jsonArray = jsonObject.getJSONArray("rejected_tickets");
                                    String id, ticket_title, sitename;
                                    for (int i = 0; i < jsonArray.length(); i++)
                                    {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        id = object.getString("id");
                                        sitename = object.getString("ticket_site_name");
                                        ticket_title = object.getString("ticket_title");
                                        Model item = new Model(id, ticket_title, sitename);
                                        Ticketlist.add(item);
                                    }
                                    listviewAdapter adapter = new listviewAdapter(TicketListManager.this, Ticketlist);
                                    lview.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
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

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(TicketListManager.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(req);
        }
        else if (A.equals("Accepted"))
        {
            final String url = "http://" + Survey_Login.IP + "/api/employee-accepted-tickets/" + UserID + "/" + DepartID;

            StringRequest req = new StringRequest(url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                progressDialog.dismiss();
                                JSONObject jsonObject = new JSONObject(response);
                                String msg = jsonObject.getString("msg");
                                if (msg.equals("fail")) {
                                    lview.setVisibility(View.GONE);
                                    txt.setVisibility(View.VISIBLE);
                                    txt.setText("NO Record Available");
                                }
                                else
                                {
                                    JSONArray jsonArray = jsonObject.getJSONArray("rejected_tickets");
                                    String id, ticket_title, sitename;
                                    for (int i = 0; i < jsonArray.length(); i++)
                                    {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        id = object.getString("id");
                                        sitename = object.getString("ticket_site_name");
                                        ticket_title = object.getString("ticket_title");
                                        Model item = new Model(id, ticket_title, sitename);
                                        Ticketlist.add(item);
                                    }
                                    listviewAdapter adapter = new listviewAdapter(TicketListManager.this, Ticketlist);
                                    lview.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
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

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(TicketListManager.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(req);
        }
    }

}
