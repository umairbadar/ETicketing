package com.example.lubna.eticketing.Survey;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lubna.eticketing.Others.MyProfile;
import com.example.lubna.eticketing.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static android.content.ContentValues.TAG;


public class TakeSurvey extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 1;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationManager locationManager;
    boolean isGPSEnabled = false;
    String SurveyID;
    Intent intent;
    SharedPreferences sharedp;
    String Hcount ,Ocount;
    private Button btn1;
    private Button btn2, btn3;
    int Qcount=0;
    int Tcount=0;
    TextView t[];
    private   Button myButton;
    LinearLayout root ,root1;
    private AlertDialog pDialog;
    String Tittle,Question;
    String  optionlist;
    String[] separated;
    int pos;
    private static final int RB_ID = 100;
    private static int id_;
    private RadioGroup rg ;
    private  RadioButton[] rb;
    private EditText editText , editText1,e2;
    private RadioButton radioSexButton;
    int pos1=0;
    int prb=0;
    int q=0;
    int k=0;
    int y=0;
    private String Varquestions="";
    private List <EditText> allprblm = null;
    private List <EditText> allEds = null;
    private List<RadioGroup> allradios=null;
    private static int w =100;
    private   String[] strings;
    private  String[] stringradio;
    private  String[] stringstext;
    private  String[] stringsqid;
    ArrayList<String>stringsproblm=null;
    ArrayList<String>stringradios=null;
    ArrayList<String>stringstexts=null;
    ArrayList<String> minqtyList=null;
    private static final String URL = "http://"+Survey_Login.IP+"/api/businessanswer";
    public  final String QuestionS = "Question";
    public static final String TextviewAns = "TextviewAns";
    public static final String Selectedoption = "radioSelectedoption";
    public static final String RadioProblem= "radioProblem";
    public static final String Publish_id= "publish_id";
    public static final String Presents= "present";
    String Publish = "";
    private   String  Varqust="";
    private  String Vartest="";
    private   String Varpblm = "";
    private   String Varradio="";
    private String UserID,DeptID,ParentID;
    private EditText DealerStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_survey);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        DealerStatus = findViewById(R.id.ettDealerStatus);

        intent = getIntent();

        sharedp = getSharedPreferences(Survey_Login.Pre, Context.MODE_PRIVATE);
        UserID = sharedp.getString("userid", "");
        SurveyID= intent.getStringExtra("SurveyID");
        DeptID= intent.getStringExtra("DeptID");
        ParentID= intent.getStringExtra("ParentID");


        allEds = new ArrayList<EditText>();
        minqtyList = new ArrayList<String>();
        allradios=new ArrayList<RadioGroup>();
        allprblm = new ArrayList<EditText>();
        stringsproblm   = new ArrayList<String>();
        stringradios  = new ArrayList<String>();
        stringstexts   = new ArrayList<String>();

        SurveyList();
        btn1 = (Button) findViewById(R.id.buttonD);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnHomeClick();
            }
        });

        btn2 = (Button) findViewById(R.id.buttonB);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 if (rg.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please fill all multiple choice questions", Toast.LENGTH_SHORT).show();
                }
                /*else if (editText1.getText().toString().length() == 0) {
                    *//*Toast.makeText(getApplicationContext(), "Please fill all problems", Toast.LENGTH_SHORT).show();
                    editText1.setError("this problem required");*//*
                    editText1.setText("N/A");
                }*/
                else
                {

                        strings = new String[allprblm.size()];
                        for (int i = 0; i < allprblm.size(); i++) {
                            strings[i] = allprblm.get(i).getText().toString().trim();
                            if (editText1.getText().toString().length() > 0)
                            {
                                stringsproblm.add(strings[i]);
                            }
                            else
                            {
                                stringsproblm.add("N/A");
                            }

                        }


                    stringradio = new String[allradios.size()];
                    for (int i = 0; i < allradios.size(); i++) {
                        int selectedId = allradios.get(i).getCheckedRadioButtonId();
                        RadioButton radiochecked1 =(RadioButton) allradios.get(i).findViewById(selectedId);
                        stringradio[i] = String.valueOf(radiochecked1.getText().toString());
                        stringradios.add(stringradio[i]);
                    }
                      stringstext = new String[allEds.size()];
                    for (int i = 0; i < allEds.size(); i++) {

                        stringstext[i] = allEds.get(i).getText().toString().trim();
                        stringstexts.add(stringstext[i]);

                    }


                }

                fetchLocation();
                SendData();

            }
        });


    }

    public void SurveyList()
    {
        pDialog = new SpotsDialog(this,R.style.CustomProgress);
        pDialog.show();
        pDialog.setCancelable(false);
        final String url = ("http://"+Survey_Login.IP+"/api/getsurveys/"+UserID+"/"+SurveyID);
        StringRequest req = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {

                                JSONObject jsonObj = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                                JSONArray c = jsonObj.getJSONArray("surveylist");
                                for (int i = 0; i < c.length(); i++) {
                                    Hcount= String.valueOf(c.length());
                                    JSONObject obj = c.getJSONObject(i);
                                    JSONObject obj1 = obj.getJSONObject("surveydetail");
                                    Tittle=obj1.getString("title");
                                    start(Tittle);
                                    JSONArray ob = obj1.getJSONArray("question");
                                    for (int j = 0; j < ob.length(); j++) {
                                        JSONObject js = ob.getJSONObject(j);
                                        stringsqid = new String[ob.length()];
                                        String qid=js.getString("id");
                                        stringsqid[j]=qid;
                                        minqtyList.add(qid);
                                        Question = js.getString("title");
                                        String questiontype=js.getString("question_type");

                                        quest(Question);

                                        if (questiontype.equals("radio"))
                                        {  Qcount++;
                                            JSONArray ob2 = js.getJSONArray("option_name");
                                            optionlist=ob2.toString();

                                            separated =optionlist.split(",");
                                            Ocount = String.valueOf(Arrays.asList(separated).size());
                                            ansoptn(separated ,Ocount, Qcount);

                                        }
                                        else if (questiontype.equals("text"))
                                        {
                                            text2();
                                            Tcount++;
                                        }

                                    }

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
                        Toast.makeText(TakeSurvey.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private void start(String Tittle) {
        root = (LinearLayout)findViewById(R.id.linearLayout2);
        TextView tvtittle = new TextView(this);
        tvtittle.setText(Tittle);
        tvtittle.setTypeface(null, Typeface.BOLD);
        tvtittle.setTextColor(Color.parseColor("#FFFFFF"));
        tvtittle.setTextSize(16);
        tvtittle.setPadding(0,50,0,50);
        tvtittle.setBackgroundColor(Color.parseColor("#42A5F5"));
        tvtittle.setGravity(Gravity.CENTER);
        root.addView(tvtittle);
    }
    private void btnHomeClick() {
        Intent intent = new Intent(this, DashboardSurvey.class);
        startActivity(intent);
        finish();
    }
    private void quest(String Question)
    {
        TextView tvquestion1 = new TextView(this);
        tvquestion1.setText(Question);
        tvquestion1.setTypeface(null, Typeface.BOLD);
        tvquestion1.setTextSize(14);
        tvquestion1.setPadding(50,80,0,20);
        tvquestion1.setTextColor(Color.parseColor("#424242"));
        root.addView(tvquestion1);
    }
    private void text2()
    {
        root=(LinearLayout)findViewById(R.id.linearLayout2);
        editText = new EditText(this);
        allEds.add(editText);
        editText.setHint("Please enter your opinion");
        editText.setTypeface(null, Typeface.NORMAL);
        editText.setTextColor(Color.parseColor("#424242"));
        editText.setTextSize(14);
        editText.setId(y+w);
        editText.setPadding(50,50,0,50);
        root.addView(editText);
        y++;
    }
    private void ansoptn(String[] separated ,String  Ocount ,int Qcount){
        rb = new RadioButton[Integer.parseInt(Ocount)];
        rg= new RadioGroup(this);
        rg.setOrientation(RadioGroup.VERTICAL);
        rg.setId(w+q);
        allradios.add(rg);
        for(int i=0; i<Integer.parseInt(Ocount); i++){
            rb[i]  = new RadioButton(this);

            rb[i].setId(i );
            rg.addView(rb[i]);
            String splitterString=separated[i].replaceAll("[\\[\\]]","").replaceAll("\"","");
            rb[i].setText(splitterString);
            rb[i].setTextSize(12);
            k++;
        }

        root.addView(rg);
        q++;
        editText1 = new EditText(this);
        allprblm.add(editText1);
        editText1.setHint("Please enter your Problem");
        editText1.setTypeface(null, Typeface.NORMAL);
        editText1.setTextColor(Color.parseColor("#424242"));
        editText1.setTextSize(14);
        editText1.setId(prb+200);
        editText1.setPadding(50,50,0,50);
        root.addView(editText1);

        prb++;


    }
    public void SendData() {
        Publish = SurveyID;
        final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        for (int i = 0; i < stringstexts.size(); i++) {
            if (Vartest == null) {
                Vartest =stringstexts.get(i);
            } else {
                Vartest = Vartest + "," + stringstexts.get(i);
            }
        }


        for (int i = 0; i < stringsproblm.size(); i++) {
            if (Varpblm.isEmpty()) {
                Varpblm = stringsproblm.get(i);
            } else {
                Varpblm = Varpblm + "," + stringsproblm.get(i);
            }
            //Toast.makeText(getApplicationContext(), Varpblm, Toast.LENGTH_SHORT).show();
        }

        for (int i = 0; i < stringradios.size(); i++) {
            if (Varradio == "") {
                Varradio = stringradios.get(i);
            } else {
                Varradio = Varradio + "," + stringradios.get(i);
            }
        }


        for(int i=0; i<minqtyList.size(); i++) {
            if (Varqust == "") {
                Varqust = minqtyList.get(i);
            } else {
                Varqust = Varqust + "," + minqtyList.get(i);
            }
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        Toast.makeText(TakeSurvey.this,"Survey Submitted",Toast.LENGTH_LONG).show();
                        openProfile();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      Toast.makeText(TakeSurvey.this,"Survey not Submitted",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                HashMap<String ,String> params=new HashMap<>();


                params.put(TextviewAns,Vartest);
                params.put(RadioProblem,Varpblm);
                params.put(Selectedoption, Varradio);
                params.put(QuestionS,Varqust);
                params.put(Publish_id,SurveyID);
                params.put(Presents,DealerStatus.getText().toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed () {
        Intent intent = new Intent(this, DashboardSurvey.class);
        startActivity(intent);
        finish();
    }
    private void openProfile() {
        Intent intent = new Intent(this, DashboardSurvey.class);
        finish();
        startActivity(intent);
    }


    private void fetchLocation() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(TakeSurvey.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(TakeSurvey.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(this)
                        .setTitle("Required Location Permission")
                        .setMessage("You have to give this permission to access the location")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(TakeSurvey.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_COARSE_LOCATION);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(TakeSurvey.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_COARSE_LOCATION);
                checkGPSorNetwork();
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();

                                final String lat = String.valueOf(latitude);
                                final String lng = String.valueOf(longitude);

                                final String url = "http://"+Survey_Login.IP+"/api/employee-acknowledge";

                                StringRequest req = new StringRequest(Request.Method.POST, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    String msg = jsonObject.getString("msg");
                                                    if (msg.equals("success"))
                                                    {
                                                        //Toast.makeText(getApplicationContext(), "Data Sent", Toast.LENGTH_LONG).show();
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(getApplicationContext(), "Error in Location", Toast.LENGTH_LONG).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("employee_id", UserID);
                                        map.put("department_id", DeptID);
                                        map.put("latitude", lat);
                                        map.put("longitude", lng);
                                        map.put("department_parent_id", ParentID);
                                        map.put("publish_survey_id", SurveyID);
                                        return map;
                                    }
                                };

                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                requestQueue.add(req);

                                //Toast.makeText(getApplicationContext(), "Latitude: " + String.valueOf(latitude) + "\n" + "Longitude: " + String.valueOf(longitude), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "GPS is not able to dectect your coordinates", Toast.LENGTH_LONG).show();
                            }
                        }
                    });


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_COARSE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

            }
        }
    }

    public void checkGPSorNetwork() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        isGPSEnabled = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);

        String gpsMsg = "GPS is disabled in your device.\nWould you like to enable it?";

        if (!isGPSEnabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Use Location?");
            dialog.setMessage(gpsMsg);

            dialog.setPositiveButton("GPS Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Intent gpsintent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(gpsintent);
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            dialog.show();
        }

    }


}




