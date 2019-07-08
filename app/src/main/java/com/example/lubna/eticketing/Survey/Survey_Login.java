package com.example.lubna.eticketing.Survey;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lubna.eticketing.Others.MainMenu;
import com.example.lubna.eticketing.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Survey_Login extends Activity implements View.OnClickListener {
    public static final String Pre = "MyPre";

    //Server IP Address
    public static String IP = "172.16.10.72";
    public static String IP_ALI = "172.16.10.217";
    public static final String LOGIN_URL = "http://" + IP + "/api/login";
    public static final String KEY_USERNAME = "email";
    public static final String KEY_PASSWORD = "password";
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private String username;
    private String password;
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    ScrollView mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey__login);
        buttonLogin = (Button) findViewById(R.id.surveylogin);
        editTextUsername = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);
        sp = getSharedPreferences(Pre, Context.MODE_PRIVATE);
        buttonLogin.setOnClickListener(this);

        mainLayout = (ScrollView) findViewById(R.id.mainLayout);
    }

    public boolean CheckFieldValidation() {

        boolean valid = true;

        if (editTextUsername.getText().toString().equals("")) {
            editTextUsername.setError("Enter Valid UserName");
            editTextUsername.requestFocus();
            valid = false;
        } else if (editTextPassword.getText().toString().equals("")) {
            editTextPassword.setError("Enter Valid Password");
            editTextPassword.requestFocus();
            valid = false;
        }

        return valid;
    }

    private void userLogin() {
        username = editTextUsername.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        final AlertDialog progressDialog = new SpotsDialog(this, R.style.CustomProgress);
        progressDialog.show();
        progressDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject json = new JSONObject(response);
                                String msg = json.getString("msg");
                                if (msg.equals("success")) {
                                    progressDialog.dismiss();
                                    String id = json.getString("user_id");
                                    edit = sp.edit();
                                    edit.putString("userid", json.getString("user_id"));
                                    edit.putString("username", json.getString("user_name"));
                                    edit.putString("role_id", json.getString("role_id"));
                                    edit.putString("role_name", json.getString("role_name"));
                                    edit.putString("userdepart", json.getString("department_name"));
                                    edit.putString("userdepartid", json.getString("department_id"));
                                    edit.putString("parentid", json.getString("parent_department_id"));
                                    edit.putString("email", username);
                                    edit.putBoolean("saveLogin", true);
                                    edit.commit();
                                    edit.apply();
                                    openProfile();
                                } else if (msg.equals("error")) {
                                    Toast.makeText(Survey_Login.this, "Your Email Or Password is Incorrect", Toast.LENGTH_LONG).show();
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
                        } else {
                            Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Survey_Login.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(KEY_USERNAME, username);
                map.put(KEY_PASSWORD, password);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void openProfile() {
        finish();
        Intent intent = new Intent(this, MainMenu.class);
        intent.putExtra(KEY_USERNAME, username);
        startActivity(intent);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null, otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {

            CheckFieldValidation();
            userLogin();
            return true;
        } else if (networkInfo == null) {
            //Toast.makeText(Survey_Login.this,"No internet Connection",Toast.LENGTH_LONG).show();
            Snackbar snackbar = Snackbar
                    .make(mainLayout, "No Internet Connection", Snackbar.LENGTH_LONG);

            snackbar.show();
            return false;
        }
        return false;

    }

    public void onClick(View v) {

        if (v == buttonLogin) {
            isNetworkAvailable();
        }


    }
    /*@Override
    public void onBackPressed () {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
        finish();
    }*/


}

