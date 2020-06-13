package id.coba.bkk.Adapter;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.coba.bkk.AdminMenu.FormUpInformasi;
import id.coba.bkk.AdminMenu.MgPelamar;
import id.coba.bkk.Controller.AppController;
import id.coba.bkk.R;
import id.coba.bkk.AdminMenu.MgInformasi;
import id.coba.bkk.server.Server;

public class InformasiAdapter extends RecyclerView.Adapter<InformasiAdapter.ViewHolder>{

    String id;
    Context context;
    Activity activity;
    SharedPreferences sharedpreferences;
    Boolean session = false;
    public final String del = Server.URL +"informasi_hapus.php";
    public static final String TAG_ID = "id";
    public static final String TAG_USER_ID = "user_id";
    public final static String TAG = "Info";

    ArrayList<HashMap<String ,String >> list_data;
    ArrayList<HashMap<String ,String >>  filterL;
    public InformasiAdapter(MgInformasi mgInformasi, ArrayList<HashMap<String ,String >>list_data)
    {
        this.context = mgInformasi;
        this.list_data = list_data;
        this.filterL = list_data;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.costumlist_1, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

//        Picasso.with(context).load("http://smknprigen.sch.id/bkk/image/default.png").into(holder.imglist);
        holder.information.setText(list_data.get(position).get("judul"));
        holder.industri.setText(list_data.get(position).get("id_perusahaan"));
        holder.deadline.setText("Batas Akhir "+list_data.get(position).get("bts_akhir"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String  id = list_data.get(position).get("id");
                final String  user_id = list_data.get(position).get("user_id");
                final CharSequence[] options = { "Perbarui","Hapus" };
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
                builder.setTitle("Pilihan");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Perbarui"))
                        {
                                Intent detail=new Intent(context,FormUpInformasi.class);
                                detail.putExtra(TAG_ID,id);
                                context.startActivity(detail);
                        }
//                        else if (options[item].equals("Perbarui"))
//                        {
//                            Intent update = new Intent(context,FormUpInformasi.class);
//                            update.putExtra(TAG_ID,id);
//                            context.startActivity(update);
//                        }

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
                                                    Intent refresh=new Intent(context,MgInformasi.class);
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
                                            map.put("id", id);
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
        TextView information,industri,deadline;
        ImageView imglist;

        public ViewHolder(View itemView) {
            super(itemView);

            information = (TextView) itemView.findViewById(R.id.txt1_list1);
            industri = (TextView) itemView.findViewById(R.id.txt2_list1);
            deadline = (TextView) itemView.findViewById(R.id.txt3_list1);
            imglist = (ImageView) itemView.findViewById(R.id.img_list1);
        }
    }

//    public void filter(String charText)
//    {
//        charText = charText.toLowerCase(Locale.getDefault());
//        list_data = new ArrayList<HashMap<String, String>>();
//        if (charText.length() == 0)
//        {
//            list_data.addAll(filterL);
//        }else {
//            for (HashMap<String, String> item : filterL)
//            {
//                if(item.toString().toLowerCase(Locale.getDefault()).contains(charText))
//                {
//                    list_data.add(item);
//                }
//            }
//        }
//    }

}
