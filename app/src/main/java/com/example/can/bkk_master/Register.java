package com.example.can.bkk_master;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.can.bkk_master.Controller.AppController;
import com.example.can.bkk_master.Server.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText tambah_username,tambah_nama,tambah_email,tambah_telepon,tambah_tahun_lulus,tambah_tgl_lahir,tambah_password;
    private TextView tambah_tv_tgl_lahir;
    private Spinner tambah_jenis_kelamin,tambah_jurusan;
    private Button tambah;

    ProgressDialog pDialog;

    int success;
    ConnectivityManager conMgr;

    private String TAG_B = "tag_b";

    private String TAG = "tag";

    private String TAG_SUCCESS = "success";
    private String TAG_MESSAGE = "message";
    private static String url = Server.URL + "users_tambah.php";

    String tag_json_obj = "json_obj_req";
    private String record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Daftar");
        setSupportActionBar(toolbar);

        //Set icon to toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }

        final RequestQueue request = Volley.newRequestQueue(getApplicationContext());

        tambah_tgl_lahir = (EditText) findViewById(R.id.tambah_tgl_lahir);
        tambah_tgl_lahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Register.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG_B, "onDateSet: date: " + day + "/" + month + "/" + year);
                String date = day + "/" + month + "/" + year;
                tambah_tv_tgl_lahir.setText(date);
            }
        };

        tambah_tv_tgl_lahir = (TextView) findViewById(R.id.tambah_tgl_lahir);
        tambah_username = (EditText) findViewById(R.id.tambah_username);
        tambah_nama = (EditText) findViewById(R.id.tambah_nama);
        tambah_email = (EditText) findViewById(R.id.tambah_email);
        tambah_telepon = (EditText) findViewById(R.id.tambah_telepon);
        tambah_password = (EditText) findViewById(R.id.tambah_password);
        tambah_tahun_lulus = (EditText) findViewById(R.id.tambah_tahun_lulus);
        tambah_jurusan = (Spinner) findViewById(R.id.tambah_jurusan);

        tambah_jenis_kelamin = (Spinner) findViewById(R.id.tambah_jenis_kelamin);
        tambah_jenis_kelamin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tambah = (Button) findViewById(R.id.btn_register);
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = tambah_username.getText().toString();
                String password = tambah_password.getText().toString();
                String nama = tambah_nama.getText().toString();
                String email = tambah_email.getText().toString();
                String telepon = tambah_telepon.getText().toString();
                String jenis_kelamin = String.valueOf(tambah_jenis_kelamin.getSelectedItem());
                String tahun_lulus = tambah_tahun_lulus.getText().toString();
                String jurusan = String.valueOf(tambah_jurusan.getSelectedItemPosition());
                String tgl_lahir = tambah_tgl_lahir.getText().toString();
                String role_id = String.valueOf(3);

                if (conMgr.getActiveNetworkInfo() != null
                        && conMgr.getActiveNetworkInfo().isAvailable()
                        && conMgr.getActiveNetworkInfo().isConnected()) {
                    checkRegister(username, password, nama, email, telepon, jenis_kelamin, tahun_lulus, jurusan, tgl_lahir, role_id);
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void checkRegister(final String username, final String password, final String nama, String email, String telepon, String jenis_kelamin, String tahun_lulus, String department_id, String tgl_lahir, String role_id) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Register ...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject dataObj = new JSONObject(response);


                    int code = Integer.parseInt(dataObj.getString("code"));
                    if (code == 1)
                    {
                        Intent login = new Intent(Register.this, Login.class);
                        finish();
                        startActivity(login);
                    }else if(code == 0)
                    {
//                        daftarGAgal();
                    }


                    Toast.makeText(Register.this, dataObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Toast.makeText(Register.this, error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override

            protected Map<String,String> getParams() {

                Map<String,String> params = new HashMap<String, String>();
                params.put("username", tambah_username.getText().toString());
                params.put("password", tambah_password.getText().toString());
                params.put("nama", tambah_nama.getText().toString());
                params.put("id_jurusan",  String.valueOf(tambah_jurusan.getSelectedItemPosition()));
                params.put("email", tambah_email.getText().toString());
                params.put("telepon", tambah_telepon.getText().toString());
                params.put("tgl_lahir", tambah_tgl_lahir.getText().toString());
                params.put("jenis_kelamin",  tambah_jenis_kelamin.getSelectedItem().toString());
                params.put("tahun_lulus", tambah_tahun_lulus.getText().toString());
                params.put("role_id", String.valueOf(3));

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

//    public void daftarBerhasil()
//    {
//        Intent login = new Intent(Register.this, Login.class);
//        finish();
//        startActivity(login);
//        }
//
//    public void daftarGAgal()
//    {
//
//    }
}
