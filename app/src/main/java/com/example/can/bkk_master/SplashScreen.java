package com.example.can.bkk_master;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {

    private int waktu_loading=3000;
//    private int waktu_loading=4000;
    int success;
    ConnectivityManager conMgr;
    SharedPreferences sharedpreferences;
    Boolean session = false;
    String id, username,nama,level,jurusan;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_LEVEL = "role_id";
    public final static String TAG_USERNAME = "username";
    public final static String TAG_NAMA = "nama";
    public final static String TAG_JURUSAN = "id_jurusan";
    public final static String TAG_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }



        // Cek session login jika TRUE maka langsung buka MainActivity
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        username = sharedpreferences.getString(TAG_USERNAME, null);
        nama = sharedpreferences.getString(TAG_NAMA, null);
        jurusan = sharedpreferences.getString(TAG_JURUSAN, null);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    //setelah loading maka akan langsung berpindah ke home activity
                    Intent intent = new Intent(SplashScreen.this, Login.class);
                    intent.putExtra(TAG_ID, id);
                    intent.putExtra(TAG_USERNAME, username);
                    intent.putExtra(TAG_NAMA, nama);
                    intent.putExtra(TAG_JURUSAN, jurusan);
                    intent.putExtra(TAG_LEVEL, level);
                    startActivity(intent);
//                    finish();

                }
            },waktu_loading);
        }

    }


