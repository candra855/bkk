package com.example.can.bkk_master.Pendidikan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.can.bkk_master.Adapter.PendidikanAdapter;
import com.example.can.bkk_master.Profil;
import com.example.can.bkk_master.R;
import com.example.can.bkk_master.Server.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.can.bkk_master.Login.my_shared_preferences;
import static com.example.can.bkk_master.Login.session_status;

public class Pendidikan extends AppCompatActivity {

    String id,idu,idn,idun;
    private RecyclerView lvpendidikan;
    FloatingActionButton fab;

    SharedPreferences sharedpreferences;
    Boolean session = false;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    String url_tampil = Server.URL + "pendidikan_tampil.php";

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_NAMA = "nama";

    ArrayList<HashMap<String ,String>> list_data;
    PendidikanAdapter pendidikanAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendidikan);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);

        idun = getIntent().getStringExtra(TAG_NAMA);
        idn = getIntent().getStringExtra(TAG_USERNAME);
        final int extraId = Integer.parseInt(getIntent().getStringExtra(TAG_ID));
        fab         = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Pendidikan.this,PendidikanTambah.class);
                intent.putExtra(TAG_ID, Integer.toString(extraId));
                intent.putExtra(TAG_USERNAME, idn);
                intent.putExtra(TAG_NAMA, idun);
                startActivity(intent);
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        lvpendidikan = (RecyclerView) findViewById(R.id.lvpendidikan);
        lvpendidikan.setLayoutManager(llm);
        list_data = new ArrayList<HashMap<String, String>>();


        requestQueue = Volley.newRequestQueue(Pendidikan.this);
        stringRequest = new StringRequest(Request.Method.GET, url_tampil, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONArray dataArray= new JSONArray(response);

                    for (int i =0; i<dataArray.length(); i++)
                    {
                        JSONObject json = dataArray.getJSONObject(i);
                        int extraId = Integer.parseInt(getIntent().getStringExtra(TAG_ID));
                        int id = json.getInt("user_id");
                        if (extraId == id  )
                        {
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id_pendidikan", json.getString("id_pendidikan"));
                            map.put("user_id", json.getString("user_id"));
                            map.put("tingkat", json.getString("tingkat"));
                            map.put("instansi", json.getString("instansi"));
                            map.put("masuk", json.getString("masuk"));
                            map.put("lulus", json.getString("lulus"));
                            list_data.add(map);
                            pendidikanAdapter = new PendidikanAdapter(Pendidikan.this, list_data);
                            lvpendidikan.setAdapter(pendidikanAdapter);
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
                Toast.makeText(Pendidikan.this,error.getMessage(),Toast.LENGTH_LONG).show();
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
        Intent kembali = new Intent(Pendidikan.this, Profil.class);
        kembali.putExtra(TAG_ID, idu);
        kembali.putExtra(TAG_USERNAME, idn);
        kembali.putExtra(TAG_NAMA, idun);
        startActivity(kembali);

    }

}
