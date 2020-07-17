package com.example.can.bkk_master;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import static com.example.can.bkk_master.Login.my_shared_preferences;
import static com.example.can.bkk_master.Login.session_status;

public class TentangValidasi extends AppCompatActivity {

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_JURUSAN = "id_jurusan";
    public static final String TAG_IMG = "img";
    private static final String TAG_LEVEL = "role_id";

    String id, username,levelU,level,nama,jurusan;
    SharedPreferences sharedpreferences;
    Boolean session = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Tentang Aplikasi");
        setSupportActionBar(toolbar);

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
        level = sharedpreferences.getString(TAG_LEVEL, null);
        jurusan = sharedpreferences.getString(TAG_JURUSAN, null);

        id = getIntent().getStringExtra(TAG_ID);
        username = getIntent().getStringExtra(TAG_USERNAME);
        nama = getIntent().getStringExtra(TAG_NAMA);
        jurusan = getIntent().getStringExtra(TAG_JURUSAN);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setInverseBackgroundForced(false);
        alert.setMessage("Maaf data belum kami validasi, silahkan hubungi Admin untuk melakukan proses validasi. " +
                "\n \n \nTerima Kasih  :)")
                .setCancelable(false)
                .setPositiveButton("Hubungi Admin", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent webIntent = new Intent(android.content.Intent.ACTION_VIEW);
                        webIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=6282234724594&text=Assalamu'alaikum...\n\nSaya pengguna baru, bisa minta bantuannya ?"));
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(session_status, false);
                        editor.putString(TAG_ID, null);
                        editor.putString(TAG_USERNAME, null);
                        editor.commit();
                        Intent tunggu = new Intent(TentangValidasi.this,Login.class);
                        finish();
                        startActivity(tunggu);
                        startActivity(webIntent);
                    }
                })
                .setNegativeButton("Tutup", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(session_status, false);
                        editor.putString(TAG_ID, null);
                        editor.putString(TAG_USERNAME, null);
                        editor.commit();
                        Intent tunggu = new Intent(TentangValidasi.this,Login.class);
                        finish();
                        startActivity(tunggu);
                    }
                });
        AlertDialog berhasil = alert.create();
        berhasil.show();

    }
}
