package com.example.lubna.eticketing.Ticketing.Quotation;

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
import com.example.lubna.eticketing.Survey.Survey_Login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class QuotationListing extends AppCompatActivity {

    private ListView listView;
    private TextView txtHeading,txtFailed;
    private String TAG = QuotationListing.class.getSimpleName();
    private ProgressDialog pDialog;
    private ArrayList<ModelQL> QuotationList;
    private SharedPreferences sharedp;
    private SharedPreferences.Editor edit;
    private Intent intent;
    private String UserID,A,DeptID,RoleID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation_listing);

        sharedp = getSharedPreferences(Survey_Login.Pre, Context.MODE_PRIVATE);
        //UserID = sharedp.getString("userid","");
        DeptID = sharedp.getString("userdepartid","");
        RoleID = sharedp.getString("role_id","");

        QuotationList = new ArrayList<ModelQL>();

        intent = getIntent();
        A = intent.getStringExtra("A");
        UserID = intent.getStringExtra("userid");

        txtHeading = findViewById(R.id.txtHeading);
        txtFailed = findViewById(R.id.txtFailed);
        listView = findViewById(R.id.lv);

        QuotationList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String ID = ((TextView) view.findViewById(R.id.txtQID)).getText().toString();
                String QID = ((TextView) view.findViewById(R.id.txtID)).getText().toString();
                Intent intent = new Intent(QuotationListing.this,QuotationDetails.class);
                intent.putExtra("QID",ID);
                intent.putExtra("ID",QID);
                intent.putExtra("UserID",UserID);
                startActivity(intent);
                finish();
            }
        });
    }

    public void QuotationList() {

        final AlertDialog progressDialog = new SpotsDialog(this,R.style.CustomProgress);
        progressDialog.show();
        progressDialog.setCancelable(false);

        if (A.equals("Recommended"))
        {
            txtHeading.setText("Recommended Quotations");
            final String url = "http://"+Survey_Login.IP+"/api/get-recommended-quotations/"+UserID;
            StringRequest req = new StringRequest(url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response != null)
                            {
                                try {
                                    progressDialog.dismiss();
                                    JSONObject jsonObject = new JSONObject(response);
                                    String msg = jsonObject.getString("msg");
                                    if (msg.equals("failed"))
                                    {
                                        listView.setVisibility(View.GONE);
                                        txtFailed.setVisibility(View.VISIBLE);
                                        txtFailed.setText("NO Record Available");
                                    }
                                    else {
                                        JSONArray jsonArray = jsonObject.getJSONArray("recommended_quotations");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            String id = object.getString("id");
                                            String QID = object.getString("quotation_id");
                                            String sender = object.getString("sender");
                                            String site = object.getString("site");
                                            ModelQL item = new ModelQL(id, QID, sender, site);
                                            QuotationList.add(item);
                                        }

                                        listviewAdapterQL adapter = new listviewAdapterQL(QuotationList, QuotationListing.this);
                                        listView.setAdapter(adapter);
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
                            }else
                            {
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
                    }
                    );
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(req);
        }
        else if (A.equals("Approved"))
        {
            txtHeading.setText("Approved Quotations");
            final String url = "http://"+Survey_Login.IP+"/api/get-approved-quotations/"+DeptID+"/"+UserID;

            StringRequest req = new StringRequest(url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response != null)
                            {
                                try {
                                    progressDialog.dismiss();
                                    JSONObject jsonObject = new JSONObject(response);
                                    String msg = jsonObject.getString("msg");
                                    if (msg.equals("failed"))
                                    {
                                        listView.setVisibility(View.GONE);
                                        txtFailed.setVisibility(View.VISIBLE);
                                        txtFailed.setText("NO Record Available");
                                    }
                                    else {
                                        JSONArray jsonArray = jsonObject.getJSONArray("approved_quotations");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            String id = object.getString("id");
                                            String QID = object.getString("quotation_id");
                                            String sender = object.getString("sender");
                                            String site = object.getString("site");
                                            ModelQL item = new ModelQL(id, QID, sender, site);
                                            QuotationList.add(item);
                                        }
                                        listviewAdapterQL adapter = new listviewAdapterQL(QuotationList, QuotationListing.this);
                                        listView.setAdapter(adapter);
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
                            else
                            {
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
        else if (A.equals("Declined"))
        {
            txtHeading.setText("Declined Quotations");
            final String url = "http://"+Survey_Login.IP+"/api/get-declined-quotations/"+DeptID+"/"+UserID;

            StringRequest req = new StringRequest(url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response != null)
                            {
                                try {
                                    progressDialog.dismiss();
                                    JSONObject jsonObject = new JSONObject(response);
                                    String msg = jsonObject.getString("msg");
                                    if (msg.equals("failed"))
                                    {
                                        listView.setVisibility(View.GONE);
                                        txtFailed.setVisibility(View.VISIBLE);
                                        txtFailed.setText("NO Record Available");
                                    }
                                    else {
                                        JSONArray jsonArray = jsonObject.getJSONArray("declined_quotations");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            String id = object.getString("id");
                                            String QID = object.getString("quotation_id");
                                            String sender = object.getString("sender");
                                            String site = object.getString("site");
                                            ModelQL item = new ModelQL(id, QID, sender, site);
                                            QuotationList.add(item);
                                        }
                                        listviewAdapterQL adapter = new listviewAdapterQL(QuotationList, QuotationListing.this);
                                        listView.setAdapter(adapter);
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
                            else
                            {
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
        else if (A.equals("Pending"))
        {
            txtHeading.setText("Pending Quotations");
            final String url = "http://"+Survey_Login.IP+"/api/get-pending-quotations/"+DeptID+"/"+UserID;

            StringRequest req = new StringRequest(url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response != null)
                            {
                                try {
                                    progressDialog.dismiss();
                                    JSONObject jsonObject = new JSONObject(response);
                                    String msg = jsonObject.getString("msg");
                                    if (msg.equals("failed"))
                                    {
                                        listView.setVisibility(View.GONE);
                                        txtFailed.setVisibility(View.VISIBLE);
                                        txtFailed.setText("NO Record Available");
                                    }
                                    else {
                                        JSONArray jsonArray = jsonObject.getJSONArray("pending_quotations");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            String id = object.getString("id");
                                            String QID = object.getString("quotation_id");
                                            String sender = object.getString("sender");
                                            String site = object.getString("site");
                                            ModelQL item = new ModelQL(id, QID, sender, site);
                                            QuotationList.add(item);
                                        }
                                        listviewAdapterQL adapter = new listviewAdapterQL(QuotationList, QuotationListing.this);
                                        listView.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
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

    }

    @Override
    public void onBackPressed() {

        finish();
        Intent intent = new Intent(getApplicationContext(), QuotationDashboard.class);
        intent.putExtra("UserID",UserID);
        startActivity(intent);
    }
}
