package com.example.can.bkk_master;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.can.bkk_master.Login.my_shared_preferences;
import static com.example.can.bkk_master.Login.session_status;

public class PasswordBaru extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText ubah_password;
    private Button ubah;

    Boolean session = false;
    String idu,idun,idn,idj;
    String id, username,nama,level,jurusan;
    SharedPreferences sharedpreferences;
    private String TAG_B = "tag_b";
    String url_update  = Server.URL + "password_baru.php";
    final String TAG ="Edit";
    public final static String TAG_ID = "id";
    private static final String TAG_LEVEL = "role_id";
    public final static String TAG_USERNAME = "username";
    public final static String TAG_NAMA = "nama";
    public final static String TAG_JURUSAN = "id_jurusan";
    public final static String TAG_MESSAGE = "message";
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Password Baru");
        setSupportActionBar(toolbar);

        //Set icon to toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        username = sharedpreferences.getString(TAG_USERNAME, null);
        nama = sharedpreferences.getString(TAG_NAMA, null);
        jurusan = sharedpreferences.getString(TAG_JURUSAN, null);

        ubah_password = (EditText) findViewById(R.id.password);
        ubah_password.setHint("Password Baru");
        ubah = (Button) findViewById(R.id.btn_password);
        ubah.setText("Simpan");
        ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan();
            }
        });


    }

    private void simpan ()
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

                        Intent intent = new Intent(PasswordBaru.this,Profil.class);
                        intent.putExtra(TAG_ID, idu);
                        intent.putExtra(TAG_USERNAME, idn);
                        intent.putExtra(TAG_NAMA, idun);
                        intent.putExtra(TAG_JURUSAN, idj);
                        startActivity(intent);
                        finish();
                    }else if(code == 0)
                    {
                        recreate();
                    }

                    Toast.makeText(PasswordBaru.this, dataObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

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
                Toast.makeText(PasswordBaru.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override

            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();

                map.put("id", id);
                map.put("password", ubah_password.getText().toString());

                return map;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }


}
