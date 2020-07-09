package com.example.can.bkk_master.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.can.bkk_master.Controller.AppController;
import com.example.can.bkk_master.DetailLowongan;
import com.example.can.bkk_master.MainActivity;
import com.example.can.bkk_master.R;
import com.example.can.bkk_master.Server.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LamaranAdapter extends RecyclerView.Adapter<LamaranAdapter.ViewHolder>{

    Context context;
    Activity activity;

    public final String del = Server.URL +"lamaran_hapus.php";
    public static final String TAG_ID = "id";
    public static final String TAG_IDL = "idl";
    public final static String TAG = "Pekerjaan";

    String idl;

    ArrayList<HashMap<String ,String >> list_data;
    ArrayList<HashMap<String ,String >>  filterL;
    public LamaranAdapter(Context context, ArrayList<HashMap<String ,String >>list_data)
    {
        this.context = context;
        this.list_data = list_data;
        this.filterL = list_data;
        activity = (Activity) context;
    }
    @Override
    public LamaranAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.costumlist_2, null);
        return new LamaranAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LamaranAdapter.ViewHolder holder, final int position) {

        holder.information_id.setText(list_data.get(position).get("judul"));
        holder.tanggal.setText(list_data.get(position).get("tanggal"));
        holder.status.setText(list_data.get(position).get("status"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  idl = list_data.get(position).get("id_lowongan");
                Intent detail=new Intent(context,DetailLowongan.class);
                detail.putExtra(TAG_IDL,idl);
                context.startActivity(detail);
            }
        });
        holder.batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String  id = list_data.get(position).get("id_lamaran");
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setMessage("Batal ?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
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
                                        activity.recreate();

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
                                map.put("id_lamaran", id);
                                return map;
                            }

                        };

                        AppController.getInstance().addToRequestQueue(stringRequest);
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }

        });
    }

    @Override
    public int getItemCount() {
        return list_data.size();
//        return filter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView information_id,tanggal, status;
        ImageButton batal;

        public ViewHolder(View itemView) {
            super(itemView);

            information_id = (TextView) itemView.findViewById(R.id.txt1_list2);
            tanggal = (TextView) itemView.findViewById(R.id.txt2_list2);
            status = (TextView) itemView.findViewById(R.id.txt3_list2);
            batal = (ImageButton) itemView.findViewById(R.id.batal);
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