package com.example.can.bkk_master;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import static com.example.can.bkk_master.Adapter.BerandaAdapter.TAG_IDL;

public class DetailIndustri extends AppCompatActivity {

    ProgressDialog pDialog;

    final String url = Server.URL + "industri_tampil.php";

    TextView id_industri,nama,email,telepon,alamat,website;
    ImageView gambar;

    public final static String TAG = "Detail";
    public final static String TAG_ID = "id";
    public final static String TAG_IDI = "idi";
    public final static String TAG_MESSAGE = "message";

    String id;
    RequestQueue requestQueue;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_industri);

        id_industri = (TextView) findViewById(R.id.id_industri);
        nama = (TextView)  findViewById(R.id.nama_industri);
        email = (TextView)  findViewById(R.id.email_industri);
        telepon = (TextView)  findViewById(R.id.telepon_industri);
        alamat = (TextView)  findViewById(R.id.alamat_industri);
        website = (TextView)  findViewById(R.id.website_industri);
        gambar = (ImageView)  findViewById(R.id.gambar_industri);

        ambilData();
    }

    public void ambilData()
    {

        pDialog = new ProgressDialog(DetailIndustri.this);
        pDialog.setMessage("Proses ...");
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
                                    String fotobase64 = obj.getString("gambar");
                                    byte[] decodedString = Base64.decode(fotobase64, Base64.DEFAULT);
                                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                    if (fotobase64.isEmpty()) {

                                        Picasso.with(DetailIndustri.this).load("http://smknprigen.sch.id/bkk/image/default.png").into(gambar);
                                    } else if (fotobase64.equals("null")) {

                                        Picasso.with(DetailIndustri.this).load("http://smknprigen.sch.id/bkk/image/default.png").into(gambar);
                                    } else {

                                        gambar.setImageBitmap(decodedByte);
                                    }

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





