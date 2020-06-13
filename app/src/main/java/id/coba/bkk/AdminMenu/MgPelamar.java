package id.coba.bkk.AdminMenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import id.coba.bkk.Adapter.PelamarAdapter;
import id.coba.bkk.R;
import id.coba.bkk.server.Server;

public class MgPelamar extends AppCompatActivity {

    private RecyclerView lvstatus;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    String url_status = Server.URL + "applicant_user_read.php";

    public final static String TAG_ID = "id";

    ArrayList<HashMap<String ,String>> list_data;
    PelamarAdapter appUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applicant_user);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        lvstatus = (RecyclerView) findViewById(R.id.lvapp_user);
        lvstatus.setLayoutManager(llm);
        list_data = new ArrayList<HashMap<String, String>>();


        requestQueue = Volley.newRequestQueue(this);
        stringRequest = new StringRequest(Request.Method.GET, url_status, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONArray dataArray= new JSONArray(response);

                    for (int i =0; i<dataArray.length(); i++)
                    {
                        JSONObject json = dataArray.getJSONObject(i);
                        int extraId = Integer.parseInt(getIntent().getStringExtra(TAG_ID));
                        int id = json.getInt("information_id");
                        if (extraId== id ) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("information_id", json.getString("information_id"));
                            map.put("user_id", json.getString("user_id"));
                            map.put("status", json.getString("status"));
                            list_data.add(map);
                            appUserAdapter = new PelamarAdapter(MgPelamar.this, list_data);
                            lvstatus.setAdapter(appUserAdapter);
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
                Toast.makeText(MgPelamar.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);


    }

}



