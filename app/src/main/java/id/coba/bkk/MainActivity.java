package id.coba.bkk;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import static id.coba.bkk.Login.my_shared_preferences;
import static id.coba.bkk.Login.session_status;

public class MainActivity extends AppCompatActivity {

    TextView txt_id, txt_username;
    String id, username,name,levelU,level;


    SharedPreferences sharedpreferences;
    NavigationView navigationView;

    Boolean session = false;

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
        getSupportActionBar().setTitle("Bursa Kerja Khusus SMK");
//
      //  txt_id = (TextView) findViewById(R.id.txt_id);
        //txt_username = (TextView) findViewById(R.id.txt_username);

        id = getIntent().getStringExtra(TAG_ID);
        username = getIntent().getStringExtra(TAG_USERNAME);
       // txt_id.setText("ID : " + id);
       // txt_username.setText("USERNAME : " + username);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);

        //Menampilkan halaman Fragment yang pertama kali muncul
        getFragmentPage(new BerandaFragment());

        /*Inisialisasi BottomNavigationView beserta listenernya untuk
         *menangkap setiap kejadian saat salah satu menu item diklik
         */
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()

        {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;

                //Menantukan halaman Fragment yang akan tampil
                switch (item.getItemId()){
                    case R.id.home:
                        fragment = new BerandaFragment();
                        break;

                    case R.id.application:
                        fragment = new LamaranFragment();
                        break;

                    case R.id.account:

                        fragment  = new ProfilFragment();
//                      getIntent().putExtra(TAG_ID, id);
                        break;


                }
                return getFragmentPage(fragment);
            }
        });

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        levelU = sharedpreferences.getString(TAG_LEVEL, null);
        name = sharedpreferences.getString(TAG_NAME, null);

        if(levelU.equals("2"))
        {

//            nmuser = (TextView) findViewById(R.id.navnamaUser);
//            nmuser.setText(nama);

        }else if(levelU.equals("1")) {
            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
            intent.putExtra(TAG_ID, id);
            intent.putExtra(TAG_USERNAME, username);
            intent.putExtra(TAG_LEVEL, level);
            intent.putExtra(TAG_NAME, name);
            startActivity(intent);
        }

    }

    //Menampilkan halaman Fragment
    private boolean getFragmentPage(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

//    @Override
//    public void onBackPressed()
//    {
////
////        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
////        {
//            moveTaskToBack(true);
////        }
////        else { Toast.makeText(getBaseContext(), "Tekan Sekali Lagi", Toast.LENGTH_SHORT).show(); }
////
////        mBackPressed = System.currentTimeMillis();
//    }

}