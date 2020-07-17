package com.example.can.bkk_master;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.can.bkk_master.Adapter.BerandaAdapter;
import com.example.can.bkk_master.Server.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.can.bkk_master.Login.TAG_ID;
import static com.example.can.bkk_master.Login.TAG_JURUSAN;
import static com.example.can.bkk_master.Login.my_shared_preferences;
import static com.example.can.bkk_master.Login.session_status;


public class Beranda extends Fragment {

    SwipeRefreshLayout pullToRefresh;
    ProgressDialog pDialog;
    private RecyclerView lvlowongan;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    SharedPreferences sharedpreferences;
    Boolean session = false;

    String id, username,nama,jurusan;

    private static final String TAG = Beranda.class.getSimpleName();

    String url_lowongan = Server.URL + "lowongan_tampil.php";

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_JURUSAN = "id_jurusan";

    ArrayList<HashMap<String ,String>> list_data;
    BerandaAdapter berandaAdapter;
    SearchView cari;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);
        getActivity().setTitle("BKK SMEKPRI");
        setHasOptionsMenu(true);

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
                getActivity().recreate();
//                pullToRefresh.setRefreshing(false);
                pullToRefresh.setEnabled(false);
                pDialog.show();
            }
        });

        lvlowongan = (RecyclerView) view.findViewById(R.id.lvlowongan);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        lvlowongan.setLayoutManager(llm);
        lvlowongan.setVisibility(View.VISIBLE);
        lvlowongan.setHasFixedSize(true);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Memuat ...");
        pDialog.show();

        list_data = new ArrayList<HashMap<String, String>>();

//        TextView t = (TextView) view.findViewById(R.id.t);
//        t.setText(String.valueOf(list_data.size()));

        requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest = new StringRequest(Request.Method.GET, url_lowongan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try{
                    JSONArray dataArray= new JSONArray(response);

                    pDialog.dismiss();
                    for (int i =0; i<dataArray.length(); i++)
                    {
                        JSONObject json = dataArray.getJSONObject(i);
                        {

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id_lowongan", json.getString("id_lowongan"));
//                        map.put("id_industri", json.getString("id_industri"));
                        map.put("id_jurusan", json.getString("id_jurusan"));
                        map.put("nama", json.getString("nama"));
                        map.put("judul", json.getString("judul"));
                        map.put("jurusan", json.getString("jurusan"));
                        map.put("tutup", json.getString("tutup"));
                        map.put("gambar", json.getString("gambar"));
                        list_data.add(map);
                        berandaAdapter = new BerandaAdapter(getActivity(), list_data);
                        lvlowongan.setAdapter(berandaAdapter);
                        berandaAdapter.notifyDataSetChanged();
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
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
        requestQueue.add(stringRequest);


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.cari);
        SearchView searchView  = new SearchView(getActivity());
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setHint("Cari Sesuatu");
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.transparency));
        ImageView searchClose = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchClose.setColorFilter(getResources().getColor(R.color.white));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                berandaAdapter.filter(newText);
                berandaAdapter.notifyDataSetChanged();
                return false;
            }
        });
        searchItem.setActionView(searchView);
    }


}