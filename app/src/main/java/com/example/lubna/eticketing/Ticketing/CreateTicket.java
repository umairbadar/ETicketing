package com.example.lubna.eticketing.Ticketing;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

public class CreateTicket extends AppCompatActivity {
    Spinner typename;
    ArrayAdapter<String> TypeArray;
    ArrayAdapter<String> TypeidArray;
    ArrayAdapter<String> SitesArray;
    ArrayAdapter<String> SitesidArray;
    SharedPreferences sharedp;
    String UserID, TYPEID, SITEID, SITENAME, departid, Parent_Depart_ID;
    private EditText Subject, Description;
    final String URLsaveTicket = "http://" + Survey_Login.IP + "/api/create-ticket";
    private Button BtnChooseImg;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 2342;
    private Uri filePath;
    private Bitmap bitmap;
    private ImageView imageView;
    private Button submit;
    private String path;

    //Custom Spinner
    String[] options;
    Spinner spinner;
    private ArrayList<String> sites;

    private String TAG = TicketListing.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ticket);

        spinner = (Spinner) findViewById(R.id.spinner);
        sites = new ArrayList<>();

        //ticketing image upload
        BtnChooseImg = findViewById(R.id.buttonChoose);
        imageView = findViewById(R.id.Img);
        //End

        sharedp = getSharedPreferences(Survey_Login.Pre, Context.MODE_PRIVATE);
        departid = sharedp.getString("userdepartid", "");
        UserID = sharedp.getString("userid", "");
        Parent_Depart_ID = sharedp.getString("parentid", "");


        typename = (Spinner) findViewById(R.id.spinnertype);

        Button cancel = (Button) findViewById(R.id.btncancel);
        submit = (Button) findViewById(R.id.buttonsubmitticket);
        //submit.setEnabled(false);

        Subject = (EditText) findViewById(R.id.editboxsubject);
        Description = (EditText) findViewById(R.id.editboxdescription);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(i);
                finish();
            }
        });

        TypeArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        TypeidArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        typename.setAdapter(TypeArray);

        SitesidArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);

        getTypes();
        typename.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, final int postion, long l) {
                TYPEID = TypeidArray.getItem(postion);
                //Toast.makeText(getApplicationContext(),TYPEID,Toast.LENGTH_LONG).show();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        getSites();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SITEID = String.valueOf(spinner.getSelectedItem());
                SITENAME = SitesidArray.getItem(i);
                //Toast.makeText(getApplicationContext(),SITEID + " " + SITENAME,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //createticketrsubmit();
                uploadImage();
            }
        });

        BtnChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showFileChooser();
            }
        });

        requestStoragePermission();


    }

    public void getTypes() {
        final String url = "http://" + Survey_Login.IP + "/api/ticket-type";
        StringRequest req = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject json = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                                JSONArray c = (JSONArray) json.get("ticket_types");
                                for (int i = 0; i < c.length(); i++) {
                                    JSONObject obj = (JSONObject) c.get(i);
                                    TypeArray.add(obj.getString("Name"));
                                    TypeidArray.add(obj.getString("id"));
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
                            Toast.makeText(getApplicationContext(),
                                    "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CreateTicket.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(CreateTicket.this);
        requestQueue.add(req);
    }

    public void getSites() {
        final String url = "http://" + Survey_Login.IP + "/api/petro-sites";
        StringRequest req = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject json = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                                JSONArray c = (JSONArray) json.get("sites");
                                for (int i = 0; i < c.length(); i++) {
                                    JSONObject obj = (JSONObject) c.get(i);
                                    sites.add(obj.getString("name"));
                                    SitesidArray.add(obj.getString("location_id"));
                                }

                                /*spinner.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                                        android.R.layout.simple_spinner_dropdown_item, sites));*/

                                ArrayAdapter adapterArea = new ArrayAdapter(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, sites);
                                spinner.setAdapter(adapterArea);
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
                            Toast.makeText(getApplicationContext(),
                                    "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CreateTicket.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(CreateTicket.this);
        requestQueue.add(req);
    }

    private void createticketrsubmit() {

        final String SUBJECT = Subject.getText().toString().trim();
        final String DESCRIPTION = Description.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLsaveTicket,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject json = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                                String msg = json.getString("msg");
                                if (msg.equals("success")) {
                                    Toast.makeText(CreateTicket.this, "Submit Successfully", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(CreateTicket.this, Dashboard.class);
                                    startActivity(i);
                                    finish();
                                } else if (msg.equals("error")) {
                                    Toast.makeText(CreateTicket.this, "Submit Fail", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(getApplicationContext(),
                                    "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CreateTicket.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("user_id", UserID);
                map.put("ticket_title", SUBJECT);
                map.put("ticket_type_id", TYPEID);
                map.put("ticket_message", DESCRIPTION);
                map.put("site_id", SITEID);
                map.put("ticket_site_name", SITENAME);
                map.put("user_depart_id", departid);
                map.put("parent_department_id", Parent_Depart_ID);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
        finish();
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
                //submit.setEnabled(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Permission not Granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);

        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null
        );
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;

    }

    private void uploadImage() {
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        final String SUBJECT = Subject.getText().toString().trim();
        final String DESCRIPTION = Description.getText().toString().trim();
        if (bitmap != null) {
            path = getStringImage(bitmap);
        } else {
            path = "null";
        }

        StringRequest req = new StringRequest(Request.Method.POST, URLsaveTicket,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("success")) {
                                loading.dismiss();
                                Toast.makeText(getApplicationContext(), "Ticket Created!", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), Dashboard.class);
                                startActivity(i);
                                finish();
                            } else {
                                loading.dismiss();
                                Toast.makeText(getApplicationContext(), msg,
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                params.put("image", path);
                params.put("user_id", UserID);
                params.put("ticket_title", SUBJECT);
                params.put("ticket_type_id", TYPEID);
                params.put("ticket_message", DESCRIPTION);
                params.put("site_id", SITENAME);
                params.put("ticket_site_name", SITEID);
                params.put("user_depart_id", departid);
                params.put("parent_department_id", Parent_Depart_ID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req);

       /* Toast.makeText(getApplicationContext(),"Userid: " + UserID +
                " user depart id: " + departid + " parent depart id:" + Parent_Depart_ID +
                " site id: " + SITENAME + " ticket site_ name: " + SITEID +
                " ticket title: " + SUBJECT + " ticket type id: " +TYPEID +
                " ticket message: " + DESCRIPTION,Toast.LENGTH_LONG).show();*/

        /*try
        {
            String Uploadid = UUID.randomUUID().toString();

            new MultipartUploadRequest(this, Uploadid, URLsaveTicket)
                    .addFileToUpload(path,"image")
                    .addParameter("user_id",UserID)
                    .addParameter("ticket_title",SUBJECT)
                    .addParameter("ticket_type_id",TYPEID)
                    .addParameter("ticket_message",DESCRIPTION)
                    .addParameter("site_id",SITEID)
                    .addParameter("ticket_site_name",SITENAME)
                    .addParameter("user_depart_id",departid)
                    .addParameter("parent_department_id",Parent_Depart_ID)
                    .setMaxRetries(10)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .startUpload();
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

        }*/
    }
}
