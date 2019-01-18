package com.example.lubna.eticketing.Others;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lubna.eticketing.R;
import com.example.lubna.eticketing.Survey.Survey_Login;

public class MyProfile extends AppCompatActivity {
    SharedPreferences sp;
    public static final  String Pre = "MyPre";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        sp = getSharedPreferences(Survey_Login.Pre, Context.MODE_PRIVATE);
        TextView  name = (TextView) findViewById(R.id.name);
        TextView email = (TextView) findViewById(R.id.email);
        TextView Eid = (TextView) findViewById(R.id.eid);
        TextView department = (TextView) findViewById(R.id.depart);
        TextView role = (TextView) findViewById(R.id.role);
        Eid.setText("   Employee ID: " +sp.getString("userid", ""));
        name.setText("   Employee Name: " +sp.getString("username", ""));
        email.setText("   Email: " +sp.getString("email", ""));
        department.setText("   Department: " +sp.getString("userdepart", ""));
        role.setText("   Role: "+sp.getString("role_name",""));

        Button dashboard = (Button) findViewById(R.id.btnDashboard);
        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                startActivity(new Intent(getApplicationContext(), MainMenu.class));
            }
        });

        Button refresh = (Button) findViewById(R.id.btnRefresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                startActivity(getIntent());
            }
        });

        Button logout = (Button) findViewById(R.id.btnLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences pref =  getSharedPreferences(Pre, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.apply();
                finish();
                startActivity(new Intent(MyProfile.this,Survey_Login.class));
            }
        });
    }
    @Override
    public void onBackPressed () {
        Intent intent = new Intent(MyProfile.this, MainMenu.class);
        startActivity(intent);
        finish();
    }
}
