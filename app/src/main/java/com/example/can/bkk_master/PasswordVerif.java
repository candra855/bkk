package com.example.can.bkk_master;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderLayout;
import com.example.can.bkk_master.Controller.AppController;
import com.example.can.bkk_master.Pendidikan.PendidikanUbah;
import com.example.can.bkk_master.Server.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.can.bkk_master.Adapter.BerandaAdapter.TAG_IDL;
import static com.example.can.bkk_master.Login.my_shared_preferences;
import static com.example.can.bkk_master.Login.session_status;

public class PasswordVerif extends AppCompatActivity {


    TextView txt_id, txt_username,username_user,nama_user;
    EditText ktp;
    String id, username,email;
    String idu,idun,idn,idj,id_u;
    ImageView fotoProfile;
    Intent intent;
    SharedPreferences sharedpreferences;
    Boolean session = false;
    Button btn_verif;
    private TextView up_idp,email_u;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;


    String url = Server.URL + "users_tampil.php";
    String url_kirim = Server.URL + "password_verif.php";

    public final static String TAG = "Password";
    public static final String TAG_ID = "id";

    public static final String TAG_USERNAME = "username";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_EMAIL = "email";
    public final static String TAG_MESSAGE = "message";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        username = sharedpreferences.getString(TAG_USERNAME, null);
        email = sharedpreferences.getString(TAG_EMAIL, null);

        up_idp = (TextView) findViewById(R.id.up_idp);
        email_u = (TextView) findViewById(R.id.up_email);

        id = getIntent().getStringExtra(TAG_ID);
        username = getIntent().getStringExtra(TAG_USERNAME);
        email = getIntent().getStringExtra(TAG_EMAIL);

        ktp = (EditText) findViewById(R.id.password);
        btn_verif = (Button) findViewById(R.id.btn_password);
        btn_verif.setText("Kirim Verifikasi");
        btn_verif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirim();
                email_send();

            }
        });


        ambilData(url);
//        kirim();
    }

    public void ambilData (String url)
    {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequests = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray dataArray = new JSONArray(response);
                    for (int i = 0; i < dataArray.length(); i++) {


                        JSONObject obj = dataArray.getJSONObject(i);
                        int extraId = Integer.parseInt(getIntent().getStringExtra(TAG_ID));
                        int id = obj.getInt("id");
                        String id_u = obj.getString("id");
                        String email = obj.getString("email");


                        if (extraId == id) {
                            up_idp.setText(id_u);
                            email_u.setText(email);

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
                up_idp.setText(error.getLocalizedMessage());
            }
        });
        requestQueue.add(stringRequests);
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

                        Intent intent = new Intent(PasswordVerif.this,Profil.class);
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

                    Toast.makeText(PasswordVerif.this, dataObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

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
                Toast.makeText(PasswordVerif.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override

            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();

                map.put("id", up_idp.getText().toString());

                return map;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void email_send (){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email_u.getText().toString()});
        intent.putExtra(Intent.EXTRA_CC, new String[] {"smkn1_prigen@yahoo.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Code Verifikasi mu adalah");
        intent.putExtra(Intent.EXTRA_TEXT, "Hai, ini adalah percobaan mengirim email dari aplikasi android");

//        try {
//            startActivity(Intent.createChooser(intent, "Ingin Mengirim Email ?"));
//        } catch (android.content.ActivityNotFoundException ex) {
//            //do something else
//        }
    }

}
