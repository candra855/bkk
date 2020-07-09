package com.example.can.bkk_master.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.can.bkk_master.Controller.AppController;
import com.example.can.bkk_master.Pekerjaan.Pekerjaan;
import com.example.can.bkk_master.Pekerjaan.PekerjaanUbah;
import com.example.can.bkk_master.Pendidikan.Pendidikan;
import com.example.can.bkk_master.R;
import com.example.can.bkk_master.Server.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.can.bkk_master.Login.my_shared_preferences;
import static com.example.can.bkk_master.Login.session_status;

public class PekerjaanAdapter extends RecyclerView.Adapter<PekerjaanAdapter.ViewHolder>{

    String id,username,nama;
    Context context;
    Activity activity;

    SharedPreferences sharedpreferences;
    Boolean session = false;

    public final String del = Server.URL +"pekerjaan_hapus.php";
    public static final String TAG_ID = "id";
    public static final String TAG_IDP = "idp";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_NAMA = "nama";
    public final static String TAG = "Pekerjaan";

    ArrayList<HashMap<String ,String >> list_data;
    ArrayList<HashMap<String ,String >>  filterL;
    public PekerjaanAdapter(Context context, ArrayList<HashMap<String ,String >>list_data)
    {
        this.context = context;
        this.list_data = list_data;
        this.filterL = list_data;
        activity = (Activity) context;
    }
    @Override
    public PekerjaanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.costumlist_4, null);
        return new PekerjaanAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PekerjaanAdapter.ViewHolder holder, final int position) {

        sharedpreferences = context.getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        username = sharedpreferences.getString(TAG_USERNAME, null);
        nama = sharedpreferences.getString(TAG_NAMA, null);

        holder.tempat.setText(list_data.get(position).get("tempat"));
        holder.masuk.setText(list_data.get(position).get("masuk"));
        holder.keluar.setText(list_data.get(position).get("keluar"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String  idp = list_data.get(position).get("id_pekerjaan");
                final String  user_id = list_data.get(position).get("user_id");
                final CharSequence[] options = { "Perbarui","Hapus" };
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
                builder.setTitle("Pilihan");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Perbarui"))
                        {
                            Intent detail=new Intent(context,PekerjaanUbah.class);
                            detail.putExtra(TAG_ID, id);
                            detail.putExtra(TAG_IDP, idp);
                            detail.putExtra(TAG_USERNAME, username);
                            detail.putExtra(TAG_NAMA, nama);
                            context.startActivity(detail);
                        }
                        else if (options[item].equals("Hapus")) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            alert.setMessage("Yakin Hapus Informasi Ini ??");
                            alert.setCancelable(false);
                            alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, del, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject dataObj = new JSONObject(response);


                                                Toast.makeText(context, dataObj.getString("message"), Toast.LENGTH_LONG).show();
//                            notifyDataSetChanged();

                                                if (dataObj.getString("message").equals("sukses")) {
                                                    Intent refresh=new Intent(context,Pekerjaan.class);
                                                    refresh.putExtra(TAG_ID, id);
                                                    refresh.putExtra(TAG_USERNAME, username);
                                                    refresh.putExtra(TAG_NAMA, nama);
                                                    context.startActivity(refresh);

                                                } else if (dataObj.getString("message").equals("gagal")) {
                                                    activity.recreate();
                                                }
                                            } catch (JSONException e) {
                                                // JSON error
                                                e.printStackTrace();
                                            }

                                        }
                                    }, new Response.ErrorListener() {

                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e(TAG, "Error: " + error.getMessage());
                                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }) {

                                        @Override

                                        protected Map<String, String> getParams() throws AuthFailureError {


//                        id = activity.getIntent().getStringExtra(TAG_ID);
//                        sharedpreferences = context.getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
                                            Map<String, String> map = new HashMap<String, String>();
                                            map.put("id_pekerjaan", idp);
                                            return map;
                                        }

                                    };

                                    AppController.getInstance().addToRequestQueue(stringRequest);
                                }
                            });
                            alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            AlertDialog keluar = alert.create();
                            keluar.show();
                        }
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_data.size();
//        return filter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tempat,masuk,keluar;
        ImageView imglist;

        public ViewHolder(View itemView) {
            super(itemView);

            tempat = (TextView) itemView.findViewById(R.id.txt1_list4);
            masuk = (TextView) itemView.findViewById(R.id.txt2_list4);
            keluar = (TextView) itemView.findViewById(R.id.txt3_list4);
//           imglist = (ImageView) itemView.findViewById(R.id.img_list1);
        }
    }

    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        list_data = new ArrayList<HashMap<String, String>>();
        if (charText.length() == 0)
        {
            list_data.addAll(filterL);
        }else {
            for (HashMap<String, String> item : filterL)
            {
                if(item.toString().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    list_data.add(item);
                }
            }
        }
    }

}
