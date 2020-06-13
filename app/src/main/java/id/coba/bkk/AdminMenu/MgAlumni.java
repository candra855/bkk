package id.coba.bkk.AdminMenu;

import android.content.Context;
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

import id.coba.bkk.Adapter.AlumniAdapter;
import id.coba.bkk.AdminActivity;
import id.coba.bkk.R;
import id.coba.bkk.server.Server;

public class MgAlumni extends AppCompatActivity {

    public static View.OnClickListener myOnClickListener;
    FloatingActionButton fab;

    private RecyclerView lvalumni;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    String url_user = Server.URL + "users_read.php";

    public final static String TAG_ID = "id";

    ArrayList<HashMap<String ,String>> list_data;
    AlumniAdapter alumniAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mg_alumni);

//        fab = (FloatingActionButton) findViewById(R.id.fab_add_users);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MgAlumni.this,FormInformasi.class);
////                finish();
//                startActivity(intent);
//            }
//        });


        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        lvalumni = (RecyclerView) findViewById(R.id.lvalumni);
        lvalumni.setLayoutManager(llm);
        list_data = new ArrayList<HashMap<String, String>>();


        requestQueue = Volley.newRequestQueue(MgAlumni.this);
        stringRequest = new StringRequest(Request.Method.GET, url_user, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray dataArray = new JSONArray(response);

                    for (int i = 0; i < dataArray.length(); i++) {

                        JSONObject json = dataArray.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id", json.getString("id"));
                        map.put("username",json.getString("username"));
                        map.put("name",json.getString("name"));
                        map.put("email",json.getString("email"));
                        list_data.add(map);
                        alumniAdapter = new AlumniAdapter(MgAlumni.this,list_data);
                        lvalumni.setAdapter(alumniAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener()
        {
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(MgAlumni.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);

    }



    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(MgAlumni.this,AdminActivity.class);
        startActivity(intent);
        return;
    }


}
