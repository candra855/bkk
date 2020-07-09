package com.example.can.bkk_master;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.example.can.bkk_master.Server.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.can.bkk_master.Login.my_shared_preferences;
import static com.example.can.bkk_master.Login.session_status;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;
    Fragment fragment = null;

    TextView txt_id, txt_username,username_user,nama_user;
    ImageView img_user;
    String id, username,levelU,level,nama;
    String idu,idun,idn;
    Intent intent;
    SharedPreferences sharedpreferences;
    Boolean session = false;

    public final static String TAG = "Profile";

    public static final String TAG_ID = "id";

    public static final String TAG_USERNAME = "username";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_IMG = "img";
    private static final String TAG_LEVEL = "role_id";

    private long exitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        id = getIntent().getStringExtra(TAG_ID);
        username = getIntent().getStringExtra(TAG_USERNAME);
        nama = getIntent().getStringExtra(TAG_NAMA);

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
        img_user = (ImageView) headerView.findViewById(R.id.imageView);
        username_user.setText(username);
        nama_user.setText(nama);

        Picasso.with(this).load("https://1.bp.blogspot.com/-fYa1pg1wHwc/WD5kIS-CGII/AAAAAAAABhA/l4KSJkcr5lYwhEXMpL5Ai-ckgQVaZA5MgCLcB/s1600/CsiEjnzUIAA177d.jpg").into(img_user);

        if (savedInstanceState == null) {
            fragment = new Beranda();
            callFragment(fragment);
        }

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        username = sharedpreferences.getString(TAG_USERNAME, null);
        nama = sharedpreferences.getString(TAG_NAMA, null);


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
           Toast.makeText(getBaseContext(), "Tekan Back Sekali lagi untuk Keluar", Toast.LENGTH_SHORT).show();
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
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.beranda) {
            fragment = new Beranda();
            callFragment(fragment);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.lamaran) {
            fragment = new Lamaran();
            callFragment(fragment);
        } else if (id == R.id.profil) {
            Intent profil = new Intent(MainActivity.this, Profil.class);
            profil.putExtra(TAG_ID, idu);
            profil.putExtra(TAG_NAMA, idun);
            profil.putExtra(TAG_USERNAME, idn);
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


}
