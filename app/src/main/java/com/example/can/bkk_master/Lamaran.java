package com.example.can.bkk_master;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.can.bkk_master.Adapter.LamaranAdapter;
import com.example.can.bkk_master.Server.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.can.bkk_master.Login.my_shared_preferences;
import static com.example.can.bkk_master.Login.session_status;


public class Lamaran extends Fragment {

    private SwipeRefreshLayout pullToRefresh;
    ProgressDialog pDialog;
    private RecyclerView lvstatus;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    SharedPreferences sharedpreferences;
    Boolean session = false;

    private static final String TAG = Lamaran.class.getSimpleName();

    String url_status = Server.URL + "lamaran_tampil.php";

    public final static String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_JURUSAN = "id_jurusan";

    ArrayList<HashMap<String ,String>> list_data;
    LamaranAdapter lamaranAdapter;

    String id, username,nama,jurusan;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lamaran, container, false);

        getActivity().setTitle("BKK SMEKPRI");

        ambilData2();

        sharedpreferences = getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        username = sharedpreferences.getString(TAG_USERNAME, null);
        nama = sharedpreferences.getString(TAG_NAMA, null);
        jurusan = sharedpreferences.getString(TAG_JURUSAN, null);

        pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ambilData2();
                pullToRefresh.setRefreshing(true);
//                pDialog.show();
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        lvstatus = (RecyclerView) view.findViewById(R.id.lvstatus);
        lvstatus.setLayoutManager(llm);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Memuat ...");
        pDialog.show();

        return view;
    }

    private void ambilData2() {
        requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest = new StringRequest(Request.Method.GET, url_status, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    list_data = new ArrayList<HashMap<String, String>>();
                    JSONArray dataArray = new JSONArray(response);

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject json = dataArray.getJSONObject(i);
                        int extraId = Integer.parseInt(getActivity().getIntent().getStringExtra(TAG_ID));
                        int id = json.getInt("id_user");
                        if (extraId == id) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id_lamaran", json.getString("id_lamaran"));
                            map.put("id_lowongan", json.getString("id_lowongan"));
                            map.put("judul", json.getString("judul"));
                            map.put("status", json.getString("status"));
                            map.put("pesan", json.getString("pesan"));
                            map.put("tanggal", json.getString("tanggal"));
                            map.put("tanggal2", json.getString("tanggal2"));
                            list_data.add(map);
                            pDialog.dismiss();
                            pullToRefresh.setRefreshing(false);
                            lamaranAdapter = new LamaranAdapter(getActivity(), list_data);
                            lvstatus.setAdapter(lamaranAdapter);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
        requestQueue.add(stringRequest);
    }

}


