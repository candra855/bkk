package id.coba.bkk.AdminMenu;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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


import id.coba.bkk.Adapter.InformasiAdapter;
import id.coba.bkk.AdminActivity;
import id.coba.bkk.Login;
import id.coba.bkk.R;
import id.coba.bkk.Register;
import id.coba.bkk.server.Server;

public class MgInformasi extends AppCompatActivity {

    FloatingActionButton fab;

    private RecyclerView lvinformasi;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    String url_info = Server.URL + "informasi_tampil.php";

    public final static String TAG_ID = "id";

    ArrayList<HashMap<String ,String>> list_data;
    InformasiAdapter informasiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mg_informasi);

        fab = (FloatingActionButton) findViewById(R.id.fab_add_information);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MgInformasi.this,FormInformasi.class);
//                finish();
                startActivity(intent);
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        lvinformasi = (RecyclerView) findViewById(R.id.lvinformasi);
        lvinformasi.setLayoutManager(llm);
        list_data = new ArrayList<HashMap<String, String>>();


        requestQueue = Volley.newRequestQueue(MgInformasi.this);
        stringRequest = new StringRequest(Request.Method.GET, url_info, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray dataArray = new JSONArray(response);

                    for (int i = 0; i < dataArray.length(); i++) {

                        JSONObject json = dataArray.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id", json.getString("id"));
                        map.put("id_perusahaan",json.getString("id_perusahaan"));
                        map.put("judul",json.getString("judul"));
                        map.put("bts_akhir",json.getString("bts_akhir"));
                        list_data.add(map);
                        informasiAdapter = new InformasiAdapter(MgInformasi.this, list_data);
                        lvinformasi.setAdapter(informasiAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener()
        {
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(MgInformasi.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);

}

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(MgInformasi.this,AdminActivity.class);
        startActivity(intent);
        return;
    }


}
