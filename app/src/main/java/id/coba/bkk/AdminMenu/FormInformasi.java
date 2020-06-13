package id.coba.bkk.AdminMenu;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import id.coba.bkk.UserProfil.DataPribadi;
import id.coba.bkk.server.Server;

public class FormInformasi extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText add_user_id,add_industry_id,add_title,add_definition,add_deadline,add_requirement,add_other;

    private TextView add_tv_deadline;
    private Button simpan;

    private String TAG_B = "tag_b";
    private String TAG = "tag";

    private static String url = Server.URL + "informasi_tambah.php";


    private String TAG_SUCCESS = "success";
    public final static String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_informasi);


        add_deadline = (EditText) findViewById(R.id.add_deadline);
        add_deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(FormInformasi.this,
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
                add_tv_deadline.setText(date);
            }
        };


        final RequestQueue request = Volley.newRequestQueue(getApplicationContext());
        add_industry_id = (EditText) findViewById(R.id.add_industry_id);
        add_title = (EditText) findViewById(R.id.add_title);
        add_definition = (EditText) findViewById(R.id.add_definition);
        add_deadline = (EditText) findViewById(R.id.add_deadline);
        add_requirement = (EditText) findViewById(R.id.add_requirement);
        add_other = (EditText) findViewById(R.id.add_other);
        add_tv_deadline = (TextView) findViewById(R.id.add_deadline);

        simpan = (Button) findViewById(R.id.btn_add_information);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan();
            }
        });

    }

    private void simpan()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataObj = new JSONObject(response);


                    int code = Integer.parseInt(dataObj.getString("code"));
                    if (code == 1)
                    {
                        onBackPressed();
                        onRestart();
                    }else if(code == 0)
                    {
                        recreate();
                    }

                    Toast.makeText(FormInformasi.this, dataObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                Toast.makeText(FormInformasi.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override

            protected Map<String,String> getParams() throws AuthFailureError{

                Map<String,String> map = new HashMap<String, String>();
                map.put("user_id", String.valueOf(1));
                map.put("id_perusahaan", add_industry_id.getText().toString());
                map.put("judul", add_title.getText().toString());
                map.put("deskripsi", add_definition.getText().toString());
                map.put("kualifikasi", add_requirement.getText().toString());
                map.put("lain", add_other.getText().toString());
                map.put("bts_akhir", add_tv_deadline.getText().toString());

                return map;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

}