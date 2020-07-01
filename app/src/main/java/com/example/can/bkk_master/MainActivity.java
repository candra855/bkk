package com.example.can.bkk_master;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.can.bkk_master.Server.Server;

import static com.example.can.bkk_master.Login.my_shared_preferences;
import static com.example.can.bkk_master.Login.session_status;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;
    Fragment fragment = null;

    TextView txt_id, txt_username,nama;
    String id, username,name,levelU,level;
    String idu;
    Intent intent;
    SharedPreferences sharedpreferences;
    Boolean session = false;

    String url = Server.URL + "users_tampil.php";
    public final static String TAG = "Profile";

    public static final String TAG_ID = "id";

    public static final String TAG_USERNAME = "username";
    public static final String TAG_NAME = "nama";
    private static final String TAG_LEVEL = "role_id";

    private long mBackPressed;
    private static final int TIME_INTERVAL = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        id = getIntent().getStringExtra(TAG_ID);
        username = getIntent().getStringExtra(TAG_USERNAME);
        // txt_id.setText("ID : " + id);
        // txt_username.setText("USERNAME : " + username);

//        nama = (TextView) findViewById(R.id.nama);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            fragment = new Beranda();
            callFragment(fragment);
        }

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        levelU = sharedpreferences.getString(TAG_LEVEL, null);
        name = sharedpreferences.getString(TAG_NAME, null);

        if(levelU.equals("2"))
        {

//            nmuser = (TextView) findViewById(R.id.navnamaUser);
//            nmuser.setText(nama);

//        }else if(levelU.equals("1")) {
//            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
//            intent.putExtra(TAG_ID, id);
//            intent.putExtra(TAG_USERNAME, username);
//            intent.putExtra(TAG_LEVEL, level);
//            intent.putExtra(TAG_NAME, name);
//            startActivity(intent);
//        }

        }
//    ambilData();
    }

//    public void ambilData()
//    {
//        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
//        StringRequest stringRequests =
//                new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONArray dataArray= new JSONArray(response);
//
//                            for (int i =0; i<dataArray.length(); i++)
//                            {
//                                JSONObject obj = dataArray.getJSONObject(i);
//                                int extraId = Integer.parseInt(getIntent().getStringExtra(TAG_ID));
//                                int id = obj.getInt("id");
//
//
//                                if (extraId== id )
//                                {
//
//                                    nama.setText(obj.getString("nama"));
//                                }
//                            }
//                            Log.d(TAG, "onResponse:" + response);
//                        }  catch(
//                                JSONException e)
//
//                        {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        nama.setText(error.getLocalizedMessage());
//                    }
//                });
//        requestQueue.add(stringRequests);
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
