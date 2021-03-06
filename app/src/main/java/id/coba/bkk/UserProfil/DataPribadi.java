package id.coba.bkk.UserProfil;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import id.coba.bkk.Controller.AppController;
import id.coba.bkk.R;
import id.coba.bkk.server.Server;

public class DataPribadi extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText ubah_username,ubah_nama, ubah_jurusan, ubah_email, ubah_telepon, ubah_tgl_lahir, ubah_jenis_kelamin,
            ubah_tahun_lulus, ubah_alamat, ubah_keahlian, ubah_tinggi, ubah_berat;
    private TextView up_id,ubah_tv_tgl_lahir;
//    private Spinner up_sex, up_department_id;
    private Button ubah;

    private String TAG_B = "tag_b";
    String url = Server.URL + "users_tampil.php";

    String url_update  = Server.URL + "users_ubah.php";
    final String TAG ="Edit";
    public final static String TAG_ID = "id";
    public final static String TAG_MESSAGE = "message";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_pribadi);

        ubah_tgl_lahir = (EditText) findViewById(R.id.ubah_tgl_lahir);
        ubah_tgl_lahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(DataPribadi.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG_B, "onDateSet: date: " + year + "/" + month + "/" + day);
                String date = day + "/" + month + "/" + year;
                ubah_tgl_lahir.setText(date);
            }
        };

        editData(url);
    }

    public void editData (String url)
    {

        up_id = (TextView) findViewById(R.id.up_id);
        ubah_username = (EditText) findViewById(R.id.ubah_username);
        ubah_nama = (EditText) findViewById(R.id.ubah_nama);
        ubah_email = (EditText) findViewById(R.id.ubah_email);
        ubah_telepon = (EditText) findViewById(R.id.ubah_telepon);
        ubah_jenis_kelamin = (EditText) findViewById(R.id.ubah_jenis_kelamin);
        ubah_tahun_lulus = (EditText) findViewById(R.id.ubah_tahun_lulus);
        ubah_jurusan = (EditText) findViewById(R.id.ubah_jurusan);
        ubah_tv_tgl_lahir = (TextView) findViewById(R.id.ubah_tgl_lahir);
        ubah_alamat = (EditText) findViewById(R.id.ubah_alamat);
        ubah_keahlian = (EditText) findViewById(R.id.ubah_keahlian);
        ubah_tinggi = (EditText) findViewById(R.id.ubah_tinggi);
        ubah_berat = (EditText) findViewById(R.id.ubah_berat);
        ubah_tv_tgl_lahir = (TextView) findViewById(R.id.ubah_tgl_lahir);

        ubah = (Button) findViewById(R.id.btn_ubah_users);
        ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan();
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequests =
                new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dataArray = new JSONArray(response);

                            for (int i = 0; i < dataArray.length(); i++) {


                                JSONObject obj = dataArray.getJSONObject(i);
                                int extraId = Integer.parseInt(getIntent().getStringExtra(TAG_ID));
                                int id = obj.getInt("id");
                                String id_u = obj.getString("id");
                                String username = obj.getString("username");
                                String nama = obj.getString("nama");
                                String email = obj.getString("email");
                                String telepon = obj.getString("telepon");
                                String jenis_kelamin = obj.getString("jenis_kelamin");
                                String tahun_lulus = obj.getString("tahun_lulus");
                                String jurusan = obj.getString("jurusan");
                                String alamat = obj.getString("alamat");
                                String keahlian = obj.getString("keahlian");
                                String tinggi = obj.getString("tinggi");
                                String berat = obj.getString("berat");
                                String tgl_lahir = obj.getString("tgl_lahir");
                                if (extraId == id) {
                                    up_id.setText(id_u);
                                    ubah_username.setText(username);
                                    ubah_nama.setText(nama);
                                    ubah_email.setText(email);
                                    ubah_telepon.setText(telepon);
                                    ubah_jenis_kelamin.setText(jenis_kelamin);
                                    ubah_tahun_lulus.setText(tahun_lulus);
                                    ubah_jurusan.setText(jurusan);
                                    ubah_tgl_lahir.setText(tgl_lahir);
                                    ubah_alamat.setText(alamat);
                                    ubah_keahlian.setText(keahlian);
                                    ubah_tinggi.setText(tinggi);
                                    ubah_berat.setText(berat);

                                    {

                                    }
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
                        ubah_nama.setText(error.getLocalizedMessage());
                    }
                });
        requestQueue.add(stringRequests);
    }

    private void simpan ()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataObj = new JSONObject(response);

                    String code = dataObj.getString(TAG_MESSAGE);
                    if (code.equals("sukses"))
                    {
                        onBackPressed();
                        onRestart();

                    }else if (code.equals("gagal"))
                    {
                        recreate();
                    }

                    Toast.makeText(DataPribadi.this, dataObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    // adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(DataPribadi.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override

            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();

                map.put("id", up_id.getText().toString());
                map.put("username", ubah_username.getText().toString());
                map.put("nama", ubah_nama.getText().toString());
                map.put("id_jurusan",  ubah_jurusan.getText().toString());
                map.put("email", ubah_email.getText().toString());
                map.put("telepon", ubah_telepon.getText().toString());
                map.put("tgl_lahir", ubah_tv_tgl_lahir.getText().toString());
                map.put("jenis_kelamin",  ubah_jenis_kelamin.getText().toString());
                map.put("tahun_lulus", ubah_tahun_lulus.getText().toString());
                map.put("keahlian", ubah_keahlian.getText().toString());
                map.put("tinggi", ubah_tinggi.getText().toString());
                map.put("berat",  ubah_berat.getText().toString());
                map.put("alamat", ubah_alamat.getText().toString());

                return map;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }


}










