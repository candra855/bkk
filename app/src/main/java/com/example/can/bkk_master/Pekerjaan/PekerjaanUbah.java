package com.example.can.bkk_master.Pekerjaan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.can.bkk_master.MainActivity;
import com.example.can.bkk_master.Pendidikan.Pendidikan;
import com.example.can.bkk_master.Pendidikan.PendidikanUbah;
import com.example.can.bkk_master.Profil;
import com.example.can.bkk_master.R;
import com.example.can.bkk_master.Server.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PekerjaanUbah extends AppCompatActivity {

    private EditText tambah_user,ubah_tempat,ubah_masuk,ubah_keluar;
    private TextView up_id;
    private Button simpan;
    String id,idu,idn,idun,idj;

    ProgressDialog progressDialog;

    private String TAG_B = "tag_b";

    String url = Server.URL + "pekerjaan_tampil.php";
    String url_update  = Server.URL + "pekerjaan_ubah.php";

    public static final String TAG_IDP = "idp";
    public final static String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_JURUSAN = "id_jurusan";

    final String TAG ="Edit";

    public final static String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pekerjaan_ubah);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Perbarui");
        setSupportActionBar(toolbar);

        //Set icon to toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editData(url);

    }

    public void editData (String url)
    {

        up_id = (TextView) findViewById(R.id.up_idpekerjaan);
        ubah_tempat = (EditText) findViewById(R.id.ubah_tempatpekerjaan);
        ubah_masuk = (EditText) findViewById(R.id.ubah_masukpekerjaan);
        ubah_keluar = (EditText) findViewById(R.id.ubah_keluarpekerjaan);

        simpan = (Button) findViewById(R.id.btn_ubahpekerjaan);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan();
            }
        });

        progressDialog = new ProgressDialog(PekerjaanUbah.this);
        progressDialog.setMessage("Memuat ...");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequests = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray dataArray = new JSONArray(response);
                    progressDialog.dismiss();
                    for (int i = 0; i < dataArray.length(); i++) {


                        JSONObject obj = dataArray.getJSONObject(i);
                        int extraId = Integer.parseInt(getIntent().getStringExtra(TAG_IDP));
                        int id = obj.getInt("id_pekerjaan");
                        String id_u = obj.getString("id_pekerjaan");
                        String tempat = obj.getString("tempat");
                        String masuk = obj.getString("masuk");
                        String keluar = obj.getString("keluar");

                        if (extraId == id) {
                            up_id.setText(id_u);
                            ubah_tempat.setText(tempat);
                            ubah_masuk.setText(masuk);
                            ubah_keluar.setText(keluar);

                            {

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
                up_id.setText(error.getLocalizedMessage());
            }
        });
        requestQueue.add(stringRequests);
    }

    private void simpan()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
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
                        idj = getIntent().getStringExtra(TAG_JURUSAN);
                        Intent intent = new Intent(PekerjaanUbah.this,Pekerjaan.class);
                        intent.putExtra(TAG_ID, idu);
                        intent.putExtra(TAG_USERNAME, idn);
                        intent.putExtra(TAG_NAMA, idun);
                        intent.putExtra(TAG_JURUSAN, idj);
                        startActivity(intent);
                    }else if(code == 0)
                    {
                        recreate();
                    }

                    Toast.makeText(PekerjaanUbah.this, dataObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Toast.makeText(PekerjaanUbah.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override

            protected Map<String, String> getParams() throws AuthFailureError {
                id = getIntent().getStringExtra(TAG_ID);
                Map<String, String> map = new HashMap<>();
                map.put("id_pekerjaan", up_id.getText().toString());
                map.put("tempat", ubah_tempat.getText().toString());
                map.put("masuk",  ubah_masuk.getText().toString());
                map.put("keluar", ubah_keluar.getText().toString());

                return map;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

//    @Override
//    public void onBackPressed()
//    {
//        idu = getIntent().getStringExtra(TAG_ID);
//        Intent kembali = new Intent(PekerjaanUbah.this, Pekerjaan.class);
//        kembali.putExtra(TAG_ID, idu);
//        startActivity(kembali);
//
//    }

}