package id.coba.bkk;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.squareup.picasso.Picasso;

import id.coba.bkk.Adapter.ProfilAdapter;
import id.coba.bkk.Login;
import id.coba.bkk.Pekerjaan.Pekerjaan;
import id.coba.bkk.Pendidikan.Pendidikan;
import id.coba.bkk.UserProfil.GantiPassword;
import id.coba.bkk.UserProfil.Tentang;
import id.coba.bkk.UserProfil.DataPribadi;
import id.coba.bkk.R;
import id.coba.bkk.server.Server;

import static id.coba.bkk.Login.my_shared_preferences;
import static id.coba.bkk.Login.session_status;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment extends Fragment {

    TextView name;

    NavigationView navigationView;
    Boolean session = false;

    String url = Server.URL + "users_read.php";

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";

    String idu;
    SharedPreferences sharedpreferences;
    public final static String TAG = "Profile";

    RequestQueue requestQueue;

    ImageView fotoProfile;

    Intent intent;
    ListView listView;
    private String[] menu_profil = {
            "Data Pribadi",
            "Riwayat Pendidikan",
            "Riwayat Pekerjaan",
//            "Nilai",
            "Ganti Password",
            "Tentang Aplikasi",
            "Keluar"};
    private Integer[] logo_menu_profil = {
            R.drawable.ic_account_circle_black,
            R.drawable.ic_school_black,
            R.drawable.ic_work_black,
//            R.drawable.ic_assessment_black,
            R.drawable.ic_key_black,
            R.drawable.ic_info_outline_black,
            R.drawable.ic_exit_to_app};

    public ProfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_profil, container, false);
        View view = inflater.inflate(R.layout.fragment_profil, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        fotoProfile = (ImageView) view.findViewById(R.id.image_user);

        Picasso.with(getActivity()).load("http://smknprigen.sch.id/bkk/image/default.png").into(fotoProfile);

        name = (TextView) view.findViewById(R.id.nameuser);

        sharedpreferences = this.getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

        listView = (ListView) view.findViewById(R.id.list_menu_profil);

        ProfilAdapter ProfilMenu = new ProfilAdapter(getActivity(), menu_profil, logo_menu_profil);
        listView.setAdapter(ProfilMenu);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    idu = getActivity().getIntent().getStringExtra(TAG_ID);
                    sharedpreferences = getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
                    session = sharedpreferences.getBoolean(session_status, false);
                     if (session) {
                        Intent profil = new Intent(getActivity(), DataPribadi.class);
                        profil.putExtra(TAG_ID, idu);
                        startActivity(profil);
                    }
                } else if (position == 1) {
                     intent = new Intent(getActivity(), Pendidikan.class);
                     startActivity(intent);
                } else if (position == 2) {
                    intent = new Intent(getActivity(), Pekerjaan.class);
                    startActivity(intent);
                } else if (position == 3) {
                    intent = new Intent(getActivity(), GantiPassword.class);
                    startActivity(intent);
                } else if (position == 4) {
                    intent = new Intent(getActivity(), Tentang.class);
                    startActivity(intent);
                }else if (position == 5) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(session_status, false);
                    editor.putString(TAG_ID, null);
                    editor.putString(TAG_USERNAME, null);
                    editor.commit();
                    intent  = new Intent(getActivity(), Login.class);
                    getActivity().finish();
                    startActivity(intent);
            }

            }
        });
//        ambilData();
        return view;

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




