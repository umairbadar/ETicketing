package com.example.lubna.eticketing.Survey;

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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lubna.eticketing.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import me.leolin.shortcutbadger.ShortcutBadger;

public class SurveyList extends AppCompatActivity {
    private String TAG = SurveyList.class.getSimpleName();
    private ProgressDialog pDialog;
    private ArrayList<ModelS> Surveylist;
    SharedPreferences sharedp;
    SharedPreferences.Editor edit;
    ListView lview;
    String count , compcount;
    private   String  NRO="";
    private   String  Sta="";
    private   String  Manager1="manager_name";
    Intent intent;
    public String A = "";
    Button back;
    TextView txt,txtHeading;
    String UserID,RoleID,RoleName,DeptID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_list);
        sharedp = getSharedPreferences(Survey_Login.Pre, Context.MODE_PRIVATE);
        UserID = sharedp.getString("userid","");
        RoleID = sharedp.getString("role_id","");
        RoleName = sharedp.getString("role_name","");
        DeptID = sharedp.getString("userdepartid","");


        Surveylist = new ArrayList<ModelS>();
        lview = (ListView) findViewById(R.id.listview);
        intent = getIntent();
        A = intent.getStringExtra("A");
        txt = (TextView) findViewById(R.id.txtFailed);
        txtHeading = (TextView) findViewById(R.id.txtHeading);

        ShortcutBadger.removeCount(SurveyList.this);

        SurveyList();

        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String sno = ((TextView)view.findViewById(R.id.sNo)).getText().toString();
                String sur = ((TextView)view.findViewById(R.id.survey)).getText().toString();
                Intent i = new Intent(SurveyList.this, SurveyDetail.class);
                i.putExtra("Id",sno);
                i.putExtra("sury",sur);
                i.putExtra("status",Sta);
                i.putExtra("A",A);
                startActivity(i);
                finish();

            }
        });

    }

    public void SurveyList() {
        final AlertDialog progressDialog = new SpotsDialog(this,R.style.CustomProgress);
        progressDialog.show();
        progressDialog.setCancelable(false);
        if (RoleName.equals("reporting manager"))
        {
            progressDialog.dismiss();
            finish();
            //Toast.makeText(getApplicationContext(),"Reporting Manager " + A,Toast.LENGTH_LONG).show();
            Intent intent = new Intent(SurveyList.this,SurveyManagerDepart.class);
            intent.putExtra("A",A);
            intent.putExtra("RoleName",RoleName);
            startActivity(intent);
        }

        else
        {
            if (A.equals("Assignedsurvey")) {

                txtHeading.setText("Pending Survey");
                final String url = "http://" + Survey_Login.IP + "/api/getincompletesurveys/" + RoleID + "/" + UserID + "/" + DeptID;
                StringRequest req = new StringRequest(url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if (response != null) {
                                    try {
                                        progressDialog.dismiss();
                                        JSONObject jsonObj = new JSONObject(response);
                                        String msg = jsonObj.getString("msg");
                                        if (msg.equals("failed")) {
                                            lview.setVisibility(View.GONE);
                                            txt.setVisibility(View.VISIBLE);
                                            txt.setText("NO Record Available");
                                        } else {
                                            lview.setVisibility(View.VISIBLE);
                                            txt.setVisibility(View.GONE);
                                            JSONArray contacts = jsonObj.getJSONArray("incomplete_surveys");
                                            count = String.valueOf(contacts.length());
                                            edit = sharedp.edit();
                                            edit.putString("totalsurvey", count);
                                            // Toast.makeText(getApplicationContext(), "totalsurvey", Toast.LENGTH_SHORT).show();
                                            edit.commit();
                                            //  Toast.makeText(getApplicationContext(),count.toString() , Toast.LENGTH_SHORT).show();
                                            String id, survey, manager, sitename;
                                            for (int i = 0; i < contacts.length(); i++) {
                                                JSONObject c = contacts.getJSONObject(i);
                                                id = c.getString("id");
                                                survey = c.getString("business_title");
                                                manager = c.getString("manager_name");
                                                sitename = c.getString("site_name");
                                                NRO = c.getString("nro_num");
                                                edit = sharedp.edit();
                                                edit.putString("Sitename", c.getString("site_name"));
                                                edit.putString("Location", c.getString("location"));
                                                edit.putString("Title", c.getString("business_title"));
                                                edit.putString("Dealer", c.getString("dealer_name"));
                                                edit.putString("NRO", c.getString("nro_num"));
                                                edit.putString("Manager", c.getString("manager_name"));
                                                edit.putString("Status1", c.getString("status"));
                                                edit.putString("created", c.getString("created_at"));
                                                Sta = c.getString("status");
                                                edit.commit();
                                                //  Toast.makeText(getApplicationContext(),  survey, Toast.LENGTH_SHORT).show();
                                                ModelS item = new ModelS(id, survey, sitename);
                                                Surveylist.add(item);
                                            }
                                            listviewSAdapter adapter = new listviewSAdapter(SurveyList.this, Surveylist);
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
                                Toast.makeText(SurveyList.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                );
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(req);
            } else if (A.equals("completesurvey")) {
                txtHeading.setText("Complete Survey");
                final String url = "http://" + Survey_Login.IP + "/api/getcompletesurveys/" + RoleID + "/" + UserID + "/" + DeptID;
                StringRequest req = new StringRequest(url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if (response != null) {
                                    try {
                                        progressDialog.dismiss();
                                        JSONObject jsonObj = new JSONObject(response);
                                        String msg = jsonObj.getString("msg");
                                        if (msg.equals("failed")) {
                                            lview.setVisibility(View.GONE);
                                            txt.setVisibility(View.VISIBLE);
                                            txt.setText("NO Record Available");
                                        } else {
                                            lview.setVisibility(View.VISIBLE);
                                            txt.setVisibility(View.GONE);
                                            JSONArray contacts = jsonObj.getJSONArray("complete_surveys");
                                            compcount = String.valueOf(contacts.length());
                                            //  Toast.makeText(getApplicationContext(),count.toString() , Toast.LENGTH_SHORT).show();
                                            edit = sharedp.edit();
                                            edit.putString("completesurvey", compcount);
                                            // Toast.makeText(getApplicationContext(), "totalsurvey", Toast.LENGTH_SHORT).show();
                                            edit.commit();
                                            //  Toast.makeText(getApplicationContext(),count.toString() , Toast.LENGTH_SHORT).show();
                                            String id, survey, manager, sitename;
                                            for (int i = 0; i < contacts.length(); i++) {
                                                JSONObject c = contacts.getJSONObject(i);
                                                id = c.getString("id");
                                                survey = c.getString("business_title");
                                                manager = c.getString("manager_name");
                                                sitename = c.getString("site_name");
                                                NRO = c.getString("nro_num");
                                                edit = sharedp.edit();
                                                edit.putString("Sitename", c.getString("site_name"));
                                                edit.putString("Location", c.getString("location"));
                                                edit.putString("Title", c.getString("business_title"));
                                                edit.putString("Dealer", c.getString("dealer_name"));
                                                edit.putString("NRO", c.getString("nro_num"));
                                                edit.putString("Manager", c.getString("manager_name"));
                                                edit.putString("Dateu", c.getString("updated_at"));
                                                edit.putString("Status", c.getString("status"));
                                                Sta = c.getString("status");
                                                edit.commit();
                                                //  Toast.makeText(getApplicationContext(),  survey, Toast.LENGTH_SHORT).show();
                                                ModelS item = new ModelS(id, survey, sitename);
                                                Surveylist.add(item);
                                            }
                                            listviewSAdapter adapter = new listviewSAdapter(SurveyList.this, Surveylist);
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
                                Toast.makeText(SurveyList.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                );
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(req);


            }

            else if(A.equals("Alerts"))
            {
                txtHeading.setText("Alert");
                final String url = "http://"+Survey_Login.IP+"/api/getalerts/"+UserID +"/" +DeptID ;
                StringRequest req = new StringRequest(url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if (response != null) {
                                    try {
                                        progressDialog.dismiss();
                                        JSONObject jsonObj = new JSONObject(response);
                                        String msg = jsonObj.getString("msg");
                                        if (msg.equals("failed"))
                                        {
                                            lview.setVisibility(View.GONE);
                                            txt.setVisibility(View.VISIBLE);
                                            txt.setText("NO Record Available");
                                        }
                                        else
                                        {
                                            lview.setVisibility(View.VISIBLE);
                                            txt.setVisibility(View.GONE);
                                            JSONArray contacts = jsonObj.getJSONArray("alerts");
                                            compcount= String.valueOf( contacts.length());


                                            String id, survey,sitename;
                                            for (int i = 0; i < contacts.length(); i++) {
                                                JSONObject c = contacts.getJSONObject(i);
                                                JSONObject innerObject = c.getJSONObject("0");

                                                id = innerObject.getString("id");
                                                sitename = innerObject.getString("site_name");
                                                survey = innerObject.getString("business_title");


                                                ModelS item = new ModelS(id, survey,sitename);
                                                Surveylist.add(item);
                                            }
                                            listviewSAdapter adapter = new listviewSAdapter(SurveyList.this, Surveylist);
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
                                Toast.makeText(SurveyList.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                );
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(req);


            }

            else if (A.equals("warning"))
            {
                txtHeading.setText("Warning");
                final String url = "http://"+Survey_Login.IP+"/api/getwarnings/"+UserID +"/" +DeptID ;
                StringRequest req = new StringRequest(url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if (response != null) {
                                    try {
                                        progressDialog.dismiss();
                                        JSONObject jsonObj = new JSONObject(response);
                                        String msg = jsonObj.getString("msg");
                                        if (msg.equals("failed"))
                                        {
                                            lview.setVisibility(View.GONE);
                                            txt.setVisibility(View.VISIBLE);
                                            txt.setText("NO Record Available");
                                        }
                                        else
                                        {
                                            JSONArray contacts = jsonObj.getJSONArray("warnings");
                                            compcount= String.valueOf( contacts.length());

                                            String id, survey,sitename;
                                            for (int i = 0; i < contacts.length(); i++) {
                                                JSONObject c = contacts.getJSONObject(i);
                                                JSONObject innerObject = c.getJSONObject("0");

                                                id = innerObject.getString("id");
                                                sitename = innerObject.getString("site_name");
                                                survey = innerObject.getString("business_title");


                                                ModelS item = new ModelS(id, survey,sitename);
                                                Surveylist.add(item);
                                            }
                                            listviewSAdapter adapter = new listviewSAdapter(SurveyList.this, Surveylist);
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
                                Toast.makeText(SurveyList.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                );
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(req);


            }
            else if (A.equals("expired"))
            {
                txtHeading.setText("Expired");
                final String url = "http://"+Survey_Login.IP+"/api/getexpired/"+UserID +"/" +DeptID ;
                // final String url = "http://192.168.0.109:8000/api/getcompletesurvey/15/" + sharedp.getString("userid","");
                StringRequest req = new StringRequest(url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if (response != null) {
                                    try {
                                        progressDialog.dismiss();
                                        JSONObject jsonObj = new JSONObject(response);
                                        String msg = jsonObj.getString("msg");
                                        if (msg.equals("failed"))
                                        {
                                            lview.setVisibility(View.GONE);
                                            txt.setVisibility(View.VISIBLE);
                                            txt.setText("NO Record Available");
                                        }
                                        else
                                        {
                                            JSONArray contacts = jsonObj.getJSONArray("expires");
                                            compcount= String.valueOf( contacts.length());

                                            String id, survey,sitename;
                                            for (int i = 0; i < contacts.length(); i++) {
                                                JSONObject c = contacts.getJSONObject(i);
                                                JSONObject innerObject = c.getJSONObject("0");

                                                id = innerObject.getString("id");
                                                sitename = innerObject.getString("site_name");
                                                survey = innerObject.getString("business_title");


                                                ModelS item = new ModelS(id, survey,sitename);
                                                Surveylist.add(item);
                                            }
                                            listviewSAdapter adapter = new listviewSAdapter(SurveyList.this, Surveylist);
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
                                Toast.makeText(SurveyList.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                );
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(req);


            }
        }
    }


    @Override
    public void onBackPressed () {
        Intent intent = new Intent(this, DashboardSurvey.class);
        startActivity(intent);
        finish();
    }
}