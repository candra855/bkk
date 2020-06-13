package id.coba.bkk;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import id.coba.bkk.Adapter.LamaranAdapter;
import id.coba.bkk.server.Server;


/**
 * A simple {@link Fragment} subclass.
 */
public class LamaranFragment extends Fragment {

    ProgressDialog pDialog;
    private RecyclerView lvstatus;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    private static final String TAG = LamaranFragment.class.getSimpleName();

    String url_status = Server.URL + "lamaran_tampil.php";

    public final static String TAG_ID = "id";

    ArrayList<HashMap<String ,String>> list_data;
    LamaranAdapter lamaranAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_home, container, false);
        View view = inflater.inflate(R.layout.fragment_lamaran, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();


        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        lvstatus = (RecyclerView) view.findViewById(R.id.lvstatus);
        lvstatus.setLayoutManager(llm);
        list_data = new ArrayList<HashMap<String, String>>();

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Proses ...");
        showDialog();

        requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest = new StringRequest(Request.Method.GET, url_status, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, response.toString());
                hideDialog();

                try{
                    JSONArray dataArray= new JSONArray(response);

                    for (int i =0; i<dataArray.length(); i++)
                    {
                        JSONObject json = dataArray.getJSONObject(i);
                        int extraId = Integer.parseInt(getActivity().getIntent().getStringExtra(TAG_ID));
                        int id = json.getInt("id_user");
                        if (extraId== id )
                        {
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id_lamaran", json.getString("id_lamaran"));
                            map.put("id_lowongan", json.getString("id_lowongan"));
                            map.put("judul", json.getString("judul"));
                            map.put("status", json.getString("status"));
                            list_data.add(map);
                            lamaranAdapter = new LamaranAdapter(getActivity(), list_data);
                            lvstatus.setAdapter(lamaranAdapter);
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

                hideDialog();
            }
        });
        requestQueue.add(stringRequest);


        return view;
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
