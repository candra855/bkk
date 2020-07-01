package com.example.can.bkk_master;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.can.bkk_master.Adapter.ProfilAdapter;
import com.example.can.bkk_master.Pekerjaan.Pekerjaan;
import com.example.can.bkk_master.Pendidikan.Pendidikan;
import com.example.can.bkk_master.Server.Server;

import static com.example.can.bkk_master.Login.my_shared_preferences;
import static com.example.can.bkk_master.Login.session_status;

public class Profil extends AppCompatActivity {

    Boolean session = false;

    String url = Server.URL + "users_read.php";

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";

    String idu;
    SharedPreferences sharedpreferences;
    public final static String TAG = "Profile";

    RequestQueue requestQueue;

    ImageView fotoProfile;

    ListView listView;
    private String[] menu_profil = {
            "Data Pribadi",
            "Riwayat Pendidikan",
            "Riwayat Pekerjaan",
            "Ganti Password",};
    private Integer[] logo_menu_profil = {
            R.drawable.ic_account_circle_black,
            R.drawable.ic_school_black,
            R.drawable.ic_work_black,
            R.drawable.ic_key_black,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        sharedpreferences = this.getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

        listView = (ListView) findViewById(R.id.list_menu_profil);

        ProfilAdapter ProfilMenu = new ProfilAdapter(Profil.this, menu_profil, logo_menu_profil);
        listView.setAdapter(ProfilMenu);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idu = getIntent().getStringExtra(TAG_ID);
                sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
                session = sharedpreferences.getBoolean(session_status, false);

                if (session) {
                    if (position == 0) {
                        Intent intent = new Intent(Profil.this, DataPribadi.class);
                        intent.putExtra(TAG_ID, idu);
                        startActivity(intent);
                    } else if (position == 1) {
                        Intent intent = new Intent(Profil.this, Pendidikan.class);
                        intent.putExtra(TAG_ID, idu);
                        startActivity(intent);
                    } else if (position == 2) {
                        Intent intent = new Intent(Profil.this, Pekerjaan.class);
                        intent.putExtra(TAG_ID, idu);
                        startActivity(intent);
                    } else if (position == 3) {
                        Intent intent = new Intent(Profil.this, DataPribadi.class);
                        intent.putExtra(TAG_ID, idu);
                        startActivity(intent);
                     }

                }
            }
        });
//        ambilData();

    }
//    public void ambilData()
//    {
//        RequestQueue  requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
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
//                                int extraId = Integer.parseInt(getActivity().getIntent().getStringExtra(TAG_ID));
//                                int id = obj.getInt("id");
//
//
//                                if (extraId== id )
//                                {
//
//                                    name.setText(obj.getString("name"));
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
//                        name.setText(error.getLocalizedMessage());
//                    }
//                });
//        requestQueue.add(stringRequests);
//    }

    @Override
    public void onResume() {
        super.onResume();

    }
}




