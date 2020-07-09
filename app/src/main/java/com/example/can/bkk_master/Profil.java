package com.example.can.bkk_master;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.can.bkk_master.Adapter.ProfilAdapter;
import com.example.can.bkk_master.Controller.AppController;
import com.example.can.bkk_master.Pekerjaan.Pekerjaan;
import com.example.can.bkk_master.Pendidikan.Pendidikan;
import com.example.can.bkk_master.Server.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static com.example.can.bkk_master.Login.my_shared_preferences;
import static com.example.can.bkk_master.Login.session_status;

public class Profil extends AppCompatActivity {

    Boolean session = false;

    String url = Server.URL + "users_tampil.php";
    String gantifoto = Server.URL + "upload_img.php";

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_NAMA = "nama";

    String id,idu,idun,idn;
    SharedPreferences sharedpreferences;
    public final static String TAG = "Profile";
    public  static final int RequestPermissionCode  = 1 ;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    ImageView fotoProfile;
    Button ganti_Foto;

    private String Document_img1="";
    Bitmap bitmap;
    int PICK_IMAGE_REQUEST = 111;


    ListView listView;
    private String[] menu_profil = {
            "Data Pribadi",
            "Riwayat Pendidikan",
            "Riwayat Pekerjaan",
            "Ganti Password",};
    private Integer[] logo_menu_profil = {
            R.drawable.ic_account_circle_black,
            R.drawable.ic_school_black,
            R.drawable.ic_work_black,
            R.drawable.ic_key_black,};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        EnableRuntimePermissionToAccessCamera();
        sharedpreferences = this.getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

        listView = (ListView) findViewById(R.id.list_menu_profil);

        ProfilAdapter ProfilMenu = new ProfilAdapter(Profil.this, menu_profil, logo_menu_profil);
        listView.setAdapter(ProfilMenu);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idu = getIntent().getStringExtra(TAG_ID);
                idn = getIntent().getStringExtra(TAG_USERNAME);
                idun = getIntent().getStringExtra(TAG_NAMA);
                sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
                session = sharedpreferences.getBoolean(session_status, false);

                if (session) {
                    if (position == 0) {
                        Intent intent = new Intent(Profil.this, DataPribadi.class);
                        intent.putExtra(TAG_ID, idu);
                        intent.putExtra(TAG_USERNAME, idn);
                        intent.putExtra(TAG_NAMA, idun);
                        startActivity(intent);
                    } else if (position == 1) {
                        Intent intent = new Intent(Profil.this, Pendidikan.class);
                        intent.putExtra(TAG_ID, idu);
                        intent.putExtra(TAG_USERNAME, idn);
                        intent.putExtra(TAG_NAMA, idun);
                        startActivity(intent);
                    } else if (position == 2) {
                        Intent intent = new Intent(Profil.this, Pekerjaan.class);
                        intent.putExtra(TAG_ID, idu);
                        intent.putExtra(TAG_USERNAME, idn);
                        intent.putExtra(TAG_NAMA, idun);
                        startActivity(intent);
                    } else if (position == 3) {
                        Intent intent = new Intent(Profil.this, DataPribadi.class);
                        intent.putExtra(TAG_ID, id);
                        startActivity(intent);
                    }

                }
            }
        });

        ambilfoto();
        fotoProfile = (ImageView) findViewById(R.id.fotoProfile);
        fotoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pilihgambar();
            }
        });

        ganti_Foto = (Button) findViewById(R.id.ganti_Foto);
        ganti_Foto.setEnabled(false);
        ganti_Foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gantifoto();
            }
        });
    }

    private void ambilfoto()
    {
        progressDialog = new ProgressDialog(Profil.this);
        progressDialog.setMessage("Proses ...");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray dataArray= new JSONArray(response);
                    progressDialog.dismiss();
                    for (int i =0; i<dataArray.length(); i++) {

                        JSONObject obj = dataArray.getJSONObject(i);
                        int extraId = Integer.parseInt(getIntent().getStringExtra(TAG_ID));

                        int id = obj.getInt("id");
                        if (extraId == id) {
                            String fotobase64 = obj.getString("img");
                            byte[] decodedString = Base64.decode(fotobase64, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            if (extraId== id ) {

                                if (fotobase64.isEmpty()) {

                                    Picasso.with(getApplication()).load("http://smknprigen.sch.id/bkk/image/default.png").into(fotoProfile);
                                } else if (fotobase64.equals("null")) {

                                    Picasso.with(getApplication()).load("http://smknprigen.sch.id/bkk/image/default.png").into(fotoProfile);
                                } else {

                                    fotoProfile.setImageBitmap(decodedByte);
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(Profil.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(Profil.this);
        rQueue.add(request);
    }

    private void pilihgambar() {
        final CharSequence[] options = { "Ambil Foto", "Pilih Dari Gallery","Batal" };
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Profil.this);
        builder.setTitle("Ganti Foto!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Ambil Foto"))
                {
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 100);

                    ganti_Foto.setEnabled(true);

                }
                else if (options[item].equals("Pilih Dari Gallery"))
                {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);

                    ganti_Foto.setEnabled(true);
                }
                else if (options[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void gantifoto()
    {
        progressDialog = new ProgressDialog(Profil.this);
        progressDialog.setMessage("Proses Simpan, Mohon Tunggu...");
        progressDialog.show();

        //converting image to base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, gantifoto, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataObj = new JSONObject(response);
                    progressDialog.dismiss();


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(Profil.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override

            protected Map<String,String> getParams() throws AuthFailureError {

                Map<String,String> map = new HashMap<>();

                map.put("id", getIntent().getStringExtra(TAG_ID));
                map.put("img",imageString);

                return map;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                //getting image from gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //Setting image to ImageView
                fotoProfile.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // Requesting runtime permission to access camera.
    public void EnableRuntimePermissionToAccessCamera(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(Profil.this,
                Manifest.permission.CAMERA))
        {

            // Printing toast message after enabling runtime permission.
            Toast.makeText(Profil.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(Profil.this,new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed()
    {

        idu = getIntent().getStringExtra(TAG_ID);
        idun = getIntent().getStringExtra(TAG_NAMA);
        idn = getIntent().getStringExtra(TAG_USERNAME);
        Intent kembali = new Intent(Profil.this, MainActivity.class);
        kembali.putExtra(TAG_ID, idu);
        kembali.putExtra(TAG_USERNAME, idn);
        kembali.putExtra(TAG_NAMA, idun);
        startActivity(kembali);
        finish();
    }
}




