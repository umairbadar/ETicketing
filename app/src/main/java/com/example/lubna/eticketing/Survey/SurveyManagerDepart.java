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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lubna.eticketing.R;
import com.example.lubna.eticketing.Ticketing.Dashboard;
import com.example.lubna.eticketing.Ticketing.Quotation.QuotationDashboard;
import com.example.lubna.eticketing.Ticketing.Quotation.QuotationDepartments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class SurveyManagerDepart extends AppCompatActivity {


    private SharedPreferences sharedp;
    private String TAG = SurveyManagerDepart.class.getSimpleName();
    private String A,RoleName,DeptID,ID,Name,UserID;
    private ArrayList<ModelSMD> Departlist;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_manager_depart);

        Departlist = new ArrayList<ModelSMD>();

        sharedp = getSharedPreferences(Survey_Login.Pre, Context.MODE_PRIVATE);
        DeptID = sharedp.getString("userdepartid","");
        UserID = sharedp.getString("userid","");

        Intent intent = getIntent();
        A = intent.getStringExtra("A");
        RoleName = intent.getStringExtra("RoleName");
        RoleName = RoleName.replace(" " , "%20");

        listView = findViewById(R.id.listview);

        getDepartments();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String ID = ((TextView) view.findViewById(R.id.departID)).getText().toString();
                finish();
                Intent intent = new Intent(SurveyManagerDepart.this,
                        SurveyListManager.class);
                intent.putExtra("DepartID",ID);
                intent.putExtra("A",A);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

        finish();
        startActivity(new Intent(SurveyManagerDepart.this,DashboardSurvey.class));
    }

    private void getDepartments()
    {
        final AlertDialog progressDialog = new SpotsDialog(this,R.style.CustomProgress);
        progressDialog.show();
        progressDialog.setCancelable(false);

        final String url = "http://"+Survey_Login.IP+"/api/get-reporting-manager-departments/"+RoleName+"/"+DeptID;
        //Toast.makeText(getApplicationContext(),url,Toast.LENGTH_LONG).show();

        StringRequest req = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response != null)
                        {
                            try {
                                progressDialog.dismiss();
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("departments");
                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    ID = object.getString("id");
                                    Name = object.getString("dep_name");
                                    ModelSMD item = new ModelSMD(ID,Name);
                                    Departlist.add(item);
                                }
                                listAdapterSMD adapter = new listAdapterSMD(
                                        Departlist, SurveyManagerDepart.this);
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

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
                        Toast.makeText(SurveyManagerDepart.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
    }

}
