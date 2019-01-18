package com.example.lubna.eticketing.Ticketing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lubna.eticketing.R;
import com.example.lubna.eticketing.Survey.Survey_Login;
import com.example.lubna.eticketing.Ticketing.Dashboard;
import com.example.lubna.eticketing.Ticketing.TicketListManager;
import com.example.lubna.eticketing.Ticketing.TicketListing;
import com.example.lubna.eticketing.Ticketing.TicketManagerDepart;

public class TransferredTickets extends AppCompatActivity {

    private LinearLayout Pending,Accepted,Rejected;
    private String RoleName,PendingCount,AcceptedCount,RejectedCount;
    private TextView txtPending,txtAccepted,txtRejected;
    private SharedPreferences sharedp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferred_tickets);

        sharedp = getSharedPreferences(Survey_Login.Pre, Context.MODE_PRIVATE);
        RoleName = sharedp.getString("role_name","");
        //Toast.makeText(getApplicationContext(),RoleName,Toast.LENGTH_LONG).show();

        txtAccepted = findViewById(R.id.txtAccepted);
        txtPending = findViewById(R.id.txtPending);
        txtRejected = findViewById(R.id.txtRejected);

        Intent intent = getIntent();
        PendingCount = intent.getStringExtra("PendingCount");
        AcceptedCount = intent.getStringExtra("AcceptedCount");
        RejectedCount = intent.getStringExtra("RejectedCount");

        Pending = findViewById(R.id.layoutPending);
        Accepted = findViewById(R.id.layoutAccept);
        Rejected = findViewById(R.id.layoutReject);

        if (RoleName.equals("reporting manager"))
        {
            txtPending.setText("Click here");
            Pending.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    Intent intent = new Intent(getApplicationContext(),TicketManagerDepart.class);
                    intent.putExtra("A","transferred");
                    intent.putExtra("RoleName",RoleName);
                    startActivity(intent);

                }
            });
            txtRejected.setText("Click here");
            Rejected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    finish();
                    Intent intent = new Intent(getApplicationContext(),TicketManagerDepart.class);
                    intent.putExtra("A","Rejected");
                    intent.putExtra("RoleName",RoleName);
                    startActivity(intent);
                }
            });

            txtAccepted.setText("Click here");
            Accepted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    finish();
                    Intent intent = new Intent(getApplicationContext(),TicketManagerDepart.class);
                    intent.putExtra("A","Accepted");
                    intent.putExtra("RoleName",RoleName);
                    startActivity(intent);
                }
            });
        }
        else
        {
            txtPending.setText(PendingCount);
            Pending.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    Intent intent = new Intent(getApplicationContext(),TicketListing.class);
                    intent.putExtra("A","transferred");
                    //intent.putExtra("RoleName",RoleName);
                    startActivity(intent);

                }
            });
            txtRejected.setText(RejectedCount);
            Rejected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    Intent intent = new Intent(getApplicationContext(),TicketListing.class);
                    intent.putExtra("A","Rejected");
                    //intent.putExtra("RoleName",RoleName);
                    startActivity(intent);

                }
            });
            txtAccepted.setText(AcceptedCount);
            Accepted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    finish();
                    Intent intent = new Intent(getApplicationContext(),TicketListing.class);
                    intent.putExtra("A","Accepted");
                    //intent.putExtra("RoleName",RoleName);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {

        finish();
        startActivity(new Intent(getApplicationContext(),Dashboard.class));
    }
}
