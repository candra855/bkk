package com.example.can.bkk_master;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderLayout;
import com.example.can.bkk_master.Server.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.can.bkk_master.Adapter.BerandaAdapter.TAG_IDL;
import static com.example.can.bkk_master.Login.my_shared_preferences;
import static com.example.can.bkk_master.Login.session_status;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;
    Fragment fragment = null;

    TextView txt_id, txt_username,username_user,nama_user;
    String id, username,levelU,level,nama,jurusan;
    String idu,idun,idn,idj,id_u;
    ImageView fotoProfile;
    Intent intent;
    SharedPreferences sharedpreferences;
    Boolean session = false;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    String url = Server.URL + "users_tampil.php";
    String url_foto = Server.URL + "users_foto_tampil.php";
    public final static String TAG = "Profile";

    public static final String TAG_ID = "id";

    public static final String TAG_USERNAME = "username";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_JURUSAN = "id_jurusan";
    public static final String TAG_IMG = "img";
    private static final String TAG_LEVEL = "role_id";

    private long exitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        username_user = (TextView) headerView.findViewById(R.id.username_user);
        nama_user = (TextView) headerView.findViewById(R.id.nama_user);
        fotoProfile = (ImageView) headerView.findViewById(R.id.imageView);
//        username_user.setText(username);
//        nama_user.setText(nama);

        if (savedInstanceState == null) {
            fragment = new Beranda();
            callFragment(fragment);
        }

        cekdatakosong();
        ambilData();

        if(level.equals("2"))
        {

        }else if(level.equals("3"))
        {
            Intent intent = new Intent(MainActivity.this, TentangValidasi.class);
            intent.putExtra(TAG_ID, id);
            intent.putExtra(TAG_USERNAME, username);
            intent.putExtra(TAG_LEVEL, level);
            intent.putExtra(TAG_NAMA,nama);
            startActivity(intent);
                   }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            doExitApp();
        }
    }

    public void doExitApp(){
        if ((System.currentTimeMillis() - exitTime) > 2000) {
           Toast.makeText(getBaseContext(), "Tekan Lagi Untuk Keluar", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            moveTaskToBack(true);
//            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        idu = getIntent().getStringExtra(TAG_ID);
        idun = getIntent().getStringExtra(TAG_NAMA);
        idn = getIntent().getStringExtra(TAG_USERNAME);
        idj = getIntent().getStringExtra(TAG_JURUSAN);
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.beranda) {
            fragment = new Beranda();
            callFragment(fragment);
        } else if (id == R.id.rekomendasi) {
            fragment = new Rekomendasi();
            callFragment(fragment);
        } else if (id == R.id.lamaran) {
            fragment = new Lamaran();
            callFragment(fragment);
        } else if (id == R.id.profil) {
            Intent profil = new Intent(MainActivity.this, Profil.class);
            profil.putExtra(TAG_ID, idu);
            profil.putExtra(TAG_NAMA, idun);
            profil.putExtra(TAG_USERNAME, idn);
            profil.putExtra(TAG_JURUSAN, idj);
            startActivity(profil);
        } else if (id == R.id.tentang) {
            Intent intent = new Intent(MainActivity.this, Tentang.class);
            startActivity(intent);
        } else if (id == R.id.logout) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(session_status, false);
            editor.putString(TAG_ID, null);
            editor.putString(TAG_USERNAME, null);
            editor.commit();
            intent  = new Intent(MainActivity.this, Login.class);
            finish();
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void callFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit();
        }

    public void ambilData()
    {

        RequestQueue  requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequests =
                new StringRequest(Request.Method.GET, url_foto, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray dataArray= new JSONArray(response);

                            for (int i =0; i<dataArray.length(); i++)

                            {

                                JSONObject obj = dataArray.getJSONObject(i);
                                int extraId = Integer.parseInt(getIntent().getStringExtra(TAG_ID));
                                int id = obj.getInt("id");
                                id_u = obj.getString("id");
                                if (extraId== id )
                                {
                                    username_user.setText("No. KTP "+ obj.getString("username"));
                                    nama_user.setText(obj.getString("nama"));
                                    String imagePath = obj.getString("gambar");
                                    Picasso.with(MainActivity.this)
                                            .load("http://muslikh.my.id/bkk/image/" + imagePath)
                                            .placeholder(R.drawable.ic_account_circle_black)
                                            .error(R.drawable.ic_account_circle_black)
                                            .fit()
                                            .into(fotoProfile);
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
                        username_user.setText(error.getLocalizedMessage());


                    }
                });
        requestQueue.add(stringRequests);
    }

    public void cekdatakosong()
    {

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONArray dataArray= new JSONArray(response);

                    for (int i =0; i<dataArray.length(); i++)
                    {

                        JSONObject obj = dataArray.getJSONObject(i);
                        int extraId = Integer.parseInt(id);
                        int id = obj.getInt("id");
                        String keahlian = obj.getString("keahlian");
                        String alamat = obj.getString("alamat");
                        String tinggi = obj.getString("tinggi");
                        String berat = obj.getString("berat");
                        if(extraId==id) {
//                            nmuser.setText(obj.getString("nama"));
                            if (keahlian.equals("null") || keahlian.isEmpty() ) {
                                Lengkapi();
                            }else if (alamat.equals("null") || alamat.isEmpty() ) {
                                Lengkapi();
                            }else if (tinggi.equals("0") || tinggi.isEmpty() ) {
                                Lengkapi();
                            }else if (berat.equals("0") || berat.isEmpty() ) {
                                Lengkapi();
                            }
                        }

                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);

    }

    public void Lengkapi()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setMessage("Ada data yang masih kosong !")
                .setCancelable(false)
                .setPositiveButton("Lengkapi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        idu = getIntent().getStringExtra(TAG_ID);
                        idun = getIntent().getStringExtra(TAG_NAMA);
                        idn = getIntent().getStringExtra(TAG_USERNAME);
                        idj = getIntent().getStringExtra(TAG_JURUSAN);
                        Intent cek = new Intent(MainActivity.this, DataPribadi.class);
                        cek.putExtra(TAG_ID, idu);
                        cek.putExtra(TAG_NAMA, idun);
                        cek.putExtra(TAG_USERNAME, idn);
                        cek.putExtra(TAG_JURUSAN, idj);
                        startActivity(cek);

                    }
                })
                .setNegativeButton("Nanti saja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog keluar = alert.create();
        keluar.show();
    }

}
