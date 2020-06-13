package id.coba.bkk.AdminMenu;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import id.coba.bkk.server.Server;

public class FormUpInformasi extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText up_user_id,up_industry_id,up_title,up_definition,up_deadline,up_requirement,up_other;
    private TextView up_id,up_tv_deadline;
    private Button update;

    private String TAG_B = "tag_b";

    String url = Server.URL + "informasi_tampil.php";
    String url_update  = Server.URL + "informasi_ubah.php";
    final String TAG ="Edit";
    public final static String TAG_ID = "id";
    public final static String TAG_MESSAGE = "message";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_up_informasi);

        editData(url);

        up_deadline = (EditText) findViewById(R.id.up_deadline);
        up_deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(FormUpInformasi.this,
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
                up_tv_deadline.setText(date);
            }
        };


    }

    public void editData (String url)
    {
        up_id = (TextView) findViewById(R.id.up_id_informasi);
        up_industry_id = (EditText) findViewById(R.id.up_industry_id);
        up_title = (EditText) findViewById(R.id.up_title);
        up_definition = (EditText) findViewById(R.id.up_definition);
        up_deadline = (EditText) findViewById(R.id.up_deadline);
        up_requirement = (EditText) findViewById(R.id.up_requirement);
        up_other = (EditText) findViewById(R.id.up_other);
        up_tv_deadline = (TextView) findViewById(R.id.up_deadline);
        up_tv_deadline = (TextView) findViewById(R.id.up_deadline);
        update = (Button) findViewById(R.id.btn_up_information);
        update.setOnClickListener(new View.OnClickListener() {
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
                                String industry_id = obj.getString("id_perusahaan");
                                String title = obj.getString("judul");
                                String definition = obj.getString("deskripsi");
                                String deadline = obj.getString("bts_akhir");
                                String requirement = obj.getString("kualifikasi");
                                String other = obj.getString("lain");
                                if (extraId == id) {
                                    up_id.setText(id_u);
                                    up_industry_id.setText(industry_id);
                                    up_title.setText(title);
                                    up_definition.setText(definition);
                                    up_tv_deadline.setText(deadline);
                                    up_requirement.setText(requirement);
                                    up_other.setText(other);

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
                        up_title.setText(error.getLocalizedMessage());
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

                    Toast.makeText(FormUpInformasi.this, dataObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

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
                Toast.makeText(FormUpInformasi.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override

            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();

                map.put("id", up_id.getText().toString());
                map.put("id_perusahaan", up_industry_id.getText().toString());
                map.put("judul", up_title.getText().toString());
                map.put("deskripsi", up_definition.getText().toString());
                map.put("kualifikasi", up_requirement.getText().toString());
                map.put("lain", up_other.getText().toString());
                map.put("bts_akhir", up_tv_deadline.getText().toString());
                return map;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }


}