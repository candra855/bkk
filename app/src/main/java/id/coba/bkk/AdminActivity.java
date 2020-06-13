package id.coba.bkk;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.coba.bkk.AdminMenu.MgAlumni;
import id.coba.bkk.AdminMenu.MgInformasi;
import id.coba.bkk.server.Server;

import static id.coba.bkk.Login.my_shared_preferences;
import static id.coba.bkk.Login.session_status;

public class AdminActivity extends AppCompatActivity {


    String url = Server.URL + "users_read.php";

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";

    String idu;
    public final static String TAG = "Profile";

    RequestQueue requestQueue;

    ImageView fotoProfile;
    TextView txt_id,adminname;
    Button logout, alumni, informasi;
    String id;

    SharedPreferences sharedpreferences;
    NavigationView navigationView;
    Boolean session = false;

    private long mBackPressed;
    private static final int TIME_INTERVAL = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        fotoProfile = (ImageView) findViewById(R.id.image_admin);

        Picasso.with(this).load("http://smknprigen.sch.id/bkk/image/default.png").into(fotoProfile);


        //  txt_id = (TextView) findViewById(R.id.txt_id);
        adminname = (TextView) findViewById(R.id.nameadmin);

//        id = getIntent().getStringExtra(TAG_ID);
//        username = getIntent().getStringExtra(TAG_USERNAME);
        // txt_id.setText("ID : " + id);
        // txt_username.setText("USERNAME : " + username);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);

        alumni = (Button) findViewById(R.id.btn_mg_alumni);
        alumni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, MgAlumni.class);
                startActivity(intent);
            }
        });

        informasi = (Button) findViewById(R.id.btn_mg_informasi);
        informasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, MgInformasi.class);
                startActivity(intent);
            }
        });

        logout = (Button) findViewById(R.id.btn_adm_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(session_status, false);
                editor.putString(TAG_ID, null);
                editor.putString(TAG_USERNAME, null);
                editor.commit();

                Intent intent = new Intent(AdminActivity.this, Login.class);
                finish();
                startActivity(intent);
            }
        });

    ambilData();
    }

    public void ambilData()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequests =
                new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dataArray= new JSONArray(response);

                            for (int i =0; i<dataArray.length(); i++)
                            {
                                JSONObject obj = dataArray.getJSONObject(i);
                                int extraId = Integer.parseInt(getIntent().getStringExtra(TAG_ID));
                                int id = obj.getInt("id");


                                if (extraId== id )
                                {

                                    adminname.setText(obj.getString("name"));
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
                        adminname.setText(error.getLocalizedMessage());
                    }
                });
        requestQueue.add(stringRequests);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
   public void onBackPressed()
    {

//        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
//        {
            moveTaskToBack(true);
//        }
//        else { Toast.makeText(getBaseContext(), "Tekan Sekali Lagi", Toast.LENGTH_SHORT).show(); }
//
//        mBackPressed = System.currentTimeMillis();
    }




}