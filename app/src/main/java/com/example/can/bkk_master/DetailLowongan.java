package com.example.can.bkk_master;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import static com.example.can.bkk_master.Login.my_shared_preferences;
import static com.example.can.bkk_master.Login.session_status;

public class DetailLowongan extends AppCompatActivity {

    ProgressDialog pDialog;

    final String url = Server.URL + "lowongan_tampil.php";
    final String cek = Server.URL + "cek_tombol_lamaran.php";
    private static String kirim = Server.URL + "lamaran_tambah.php";

    TextView id_lowongan,judul_tampil,id_industri,deskripsi_tampil,jurusan_tampil,tutup_tampil,kualifikasi_tampil,lain_tampil,created_at_tampil,nama_tampil,alamat_tampil;
    ImageView img_detail_lowongan;
    FrameLayout frame_button,frame_bawah;
    private Button simpan,lihat_industri;

    public final static String TAG = "Detail";
    public final static String TAG_ID = "id";
    public final static String TAG_IDI = "idi";
    public final static String TAG_MESSAGE = "message";

    String id,idu,idi;
    Boolean session = false;
    RequestQueue requestQueue;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_lowongan);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);

        id_lowongan = (TextView) findViewById(R.id.id_lowongan);
        id_industri = (TextView) findViewById(R.id.id_industri2);
        judul_tampil = (TextView)  findViewById(R.id.judul_tampil);
        deskripsi_tampil = (TextView)  findViewById(R.id.deskripsi_tampil);
//        jurusan_tampil = (TextView)  findViewById(R.id.ju);
        tutup_tampil = (TextView)  findViewById(R.id.tutup_tampil);
        kualifikasi_tampil = (TextView)  findViewById(R.id.kualifikasi_tampil);
        lain_tampil = (TextView)  findViewById(R.id.lain_tampil);
//        created_at_tampil = (TextView)  findViewById(R.id.created_at_tampil);

        nama_tampil = (TextView)  findViewById(R.id.nama_industri_tampil);
        alamat_tampil = (TextView)  findViewById(R.id.alamat_industri_tampil);
        img_detail_lowongan = (ImageView)  findViewById(R.id.img_detail_lowongan);
        frame_button = (FrameLayout)  findViewById(R.id.frame_button);
        frame_bawah = (FrameLayout)  findViewById(R.id.frame_bawah);

        ambilData();

        lihat_industri = (Button) findViewById(R.id.btn_lihat_industri);
        lihat_industri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idu = getIntent().getStringExtra(TAG_ID);
                idi = getIntent().getStringExtra(TAG_IDI);
                Intent intent = new Intent(DetailLowongan.this, DetailIndustri.class);
                intent.putExtra(TAG_ID, idu);
                intent.putExtra(TAG_IDI, idi);
                startActivity(intent);
            }
        });


        simpan = (Button) findViewById(R.id.btn_kirim);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan();
            }
        });

    }

    public void ambilData()
    {

        pDialog = new ProgressDialog(DetailLowongan.this);
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
                                int extraId = Integer.parseInt(getIntent().getStringExtra(TAG_IDL));
                                int id = obj.getInt("id_lowongan");
                                String id_u = obj.getString("id_lowongan");

                                if (extraId== id )
                                {
                                    id_lowongan.setText(id_u);
                                    judul_tampil.setText(obj.getString("judul"));
                                    deskripsi_tampil.setText(obj.getString("deskripsi"));
                                    id_industri.setText(obj.getString("id_industri"));
//                                    jurusan_tampil.setText(obj.getString("jurusan"));
                                    tutup_tampil.setText(obj.getString("tutup"));
                                    kualifikasi_tampil.setText(obj.getString("kualifikasi"));
                                    lain_tampil.setText(obj.getString("lain"));
//                                    created_at_tampil.setText(obj.getString("created_at"));
                                    nama_tampil.setText(obj.getString("nama"));
                                    alamat_tampil.setText(obj.getString("alamat"));

                                    String fotobase64 = obj.getString("gambar");
                                    byte[] decodedString = Base64.decode(fotobase64, Base64.DEFAULT);
                                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                    if (fotobase64.isEmpty()) {

                                        Picasso.with(DetailLowongan.this).load("http://smknprigen.sch.id/bkk/image/default.png").into(img_detail_lowongan);
                                    } else if (fotobase64.equals("null")) {

                                        Picasso.with(DetailLowongan.this).load("http://smknprigen.sch.id/bkk/image/default.png").into(img_detail_lowongan);
                                    } else {

                                        img_detail_lowongan.setImageBitmap(decodedByte);
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
                        judul_tampil.setText(error.getLocalizedMessage());


                    }
                });
        requestQueue.add(stringRequests);
    }

    private void simpan()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, kirim, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataObj = new JSONObject(response);


                    int code = Integer.parseInt(dataObj.getString("code"));
                    if (code == 1)
                    {
                         recreate();
                    }else if(code == 0)
                    {
//                        recreate();
                        simpan.setVisibility(View.GONE);
                        frame_button.setVisibility(View.GONE);
                        frame_bawah.setVisibility(View.GONE);
                    }

                    Toast.makeText(DetailLowongan.this, dataObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    // adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(DetailLowongan.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override

            protected Map<String,String> getParams() throws AuthFailureError {
                id = getIntent().getStringExtra(TAG_ID);

                Map<String,String> map = new HashMap<String, String>();
                map.put("id_user", id);
                map.put("id_lowongan", id_lowongan.getText().toString());
                map.put("status", String.valueOf("Menunggu"));

                return map;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    @Override
    public void onResume() {
        super.onResume();

    }

//    @Override
//    public void onBackPressed()
//    {
//        Intent pindah = new Intent(DetailLowongan.this, MainActivity.class);
//        startActivity(pindah);
//
//    }
}





