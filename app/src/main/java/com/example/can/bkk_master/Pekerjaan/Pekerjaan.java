package com.example.can.bkk_master.Pekerjaan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.can.bkk_master.Adapter.PekerjaanAdapter;
import com.example.can.bkk_master.Pendidikan.Pendidikan;
import com.example.can.bkk_master.Profil;
import com.example.can.bkk_master.R;
import com.example.can.bkk_master.Server.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Pekerjaan extends AppCompatActivity {

    String id,idu,idn,idun,idj;
    public static View.OnClickListener myOnClickListener;
    FloatingActionButton fab;

    private RecyclerView lvpekerjaan;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    ProgressDialog progressDialog;

    String url_read = Server.URL + "pekerjaan_tampil.php";

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_JURUSAN = "id_jurusan";

    ArrayList<HashMap<String ,String>> list_data;
    PekerjaanAdapter pekerjaanAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pekerjaan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Riwayat Pekerjaan");
        setSupportActionBar(toolbar);

        //Set icon to toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idu = getIntent().getStringExtra(TAG_ID);
                idun = getIntent().getStringExtra(TAG_NAMA);
                idn = getIntent().getStringExtra(TAG_USERNAME);
                idj = getIntent().getStringExtra(TAG_JURUSAN);
                Intent kembali = new Intent(Pekerjaan.this, Profil.class);
                kembali.putExtra(TAG_ID, idu);
                kembali.putExtra(TAG_USERNAME, idn);
                kembali.putExtra(TAG_NAMA, idun);
                kembali.putExtra(TAG_JURUSAN, idj);
                startActivity(kembali);
                finish();
            }
        });

        final int extraId = Integer.parseInt(getIntent().getStringExtra(TAG_ID));
        idun = getIntent().getStringExtra(TAG_NAMA);
        idn = getIntent().getStringExtra(TAG_USERNAME);
        idj = getIntent().getStringExtra(TAG_JURUSAN);
        fab         = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Pekerjaan.this,PekerjaanTambah.class);
                intent.putExtra(TAG_ID, Integer.toString(extraId));
                intent.putExtra(TAG_USERNAME, idn);
                intent.putExtra(TAG_NAMA, idun);
                intent.putExtra(TAG_JURUSAN, idj);
                startActivity(intent);
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        lvpekerjaan = (RecyclerView) findViewById(R.id.lvpekerjaan);
        lvpekerjaan.setLayoutManager(llm);
        list_data = new ArrayList<HashMap<String, String>>();

        progressDialog = new ProgressDialog(Pekerjaan.this);
        progressDialog.setMessage("Memuat ...");
        progressDialog.show();

        requestQueue = Volley.newRequestQueue(Pekerjaan.this);
        stringRequest = new StringRequest(Request.Method.GET, url_read, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONArray dataArray= new JSONArray(response);
                    progressDialog.dismiss();
                    for (int i =0; i<dataArray.length(); i++)
                    {
                        JSONObject json = dataArray.getJSONObject(i);
                        int extraId = Integer.parseInt(getIntent().getStringExtra(TAG_ID));
                        int id = json.getInt("user_id");
                        if (extraId== id )
                        {
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id_pekerjaan", json.getString("id_pekerjaan"));
                            map.put("tempat", json.getString("tempat"));
                            map.put("masuk", json.getString("masuk"));
                            map.put("keluar", json.getString("keluar"));
                            list_data.add(map);
                            pekerjaanAdapter = new PekerjaanAdapter(Pekerjaan.this, list_data);
                            lvpekerjaan.setAdapter(pekerjaanAdapter);
                        }

                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(Pekerjaan.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);


    }

    @Override
    public void onBackPressed()
    {
        idu = getIntent().getStringExtra(TAG_ID);
        idun = getIntent().getStringExtra(TAG_NAMA);
        idn = getIntent().getStringExtra(TAG_USERNAME);
        idj = getIntent().getStringExtra(TAG_JURUSAN);
        Intent kembali = new Intent(Pekerjaan.this, Profil.class);
        kembali.putExtra(TAG_ID, idu);
        kembali.putExtra(TAG_USERNAME, idn);
        kembali.putExtra(TAG_NAMA, idun);
        kembali.putExtra(TAG_JURUSAN, idj);
        startActivity(kembali);
        finish();
    }

}
