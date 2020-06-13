package id.coba.bkk.Pekerjaan;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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

import id.coba.bkk.Adapter.PekerjaanAdapter;
import id.coba.bkk.Pendidikan.Pendidikan;
import id.coba.bkk.Pendidikan.PendidikanTambah;
import id.coba.bkk.R;
import id.coba.bkk.server.Server;

public class Pekerjaan extends AppCompatActivity {

    public static View.OnClickListener myOnClickListener;
    FloatingActionButton fab;

    private RecyclerView lvpekerjaan;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    String url_read = Server.URL + "pekerjaan_tampil.php";

    public final static String TAG_ID = "id";

    ArrayList<HashMap<String ,String>> list_data;
    PekerjaanAdapter pekerjaanAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pekerjaan);

        fab         = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Pekerjaan.this,PekerjaanTambah.class);
//                finish();
                startActivity(intent);
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        lvpekerjaan = (RecyclerView) findViewById(R.id.lvpekerjaan);
        lvpekerjaan.setLayoutManager(llm);
        list_data = new ArrayList<HashMap<String, String>>();


        requestQueue = Volley.newRequestQueue(Pekerjaan.this);
        stringRequest = new StringRequest(Request.Method.GET, url_read, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONArray dataArray= new JSONArray(response);

                    for (int i =0; i<dataArray.length(); i++)
                    {
                        JSONObject json = dataArray.getJSONObject(i);
//                        int extraId = Integer.parseInt(getIntent().getStringExtra(TAG_ID));
                        int id = json.getInt("user_id");
//                        if (extraId== id )
                        {
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id", json.getString("id"));
                            map.put("tempat", json.getString("tempat"));
                            map.put("masuk", json.getString("masuk"));
                            map.put("keluar", json.getString("keluar"));
                            list_data.add(map);
                            pekerjaanAdapter = new PekerjaanAdapter(Pekerjaan.this, list_data);
                            lvpekerjaan.setAdapter(pekerjaanAdapter);
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
                Toast.makeText(Pekerjaan.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);


    }

}