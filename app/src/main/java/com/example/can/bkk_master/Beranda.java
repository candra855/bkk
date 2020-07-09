package com.example.can.bkk_master;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
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


public class Beranda extends Fragment {

    SwipeRefreshLayout pullToRefresh;
    ProgressDialog pDialog;
    private RecyclerView lvlowongan;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    private static final String TAG = Beranda.class.getSimpleName();

    String url_lowongan = Server.URL + "lowongan_tampil.php";

    ArrayList<HashMap<String ,String>> list_data;
    BerandaAdapter berandaAdapter;
    SearchView cari;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);
        getActivity().setTitle("BKK SMEKPRI");
        setHasOptionsMenu(true);

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
        LinearLayoutManager llm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        lvlowongan.setLayoutManager(llm);
        lvlowongan.setVisibility(View.VISIBLE);
        lvlowongan.setHasFixedSize(true);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Proses ...");
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
                    for (int i =0; i<dataArray.length(); i++) {

                        JSONObject json = dataArray.getJSONObject(i);{

                            HashMap<String, String> map = new HashMap<String, String>();

                        map.put("id_lowongan", json.getString("id_lowongan"));
                            map.put("id_industri", json.getString("id_industri"));

                            map.put("nama", json.getString("nama"));
                        map.put("judul", json.getString("judul"));
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
        searchView.setQueryHint("Cari Sesuatu....");
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