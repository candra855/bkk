package com.example.can.bkk_master;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.can.bkk_master.Controller.AppController;
import com.example.can.bkk_master.Server.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class PasswordLupa extends AppCompatActivity {

    ProgressDialog pDialog;
    Button btn_cek;
    EditText in_username, in_email;
    Intent intent;
    TextView isi_daftar,lupa_pass;
    String idu,idun,idn,idj,id_u;

    int success;
    ConnectivityManager conMgr;

    private String url = Server.URL + "password_lupa.php";
    String url_kirim = Server.URL + "password_verif.php";

    private static final String TAG = Login.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public final static String TAG_ID = "id";
    public static final String TAG_NAMA = "nama";
    public final static String TAG_USERNAME = "username";
    public final static String TAG_EMAIL= "email";
    private static final String TAG_LEVEL = "role_id";

    String tag_json_obj = "json_obj_req";

    SharedPreferences sharedpreferences;
    Boolean session = false;
    String id, username,email,level;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_lupa);
        getSupportActionBar();

        btn_cek = (Button) findViewById(R.id.btn_cek);
        in_username = (EditText) findViewById(R.id.username);
        in_email = (EditText) findViewById(R.id.email);

        // Cek session login jika TRUE maka langsung buka MainActivity
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        username = sharedpreferences.getString(TAG_USERNAME, null);
        email = sharedpreferences.getString(TAG_EMAIL, null);

        if (session) {
            Intent intent = new Intent(PasswordLupa.this, PasswordVerif.class);
            intent.putExtra(TAG_ID, id);
            intent.putExtra(TAG_USERNAME, username);
            intent.putExtra(TAG_EMAIL, email);
            finish();
            startActivity(intent);
        }


        btn_cek.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String no_ktp = in_username.getText().toString();
                String email = in_email.getText().toString();

                // mengecek kolom yang kosong

                        checkLogin(no_ktp, email);

            }
        });

    }

    private void checkLogin(final String username, final String email) {

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {
                        String username = jObj.getString(TAG_USERNAME);
                        String id = jObj.getString(TAG_ID);
                        String email = jObj.getString(TAG_EMAIL);

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        // menyimpan login ke session
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(session_status, true);
                        editor.putString(TAG_ID, id);
                        editor.putString(TAG_USERNAME, username);
                        editor.putString(TAG_EMAIL, email);
                        editor.putString(TAG_LEVEL, level);
                        editor.commit();

                        // Memanggil main activity
                        Intent intent = new Intent(PasswordLupa.this, PasswordVerif.class);
                        intent.putExtra(TAG_ID, id);
                        intent.putExtra(TAG_USERNAME, username);
                        intent.putExtra(TAG_EMAIL, email);
                        intent.putExtra(TAG_LEVEL, level);
                        kirim();
                        finish();
                        startActivity(intent);

                    } else {
                      Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
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
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("email", email);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }


    private void kirim ()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_kirim, new Response.Listener<String>() {
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
                        idj = getIntent().getStringExtra(TAG_EMAIL);

                        Intent intent = new Intent(PasswordLupa.this,PasswordVerif.class);
                        intent.putExtra(TAG_ID, idu);
                        intent.putExtra(TAG_USERNAME, idn);
                        intent.putExtra(TAG_NAMA, idun);
                        intent.putExtra(TAG_EMAIL, idj);
                        startActivity(intent);
                        finish();
                    }else if(code == 0)
                    {
                        recreate();
                    }

                    Toast.makeText(PasswordLupa.this, dataObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

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
                Toast.makeText(PasswordLupa.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override

            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();

                map.put("id", id);

                return map;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }


}
