package com.example.can.bkk_master.Pekerjaan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.can.bkk_master.R;
import com.example.can.bkk_master.Server.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PekerjaanTambah extends AppCompatActivity {

    private EditText tambah_user,tambah_tempat,tambah_masuk,tambah_keluar;
    private Button simpan;
    String id,idu,idn,idun;
    private String TAG = "tag";
    private String TAG_ID="id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_NAMA = "nama";
    private static String url = Server.URL + "pekerjaan_tambah.php";
    private String TAG_SUCCESS = "success";
    public final static String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pekerjaan_tambah);

        final RequestQueue request = Volley.newRequestQueue(getApplicationContext());
//        tambah_user = (EditText) findViewById(R.id.add_industry_id);
        tambah_tempat = (EditText) findViewById(R.id.tambah_tempatpekerjaan);
        tambah_masuk = (EditText) findViewById(R.id.tambah_masukpekerjaan);
        tambah_keluar = (EditText) findViewById(R.id.tambah_keluarpekerjaan);

        simpan = (Button) findViewById(R.id.btn_tambahpekerjaan);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan();
            }
        });

    }

    private void simpan()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataObj = new JSONObject(response);


                    int code = Integer.parseInt(dataObj.getString("code"));
                    if (code == 1)
                    {
                        idu = getIntent().getStringExtra(TAG_ID);
                        idun = getIntent().getStringExtra(TAG_NAMA);
                        idn = getIntent().getStringExtra(TAG_USERNAME);
                        Intent intent = new Intent(PekerjaanTambah.this,Pekerjaan.class);
                        intent.putExtra(TAG_ID, idu);
                        intent.putExtra(TAG_USERNAME, idn);
                        intent.putExtra(TAG_NAMA, idun);
                        startActivity(intent);
                    }else if(code == 0)
                    {
                        recreate();
                    }

                    Toast.makeText(PekerjaanTambah.this, dataObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Toast.makeText(PekerjaanTambah.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override

            protected Map<String,String> getParams() throws AuthFailureError {

                id = getIntent().getStringExtra(TAG_ID);

                Map<String,String> map = new HashMap<String, String>();
                map.put("user_id", id);
                map.put("tempat", tambah_tempat.getText().toString());
                map.put("masuk", tambah_masuk.getText().toString());
                map.put("keluar", tambah_keluar.getText().toString());

                return map;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

}

