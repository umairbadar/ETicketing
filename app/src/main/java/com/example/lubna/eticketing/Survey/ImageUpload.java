package com.example.lubna.eticketing.Survey;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lubna.eticketing.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dmax.dialog.SpotsDialog;


public class ImageUpload extends AppCompatActivity implements View.OnClickListener {

    private Button buttonChoose;
    private Button buttonUpload;
    SharedPreferences sp;
    private ImageView imageView;
    private EditText editTextTittle;
    String srId = "";
    private Bitmap bitmap;
    Intent i;
    String Id =null;
    /** The default socket timeout in milliseconds */
    public static final int DEFAULT_TIMEOUT_MS = 2500;

    /** The default number of retries */
    public static final int DEFAULT_MAX_RETRIES = 0;

    /** The default backoff multiplier */
    public static final float DEFAULT_BACKOFF_MULT = 1f;
    private int PICK_IMAGE_REQUEST = 1;
    private String UPLOAD_URL ="http://"+Survey_Login.IP+"/api/save-survey-image";
    private String KEY_IMAGE = "encoded_image";
    private String KEY_NAME = "image_title";
    private String KEY_User = "user_id";
    private String KEY_SurveyId = "publish_survey_id";

    private static final int STORAGE_PERMISSION_CODE = 2342;
    private Uri filePath;

    //Display image code

    public static final String TAG_IMAGE_URL = "image";
    public static final String TAG_NAME = "title";
    private GridView gridView;
    private ArrayList<String> images;
    private ArrayList<String> names;

    private SharedPreferences sharedp;
    private String UserID,SurveyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);



        sharedp = getSharedPreferences(Survey_Login.Pre, Context.MODE_PRIVATE);
        UserID = sharedp.getString("userid", "");

        Intent intent = getIntent();
        SurveyID = intent.getStringExtra("SurveyID");

        //display image code
        gridView = (GridView) findViewById(R.id.gridView);

        images = new ArrayList<>();
        names = new ArrayList<>();

        //upload image code
        sp = getSharedPreferences(Survey_Login.Pre, Context.MODE_PRIVATE);
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.BtnUploadImg);
        buttonUpload.setEnabled(false);
        editTextTittle = (EditText) findViewById(R.id.txtImgTitle);
        imageView  = (ImageView) findViewById(R.id.Img);
        i = getIntent();

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

        requestStoragePermission();
        getImages();

    }

    private void getImages()
    {

        final String SHOW_IMAGE_URL = "http://"+Survey_Login.IP+"/api/get-survey-images/"+SurveyID;
        final AlertDialog progressDialog = new SpotsDialog(this,R.style.CustomProgress);
        progressDialog.show();
        progressDialog.setCancelable(false);


        StringRequest req = new StringRequest(Request.Method.GET, SHOW_IMAGE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("survey_images");
                            String msg = jsonObject.getString("msg");

                            if (msg.equals("fail"))
                            {
                                //progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    //progressDialog.dismiss();
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    images.add("http://" + Survey_Login.IP + "/" + object.getString("image"));
                                    names.add(object.getString(TAG_NAME));
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        GridViewAdapter gridViewAdapter = new GridViewAdapter(getApplicationContext(),images,names);
                        gridView.setAdapter(gridViewAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
                buttonUpload.setEnabled(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void requestStoragePermission()
    {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_PERMISSION_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getApplicationContext(),"Permission Granted",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Permission not Granted",Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getPath(Uri uri)
    {
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);

        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id},null
        );
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    private void uploadImage()
    {
        String name = editTextTittle.getText().toString().trim();
        String path = getPath(filePath);

        try
        {
            String Uploadid = UUID.randomUUID().toString();

            new MultipartUploadRequest(this, Uploadid, UPLOAD_URL)
                    .addFileToUpload(path,"image")
                    .addParameter("image_title",name)
                    .addParameter("user_id",UserID)
                    .addParameter("publish_survey_id",SurveyID)
                    .setMaxRetries(10)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .startUpload();
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),DashboardSurvey.class);

        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {

        if(v == buttonChoose){
            showFileChooser();
        }

        if(v == buttonUpload){

            String title = editTextTittle.getText().toString().trim();
            if (!title.isEmpty())
            {
                uploadImage();
                Intent intent = getIntent();
                startActivity(intent);
                getImages();
                //finish();
            }
            else
            {
                editTextTittle.setError("Please enter Image Title");
                editTextTittle.requestFocus();
            }


        }
    }
}

