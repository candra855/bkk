package com.example.can.bkk_master;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.can.bkk_master.Controller.AppController;
import com.example.can.bkk_master.Pendidikan.Pendidikan;
import com.example.can.bkk_master.Pendidikan.PendidikanTambah;
import com.example.can.bkk_master.Server.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.example.can.bkk_master.Login.my_shared_preferences;

public class FotoIndustri extends AppCompatActivity {

    Button pilih,simpan;
    ImageView foto;
    FloatingActionButton fab;

    String id,idu,idun,idn,idj;

    SharedPreferences sharedpreferences;
    ProgressDialog progressDialog;
    String url = Server.URL + "industri_tampil.php";

    public final static String TAG_ID = "id";
    public final static String TAG_IDII = "idi";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_JURUSAN = "id_jurusan";
    public final static String TAG = "Foto";
    public final static String TAG_MESSAGE = "message";

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_profil);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Foto Profil");
        setSupportActionBar(toolbar);

        //Set icon to toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sharedpreferences = this.getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

        foto = (ImageView) findViewById(R.id.foto_profil);

        ambilfoto();

        fab         = (FloatingActionButton) findViewById(R.id.simpan_gambar);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }


    private void ambilfoto()
    {
        progressDialog = new ProgressDialog(FotoIndustri.this);
        progressDialog.setMessage("Memuat ...");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray dataArray= new JSONArray(response);
                    progressDialog.dismiss();
                    for (int i =0; i<dataArray.length(); i++) {

                        JSONObject obj = dataArray.getJSONObject(i);
                        int extraId = Integer.parseInt(getIntent().getStringExtra(TAG_IDII));
                        int id = obj.getInt("id_industri");

                        if (extraId == id)
                        {
                            String imagePath = obj.getString("gambar");
                            Picasso.with(FotoIndustri.this)
                                    .load("http://muslikh.my.id/bkk/image/" + imagePath)
                                    .placeholder(R.drawable.ic_image_black)
                                    .error(R.drawable.ic_image_black)
                                    .fit()
                                    .into(foto);
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
                Toast.makeText(FotoIndustri.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(FotoIndustri.this);
        rQueue.add(request);
    }

}
