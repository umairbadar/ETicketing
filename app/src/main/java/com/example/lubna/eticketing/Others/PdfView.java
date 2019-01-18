package com.example.lubna.eticketing.Others;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.lubna.eticketing.R;
import com.example.lubna.eticketing.Survey.Survey_Login;

import java.net.URI;

public class PdfView extends AppCompatActivity {

    private WebView PDFView;
    private String SiteName,URL;
    private Context context  = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);


        PDFView = findViewById(R.id.PDF_WebView);

        Intent intent = getIntent();
        SiteName = intent.getStringExtra("SiteName");

        URL = "http://"+Survey_Login.IP +"/HSSEPDF/"+SiteName+".pdf";


        Intent intent1 = new Intent(Intent.ACTION_VIEW,Uri.parse(URL));
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.setPackage("com.android.chrome");

        try
        {
            context.startActivity(intent1);
        }
        catch (ActivityNotFoundException ex)
        {
            intent1.setPackage(null);
            context.startActivity(intent1);
        }

    }

}
