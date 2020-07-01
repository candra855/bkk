package com.example.can.bkk_master;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.can.bkk_master.Pendidikan.Pendidikan;
import com.example.can.bkk_master.Pendidikan.PendidikanTambah;
import com.example.can.bkk_master.Server.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailLowongan extends AppCompatActivity {

    final String url = Server.URL + "lowongan_tampil.php";
    private static String kirim = Server.URL + "lamaran_tambah.php";

    TextView id_lowongan,judul_tampil,deskripsi_tampil,jurusan_tampil,tutup_tampil,kualifikasi_tampil,lain_tampil,created_at_tampil,nama_tampil,alamat_tampil;
    private Button simpan;

    public final static String TAG = "Detail";
    public final static String TAG_ID = "id";
    public final static String TAG_MESSAGE = "message";

    String idindustri,id;
    RequestQueue requestQueue;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_lowongan);

        id_lowongan = (TextView) findViewById(R.id.id_lowongan);
        judul_tampil = (TextView)  findViewById(R.id.judul_tampil);
        deskripsi_tampil = (TextView)  findViewById(R.id.deskripsi_tampil);
//        jurusan_tampil = (TextView)  findViewById(R.id.ju);
        tutup_tampil = (TextView)  findViewById(R.id.tutup_tampil);
        kualifikasi_tampil = (TextView)  findViewById(R.id.kualifikasi_tampil);
        lain_tampil = (TextView)  findViewById(R.id.lain_tampil);
//        created_at_tampil = (TextView)  findViewById(R.id.created_at_tampil);

        nama_tampil = (TextView)  findViewById(R.id.nama_industri_tampil);
        alamat_tampil = (TextView)  findViewById(R.id.alamat_industri_tampil);


        ambilData();
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
        RequestQueue  requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        StringRequest stringRequests =
                new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dataArray= new JSONArray(response);

                            for (int i =0; i<dataArray.length(); i++)
                            {

                                JSONObject obj = dataArray.getJSONObject(i);
                                int extraId = Integer.parseInt(getIntent().getStringExtra(TAG_ID));
                                int id = obj.getInt("id_lowongan");
                                String id_u = obj.getString("id_lowongan");

                                if (extraId== id )
                                {
                                    id_lowongan.setText(id_u);
//                                    id_industri_tampil.setText(obj.getString("id_industri"));
                                    judul_tampil.setText(obj.getString("judul"));
                                    deskripsi_tampil.setText(obj.getString("deskripsi"));
//                                    jurusan_tampil.setText(obj.getString("jurusan"));
                                    tutup_tampil.setText(obj.getString("tutup"));
                                    kualifikasi_tampil.setText(obj.getString("kualifikasi"));
                                    lain_tampil.setText(obj.getString("lain"));
//                                    created_at_tampil.setText(obj.getString("created_at"));

                                    nama_tampil.setText(obj.getString("nama"));
                                    alamat_tampil.setText(obj.getString("alamat"));

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
//                        final int extraId = Integer.parseInt(getIntent().getStringExtra(TAG_ID));
                        Intent intent = new Intent(DetailLowongan.this,MainActivity.class);
//                        intent.putExtra(TAG_ID, Integer.toString(extraId));
                        startActivity(intent);
                    }else if(code == 0)
                    {
                        recreate();
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

}





