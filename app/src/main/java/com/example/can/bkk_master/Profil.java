package com.example.can.bkk_master;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.can.bkk_master.Adapter.ProfilAdapter;
import com.example.can.bkk_master.Pekerjaan.Pekerjaan;
import com.example.can.bkk_master.Pendidikan.Pendidikan;
import com.example.can.bkk_master.Server.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.can.bkk_master.Login.my_shared_preferences;
import static com.example.can.bkk_master.Login.session_status;

public class Profil extends AppCompatActivity {

    Boolean session = false;

    String url = Server.URL + "users_foto_tampil.php";

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_JURUSAN = "id_jurusan";

    String id,idu,idun,idn,idj;
    SharedPreferences sharedpreferences;
    public final static String TAG = "Profile";
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    ImageView fotoProfile;

    ListView listView;
    private String[] menu_profil = {
            "Curriculum Vitae",
            "Data Pribadi",
            "Riwayat Pendidikan",
            "Riwayat Pekerjaan",
            "Ganti Password",
    };

    private String[] menu_profil2 = {
            "Lihat Curriculum Vitae, cetak Curriculum Vitae",
            "No. KTP, nama lengkap, tanggal lahir, jenis kelamin, email, nomor telepon",
            "Lihat riwayat pendidikan, tambah riwayat pendidikan, ubah riwayat pendidikan",
            "Lihat riwayat pekerjaan, tambah riwayat pekerjaan, ubah riwayat pekerjaan",
            "Password lama, password baru",
    };

    private Integer[] logo_menu_profil = {
            R.drawable.ic_assignment_ind_black,
            R.drawable.ic_account_circle_black,
            R.drawable.ic_school_black,
            R.drawable.ic_work_black,
            R.drawable.ic_key_black,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Profil");
        setSupportActionBar(toolbar);

        //Set icon to toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idu = getIntent().getStringExtra(TAG_ID);
                idun = getIntent().getStringExtra(TAG_NAMA);
                idn = getIntent().getStringExtra(TAG_USERNAME);
                idj = getIntent().getStringExtra(TAG_JURUSAN);
                Intent kembali = new Intent(Profil.this, MainActivity.class);
                kembali.putExtra(TAG_ID, idu);
                kembali.putExtra(TAG_USERNAME, idn);
                kembali.putExtra(TAG_NAMA, idun);
                kembali.putExtra(TAG_JURUSAN, idj);
                startActivity(kembali);
                finish();
            }
        });

//        EnableRuntimePermissionToAccessCamera();
        sharedpreferences = this.getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

        listView = (ListView) findViewById(R.id.list_menu_profil);

        ProfilAdapter ProfilMenu = new ProfilAdapter(Profil.this, menu_profil,menu_profil2, logo_menu_profil);
        listView.setAdapter(ProfilMenu);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idu = getIntent().getStringExtra(TAG_ID);
                idn = getIntent().getStringExtra(TAG_USERNAME);
                idun = getIntent().getStringExtra(TAG_NAMA);
                idj = getIntent().getStringExtra(TAG_JURUSAN);
                sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
                session = sharedpreferences.getBoolean(session_status, false);

                if (session) {
                    if (position == 0) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Uri.parse("http://muslikh.my.id/bkk/cv_cetak.php?id=")+idu));
                        startActivity(intent);
                    } else if (position == 1) {
                        Intent intent = new Intent(Profil.this, DataPribadi.class);
                        intent.putExtra(TAG_ID, idu);
                        intent.putExtra(TAG_USERNAME, idn);
                        intent.putExtra(TAG_NAMA, idun);
                        intent.putExtra(TAG_JURUSAN, idj);
                        startActivity(intent);
                    } else if (position == 2) {
                        Intent intent = new Intent(Profil.this, Pendidikan.class);
                        intent.putExtra(TAG_ID, idu);
                        intent.putExtra(TAG_USERNAME, idn);
                        intent.putExtra(TAG_NAMA, idun);
                        intent.putExtra(TAG_JURUSAN, idj);
                        startActivity(intent);
                    } else if (position == 3) {
                        Intent intent = new Intent(Profil.this, Pekerjaan.class);
                        intent.putExtra(TAG_ID, idu);
                        intent.putExtra(TAG_USERNAME, idn);
                        intent.putExtra(TAG_NAMA, idun);
                        intent.putExtra(TAG_JURUSAN, idj);
                        startActivity(intent);
                    } else if (position == 4) {
                        Intent intent = new Intent(Profil.this, PasswordLama.class);
                        intent.putExtra(TAG_ID, idu);
                        intent.putExtra(TAG_USERNAME, idn);
                        intent.putExtra(TAG_NAMA, idun);
                        intent.putExtra(TAG_JURUSAN, idj);
                        startActivity(intent);
                    }

                }
            }
        });

        ambilfoto();
        fotoProfile = (ImageView) findViewById(R.id.image_user);
        fotoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idu = getIntent().getStringExtra(TAG_ID);
                idn = getIntent().getStringExtra(TAG_USERNAME);
                idun = getIntent().getStringExtra(TAG_NAMA);
                idj = getIntent().getStringExtra(TAG_JURUSAN);
                sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
                session = sharedpreferences.getBoolean(session_status, false);
                Intent intent = new Intent(Profil.this, FotoProfil.class);
                intent.putExtra(TAG_ID, idu);
                intent.putExtra(TAG_USERNAME, idn);
                intent.putExtra(TAG_NAMA, idun);
                intent.putExtra(TAG_JURUSAN, idj);
                startActivity(intent);
            }
        });

    }

    private void ambilfoto()
    {
        progressDialog = new ProgressDialog(Profil.this);
        progressDialog.setMessage("Memuat ...");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray dataArray= new JSONArray(response);
                    progressDialog.dismiss();
                    for (int i =0; i<dataArray.length(); i++) {

                        JSONObject obj = dataArray.getJSONObject(i);
                        int extraId = Integer.parseInt(getIntent().getStringExtra(TAG_ID));
                        int id = obj.getInt("id");

                        if (extraId == id)
                        {
                            String imagePath = obj.getString("gambar");
                            Picasso.with(Profil.this)
                                    .load("http://muslikh.my.id/bkk/image/" + imagePath)
                                    .placeholder(R.drawable.ic_account_circle_black)
                                    .error(R.drawable.ic_account_circle_black)
                                    .fit()
                                    .into(fotoProfile);
                        }
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(Profil.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(Profil.this);
        rQueue.add(request);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed()
    {

        idu = getIntent().getStringExtra(TAG_ID);
        idun = getIntent().getStringExtra(TAG_NAMA);
        idn = getIntent().getStringExtra(TAG_USERNAME);
        idj = getIntent().getStringExtra(TAG_JURUSAN);
        Intent kembali = new Intent(Profil.this, MainActivity.class);
        kembali.putExtra(TAG_ID, idu);
        kembali.putExtra(TAG_USERNAME, idn);
        kembali.putExtra(TAG_NAMA, idun);
        kembali.putExtra(TAG_JURUSAN, idj);
        startActivity(kembali);
        finish();
    }
}




