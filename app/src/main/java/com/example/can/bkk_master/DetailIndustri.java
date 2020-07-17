package com.example.can.bkk_master;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.can.bkk_master.Controller.AppController;
import com.example.can.bkk_master.Server.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.can.bkk_master.Login.my_shared_preferences;
import static com.example.can.bkk_master.Login.session_status;


public class DetailIndustri extends AppCompatActivity {

    ProgressDialog pDialog;

    final String url = Server.URL + "industri_tampil.php";

    TextView id_industri,nama,email,telepon,alamat,website;
    ImageView gambar;

    public final static String TAG = "Detail";
    public final static String TAG_ID = "id";
    public final static String TAG_IDI = "idi";
    public final static String TAG_MESSAGE = "message";

    String id,idu,idi;
    RequestQueue requestQueue;
    SharedPreferences sharedpreferences;
    Boolean session = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_industri);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Detail Industri");
        setSupportActionBar(toolbar);

        //Set icon to toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);


        id_industri = (TextView) findViewById(R.id.id_industri);
        nama = (TextView)  findViewById(R.id.nama_industri);
        email = (TextView)  findViewById(R.id.email_industri);
        telepon = (TextView)  findViewById(R.id.telepon_industri);
        alamat = (TextView)  findViewById(R.id.alamat_industri);
        website = (TextView)  findViewById(R.id.website_industri);
        gambar = (ImageView)  findViewById(R.id.gambar_industri);
        gambar = (ImageView) findViewById(R.id.gambar_industri);
        gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idu = getIntent().getStringExtra(TAG_ID);
                idi = getIntent().getStringExtra(TAG_IDI);
                sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
                session = sharedpreferences.getBoolean(session_status, false);
                Intent intent = new Intent(DetailIndustri.this, FotoIndustri.class);
                intent.putExtra(TAG_ID, idu);
                intent.putExtra(TAG_IDI, idi);

                startActivity(intent);
            }
        });

        ambilData();

    }

    public void ambilData()
    {

        pDialog = new ProgressDialog(DetailIndustri.this);
        pDialog.setMessage("Memuat ...");
        pDialog.show();

        RequestQueue  requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        StringRequest stringRequests =
                new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray dataArray= new JSONArray(response);

                            pDialog.dismiss();
                            for (int i =0; i<dataArray.length(); i++)
                            {

                                JSONObject obj = dataArray.getJSONObject(i);
                                int extraId = Integer.parseInt(getIntent().getStringExtra(TAG_IDI));
                                int id = obj.getInt("id_industri");
                                String id_u = obj.getString("id_industri");

                                if (extraId== id )
                                {
                                    id_industri.setText(id_u);
                                    nama.setText(obj.getString("nama"));
                                    email.setText(obj.getString("email"));
                                    telepon.setText(obj.getString("telepon"));
                                    alamat.setText(obj.getString("alamat"));
                                    website.setText(obj.getString("website"));
//
                                    String imagePath = obj.getString("gambar");
                                    Picasso.with(DetailIndustri.this)
                                            .load("http://muslikh.my.id/bkk/image/" + imagePath)
                                            .placeholder(R.drawable.ic_business_black)
                                            .error(R.drawable.ic_business_black)
                                            .fit()
                                            .into(gambar);
                                }
                            }
                            Log.d(TAG, "onResponse:" + response);
                        }  catch(
                                JSONException e)

                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        nama.setText(error.getLocalizedMessage());


                    }
                });
        requestQueue.add(stringRequests);
    }

//    @Override
//    public void onBackPressed()
//    {
//        Intent pindah = new Intent(DetailLowongan.this, MainActivity.class);
//        startActivity(pindah);
//
//    }
}





