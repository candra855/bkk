package id.coba.bkk;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.coba.bkk.server.Server;

public class DetailLowongan extends AppCompatActivity {

    final String url = Server.URL + "lowongan_tampil.php";

    TextView id_lowongan,judul_tampil,deskripsi_tampil,jurusan_tampil,tutup_tampil,kualifikasi_tampil,lain_tampil,created_at_tampil,nama_tampil,alamat_tampil;

    public final static String TAG = "Detail";
    public final static String TAG_ID = "id";

    String idindustri;
    RequestQueue requestQueue;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detaillowongan);

        id_lowongan = (TextView) findViewById(R.id.id_lowongan);
        judul_tampil = (TextView)  findViewById(R.id.judul_tampil);
        deskripsi_tampil = (TextView)  findViewById(R.id.deskripsi_tampil);
//        jurusan_tampil = (TextView)  findViewById(R.id.ju);
        tutup_tampil = (TextView)  findViewById(R.id.tutup_tampil);
        kualifikasi_tampil = (TextView)  findViewById(R.id.kualifikasi_tampil);
        lain_tampil = (TextView)  findViewById(R.id.lain_tampil);
//        created_at_tampil = (TextView)  findViewById(R.id.created_at_tampil);

        nama_tampil = (TextView)  findViewById(R.id.nama_industri_tampil);
        alamat_tampil = (TextView)  findViewById(R.id.alamat_industri_tampil);


        ambilData();

    }
    public void ambilData()
    {
        RequestQueue  requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        StringRequest stringRequests =
                new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dataArray= new JSONArray(response);

                            for (int i =0; i<dataArray.length(); i++)
                            {

                                JSONObject obj = dataArray.getJSONObject(i);
                                int extraId = Integer.parseInt(getIntent().getStringExtra(TAG_ID));
                                int id = obj.getInt("id_lowongan");
                                String id_u = obj.getString("id_lowongan");

                                if (extraId== id )
                                {
                                    id_lowongan.setText(id_u);
//                                    id_industri_tampil.setText(obj.getString("id_industri"));
                                    judul_tampil.setText(obj.getString("judul"));
                                    deskripsi_tampil.setText(obj.getString("deskripsi"));
//                                    jurusan_tampil.setText(obj.getString("jurusan"));
                                    tutup_tampil.setText(obj.getString("tutup"));
                                    kualifikasi_tampil.setText(obj.getString("kualifikasi"));
                                    lain_tampil.setText(obj.getString("lain"));
//                                    created_at_tampil.setText(obj.getString("created_at"));

                                    nama_tampil.setText(obj.getString("nama"));
                                    alamat_tampil.setText(obj.getString("alamat"));

                                }
                            }
                            Log.d(TAG, "onResponse:" + response);
                        }  catch(
                                JSONException e)

                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        judul_tampil.setText(error.getLocalizedMessage());
                    }
                });
        requestQueue.add(stringRequests);
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}





