package com.example.can.bkk_master;

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
import com.example.can.bkk_master.Pendidikan.Pendidikan;
import com.example.can.bkk_master.Pendidikan.PendidikanUbah;
import com.example.can.bkk_master.Server.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UbahPassword extends AppCompatActivity {

    private EditText tambah_user,password_lama,password_baru,konfirmasi_password;
    private TextView up_id;
    private Button simpan;
    String id,idu,idn,idun,idj;

    private String TAG_B = "tag_b";

    String url_update  = Server.URL + "password_ubah.php";

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
        setContentView(R.layout.activity_ubah_password);

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

        up_id = (TextView) findViewById(R.id.up_idp);
        password_lama = (EditText) findViewById(R.id.ubah_pass_lama);
        password_baru = (EditText) findViewById(R.id.ubah_pass_baru);
        konfirmasi_password = (EditText) findViewById(R.id.ubah_konf_pass);


        simpan = (Button) findViewById(R.id.btn_ubahpassword);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan();
            }
        });
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
                        Intent intent = new Intent(UbahPassword.this,Pendidikan.class);
                        intent.putExtra(TAG_ID, idu);
                        intent.putExtra(TAG_USERNAME, idn);
                        intent.putExtra(TAG_NAMA, idun);
                        intent.putExtra(TAG_JURUSAN, idj);
                        startActivity(intent);
                    }else if(code == 0)
                    {
                        recreate();
                    }

                    Toast.makeText(UbahPassword.this, dataObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Toast.makeText(UbahPassword.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override

            protected Map<String, String> getParams() throws AuthFailureError {
                id = getIntent().getStringExtra(TAG_ID);
                Map<String, String> map = new HashMap<>();
                map.put("id", up_id.getText().toString());
                map.put("password_lama", password_lama.getText().toString());
                map.put("password_baru", password_baru.getText().toString());
                map.put("konfirmasi_password",  konfirmasi_password.getText().toString());

                return map;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

//    @Override
//    public void onBackPressed()
//    {
//        idu = getIntent().getStringExtra(TAG_ID);
//        idun = getIntent().getStringExtra(TAG_NAMA);
//        idn = getIntent().getStringExtra(TAG_USERNAME);
//        Intent kembali = new Intent(PendidikanUbah.this, Pendidikan.class);
//        kembali.putExtra(TAG_ID, idu);
//        kembali.putExtra(TAG_USERNAME, idn);
//        kembali.putExtra(TAG_NAMA, idun);
//        startActivity(kembali);
//
//    }

}
