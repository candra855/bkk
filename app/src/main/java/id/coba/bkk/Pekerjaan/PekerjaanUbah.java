package id.coba.bkk.Pekerjaan;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.coba.bkk.Controller.AppController;
import id.coba.bkk.R;
import id.coba.bkk.UserProfil.DataPribadi;
import id.coba.bkk.server.Server;

public class PekerjaanUbah extends AppCompatActivity {

    private EditText tambah_user,ubah_tempat,ubah_masuk,ubah_keluar;
    private TextView up_id;
    private Button simpan;

    private String TAG_B = "tag_b";

    String url = Server.URL + "pekerjaan_tampil.php";

    String url_update  = Server.URL + "pekerjaan_ubah.php";

    final String TAG ="Edit";
    public final static String TAG_ID = "id";
    public final static String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pekerjaan_ubah);

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
                        ubah_tempat.setText(error.getLocalizedMessage());
                    }
                });
        requestQueue.add(stringRequests);
    }

    private void simpan ()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataObj = new JSONObject(response);

                    String code = dataObj.getString(TAG_MESSAGE);
                    if (code.equals("sukses"))
                    {
                        onBackPressed();
                        onRestart();

                    }else if (code.equals("gagal"))
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

                Map<String, String> map = new HashMap<>();

                map.put("id", up_id.getText().toString());
                map.put("tempat", ubah_tempat.getText().toString());
                map.put("masuk", ubah_masuk.getText().toString());
                map.put("keluar", ubah_keluar.getText().toString());

                return map;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(PekerjaanUbah.this,Pekerjaan.class);
        startActivity(intent);
        return;
    }

}