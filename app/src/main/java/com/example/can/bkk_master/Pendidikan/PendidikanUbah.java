package com.example.can.bkk_master.Pendidikan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.can.bkk_master.R;
import com.example.can.bkk_master.Server.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PendidikanUbah extends AppCompatActivity {

    private EditText tambah_user,ubah_tingkat,ubah_instansi,ubah_masuk,ubah_lulus;
    private TextView up_id;
    private Button simpan;

    private String TAG_B = "tag_b";

    String url = Server.URL + "pendidikan_tampil.php";

    String url_update  = Server.URL + "pendidikan_ubah.php";

    final String TAG ="Edit";
    public final static String TAG_ID = "id";

    public final static String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendidikan_ubah);

        editData(url);

    }

    public void editData (String url)
    {

        up_id = (TextView) findViewById(R.id.up_idpendidikan);
        ubah_tingkat = (EditText) findViewById(R.id.ubah_tingkatpendidikan);
        ubah_instansi = (EditText) findViewById(R.id.ubah_instansipendidikan);
        ubah_masuk = (EditText) findViewById(R.id.ubah_masukpendidikan);
        ubah_lulus = (EditText) findViewById(R.id.ubah_luluspendidikan);

        simpan = (Button) findViewById(R.id.btn_ubahpendidikan);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan();
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequests =
                new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dataArray = new JSONArray(response);

                            for (int i = 0; i < dataArray.length(); i++) {


                                JSONObject obj = dataArray.getJSONObject(i);
                                int extraId = Integer.parseInt(getIntent().getStringExtra(TAG_ID));
                                int id = obj.getInt("id");
                                String id_u = obj.getString("id");
                                String tingkat = obj.getString("tingkat");
                                String instansi = obj.getString("instansi");
                                String masuk = obj.getString("masuk");
                                String lulus = obj.getString("lulus");

                                if (extraId == id) {
                                    up_id.setText(id_u);
                                    ubah_tingkat.setText(tingkat);
                                    ubah_instansi.setText(instansi);
                                    ubah_masuk.setText(masuk);
                                    ubah_lulus.setText(lulus);

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
                        onBackPressed();
                        onRestart();
                    }else if(code == 0)
                    {
                        recreate();
                    }

                    Toast.makeText(PendidikanUbah.this, dataObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Toast.makeText(PendidikanUbah.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override

            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();

                map.put("id", up_id.getText().toString());
                map.put("tingkat", ubah_tingkat.getText().toString());
                map.put("instansi", ubah_instansi.getText().toString());
                map.put("masuk",  ubah_masuk.getText().toString());
                map.put("lulus", ubah_lulus.getText().toString());

                return map;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(PendidikanUbah.this,Pendidikan.class);
        startActivity(intent);
        return;
    }

}