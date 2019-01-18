package com.example.lubna.eticketing.Ticketing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

public class AssignTickets extends AppCompatActivity
        implements MultiSelectSpinner.OnMultipleItemsSelectedListener{

    //Submit Button
    private Button Submit;

    private Spinner Fspinner;
    private String name,A,id;
    private ArrayList<String> Assign;
    private ArrayAdapter<CharSequence> adapter;
    private String labelName;

    //Second
    private RelativeLayout SecondSpinner;
    private Spinner Department,ThirdParty,OwnEmployee;
    private ArrayList<String> DepartmentList,DepartmentIDList,ThirdPartyList,ThirdPartyIDList,OwnEmployeeList,OwnEmployeeIDList;

    //post Data
    private String TicketID,Rejected_Tic_ID,DepartID = "",PartyID = "",Option,Emp_ID = "";
    private final String PostURL = "http://"+Survey_Login.IP+"/api/assign-ticket-process";

    private SharedPreferences sharedp;
    private String UserID;

    MultiSelectSpinner multiSelectSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_tickets);

        //Multi-Select Spinner
        //String[] array = {"None", "Apple", "Google", "Facebook", "Tesla", "IBM", "Twitter"};
        multiSelectSpinner = (MultiSelectSpinner) findViewById(R.id.spinner);


        sharedp = getSharedPreferences(Survey_Login.Pre, Context.MODE_PRIVATE);
        UserID = sharedp.getString("userid","");

        //Assign Ticket Submit Button
        Submit = findViewById(R.id.btnSubmit);

        Intent intent = getIntent();
        TicketID = intent.getStringExtra("T_ID");
        Rejected_Tic_ID = intent.getStringExtra("ID");
        //Toast.makeText(getApplicationContext(),ID,Toast.LENGTH_LONG).show();

        //First Spinner Data
        Fspinner = findViewById(R.id.FirstSpinner);
        Assign = new ArrayList<>();
        adapter = ArrayAdapter.createFromResource(this, R.array.assign,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        Fspinner.setAdapter(adapter);


        //Second Spinner Data
        SecondSpinner = findViewById(R.id.SecondSpinner);
        Department = new Spinner(getApplicationContext());
        ThirdParty = new Spinner(getApplicationContext());
        OwnEmployee = findViewById(R.id.Spinner_OwnEmp);

        DepartmentList = new ArrayList<>();
        DepartmentIDList = new ArrayList<>();
        ThirdPartyList = new ArrayList<>();
        ThirdPartyIDList = new ArrayList<>();
        OwnEmployeeList = new ArrayList<>();
        OwnEmployeeIDList = new ArrayList<>();

        //First Spinner Code
        Fspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                labelName = (String) parent.getItemAtPosition(position);

                if (labelName.equals("Own Employees"))
                {
                    if (ThirdParty.getVisibility() == View.VISIBLE)
                    {
                        SecondSpinner.removeView(ThirdParty);
                    }
                    if (Department.getVisibility() == View.VISIBLE)
                    {
                        SecondSpinner.removeView(Department);
                    }

                    RelativeLayout.LayoutParams lp = new
                            RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);

                    multiSelectSpinner.setLayoutParams(lp);
                    multiSelectSpinner.setVisibility(multiSelectSpinner.VISIBLE);
                    //OwnEmployee.setVisibility(OwnEmployee.VISIBLE);
                    //SecondSpinner.addView(multiSelectSpinner);
                }
                else if (labelName.equals("Department"))
                {
                    multiSelectSpinner.setVisibility(View.GONE);
                    if (ThirdParty.getVisibility() == View.VISIBLE)
                    {
                        SecondSpinner.removeView(ThirdParty);
                    }
                    /*if (multiSelectSpinner.getVisibility() == View.VISIBLE)
                    {
                        SecondSpinner.removeView(multiSelectSpinner);
                        //multiSelectSpinner.setVisibility(View.GONE);
                    }*/

                    ViewGroup.LayoutParams lp = new
                            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);

                    Department.setLayoutParams(lp);
                    Department.setVisibility(Department.VISIBLE);
                    SecondSpinner.addView(Department);
                }
                else if (labelName.equals("Third Party"))
                {
                    multiSelectSpinner.setVisibility(View.GONE);
                    /*if (multiSelectSpinner.getVisibility() == View.VISIBLE)
                    {
                        SecondSpinner.removeView(multiSelectSpinner);
                        //multiSelectSpinner.setVisibility(View.GONE);
                    }*/
                    if (Department.getVisibility() == View.VISIBLE)
                    {
                        SecondSpinner.removeView(Department);
                    }

                    ViewGroup.LayoutParams lp = new
                            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);

                    ThirdParty.setLayoutParams(lp);
                    ThirdParty.setVisibility(ThirdParty.VISIBLE);
                    SecondSpinner.addView(ThirdParty);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getOwnEmployee();
        OwnEmployee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Emp_ID = OwnEmployeeIDList.get(i);
                Option = "own-employees";
                //Toast.makeText(getApplicationContext(),Emp_ID + " " + TicketID + " " + Option,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getThirdParties();
        ThirdParty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PartyID = ThirdPartyIDList.get(i);
                Option = "third-parties";
                //Toast.makeText(getApplicationContext(),PartyID + " " + TicketID + " " + Option,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getDepartments();
        Department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                DepartID = DepartmentIDList.get(i);
                Option = "departments";
                //Toast.makeText(getApplicationContext(),DepartID + " " + TicketID + " " + Option,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),Rejected_Tic_ID,Toast.LENGTH_LONG).show();
                AssignTicket();
            }
        });

    }

    private void getOwnEmployee()
    {
        final String url = "http://"+Survey_Login.IP+"/api/assign-ticket/own_employee/"+UserID;

        StringRequest req = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("employees");
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject object = jsonArray.getJSONObject(i);
                                OwnEmployeeList.add(object.getString("name"));
                                OwnEmployeeIDList.add(object.getString("id") + " " + object.getString("name"));

                                //multiSelectSpinner.setItems(OwnEmployeeList);
                                multiSelectSpinner.setItems(OwnEmployeeIDList);
                                multiSelectSpinner.hasNoneOption(true);
                                multiSelectSpinner.setSelection(new int[]{0});
                                multiSelectSpinner.setListener(AssignTickets.this);
                            }

                            /*OwnEmployee.setAdapter(new ArrayAdapter<String>(AssignTickets.this,
                                    android.R.layout.simple_spinner_dropdown_item,OwnEmployeeList));*/


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

    private void getThirdParties()
    {
        final String url = "http://"+Survey_Login.IP+"/api/assign-ticket/third_party/"+UserID;

        StringRequest req = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("thirdparties");
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject object = jsonArray.getJSONObject(i);
                                ThirdPartyList.add(object.getString("name"));
                                ThirdPartyIDList.add(object.getString("id"));
                            }

                            ThirdParty.setAdapter(new ArrayAdapter<String>(AssignTickets.this,
                                    android.R.layout.simple_spinner_dropdown_item,ThirdPartyList));
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

    private void getDepartments()
    {
        final String url = "http://"+Survey_Login.IP+"/api/assign-ticket/department/"+UserID;

        StringRequest req = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("departments");
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject object = jsonArray.getJSONObject(i);
                                DepartmentList.add(object.getString("dep_name"));
                                DepartmentIDList.add(object.getString("id"));
                            }

                            Department.setAdapter(new ArrayAdapter<String>(AssignTickets.this,
                                    android.R.layout.simple_spinner_dropdown_item,DepartmentList));
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

    private void AssignTicket()
    {
        StringRequest req = new StringRequest(Request.Method.POST, PostURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("success"))
                            {
                                Toast.makeText(getApplicationContext(),"Data submitted",Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(),Dashboard.class));
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Data not submitted",Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("option", Option);
                map.put("ticket_id",TicketID);
                map.put("manager_id", UserID);
                map.put("department_id", DepartID);
                map.put("thirdparty_id", PartyID);
                map.put("employees", Emp_ID);
                map.put("rejected_id", Rejected_Tic_ID);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
    }

    @Override
    public void onBackPressed() {

        finish();
        startActivity(new Intent(getApplicationContext(),Dashboard.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void selectedIndices(List<Integer> indices) {
    }

    @Override
    public void selectedStrings(List<String> strings) {

        String abc = "";
        for (String s : strings)
        {
            abc += s.substring(0,3) + ",";
        }
        if (abc.endsWith(","))
        {
            abc = abc.substring(0, abc.length() - 1);
        }

        final String finalAbc = abc;
        //Toast.makeText(getApplicationContext(),finalAbc,Toast.LENGTH_LONG).show();
        StringRequest req = new StringRequest(Request.Method.POST, PostURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("success"))
                            {
                                Toast.makeText(getApplicationContext(),"Data submitted",Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(),Dashboard.class));
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Data not submitted",Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("option", "own-employees");
                map.put("ticket_id",TicketID);
                map.put("manager_id", UserID);
                map.put("department_id", DepartID);
                map.put("thirdparty_id", PartyID);
                map.put("employees", finalAbc);
                map.put("rejected_id", Rejected_Tic_ID);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
    }
}
