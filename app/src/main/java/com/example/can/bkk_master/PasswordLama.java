package com.example.can.bkk_master;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.can.bkk_master.Controller.AppController;
import com.example.can.bkk_master.Server.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PasswordLama extends AppCompatActivity {

    ProgressDialog pDialog;
    Button btn_lanjut;
    EditText in_username, in_password;
    String idu,idun,idn,idj;
    Intent intent;

    int success;

    private String url = Server.URL + "password_lama.php";

    private static final String TAG = Login.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_LEVEL = "role_id";
    public final static String TAG_USERNAME = "username";
    public final static String TAG_NAMA = "nama";
    public final static String TAG_JURUSAN = "id_jurusan";
    public final static String TAG_ID = "id";

    String tag_json_obj = "json_obj_req";

    SharedPreferences sharedpreferences;
    Boolean session = false;
    String id, username,nama,level,jurusan;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        getSupportActionBar();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Password Lama");
        setSupportActionBar(toolbar);

        //Set icon to toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn_lanjut = (Button) findViewById(R.id.btn_password);
        btn_lanjut.setText("Lanjutkan");
        in_password = (EditText) findViewById(R.id.password);
        in_password.setHint("Password Lama");

        // Cek session login jika TRUE maka langsung buka MainActivity
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        username = sharedpreferences.getString(TAG_USERNAME, null);
        nama = sharedpreferences.getString(TAG_NAMA, null);
        jurusan = sharedpreferences.getString(TAG_JURUSAN, null);


        btn_lanjut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String password = in_password.getText().toString();
                  checkPass(password);
                    }
        });

    }

    private void checkPass(final String password) {

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);
                    if (success == 1) {

                        idu = getIntent().getStringExtra(TAG_ID);
                        idun = getIntent().getStringExtra(TAG_NAMA);
                        idn = getIntent().getStringExtra(TAG_USERNAME);
                        idj = getIntent().getStringExtra(TAG_JURUSAN);

                        Intent intent = new Intent(PasswordLama.this,PasswordBaru.class);
                        intent.putExtra(TAG_ID, idu);
                        intent.putExtra(TAG_USERNAME, idn);
                        intent.putExtra(TAG_NAMA, idun);
                        intent.putExtra(TAG_JURUSAN, idj);
                        startActivity(intent);
                        finish();

                    }else if(success == 0)
                    {
                        recreate();
                    }

                    Toast.makeText(PasswordLama.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    // adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("password", password);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }



//    @Override
//    public void onBackPressed()
//    {
//
//        moveTaskToBack(true);
//    }


}
