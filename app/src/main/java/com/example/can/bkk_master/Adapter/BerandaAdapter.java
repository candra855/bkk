package com.example.can.bkk_master.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.can.bkk_master.DetailLowongan;
import com.example.can.bkk_master.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import static com.example.can.bkk_master.DetailLowongan.TAG_IDI;
import static com.example.can.bkk_master.Login.my_shared_preferences;
import static com.example.can.bkk_master.Login.session_status;

public class BerandaAdapter extends RecyclerView.Adapter<BerandaAdapter.ViewHolder>{

    Context context;
    Activity activity;
    public static final String TAG_IDL = "idl";
    public static final String TAG_ID = "id";
    ArrayList<HashMap<String ,String >> list_data;
    ArrayList<HashMap<String ,String >>  filterL;

    SharedPreferences sharedpreferences;
    Boolean session = false;
    String id,idi;

    public BerandaAdapter(Context context, ArrayList<HashMap<String ,String >>list_data)
    {
        this.context = context;
        this.list_data = list_data;
        this.filterL = list_data;
        activity = (Activity) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.costumlist_1, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        sharedpreferences = context.getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);

        String fotobase64 = list_data.get(position).get("gambar");
        byte[] decodedString = Base64.decode(fotobase64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        if (fotobase64.isEmpty()) {

            Picasso.with(context).load("http://smknprigen.sch.id/bkk/image/default.png").into(holder.gambar);
        } else if (fotobase64.equals("null")) {

            Picasso.with(context).load("http://smknprigen.sch.id/bkk/image/default.png").into(holder.gambar);
        } else {

            holder.gambar.setImageBitmap(decodedByte);
        }

        holder.information.setText(list_data.get(position).get("judul"));
        holder.industri.setText(list_data.get(position).get("nama"));
        holder.deadline.setText(list_data.get(position).get("tutup"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  idl = list_data.get(position).get("id_lowongan");
                String  idi = list_data.get(position).get("id_industri");
                Intent detail=new Intent(context,DetailLowongan.class);
                detail.putExtra(TAG_IDL,idl);
                detail.putExtra(TAG_IDI,idi);
                detail.putExtra(TAG_ID,id);
                context.startActivity(detail);


            }
        });
    }

    @Override
    public int getItemCount() {
            return  list_data.size();
//        return filter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView information,industri,deadline;
        ImageView gambar;

        public ViewHolder(View itemView) {
            super(itemView);

            information = (TextView) itemView.findViewById(R.id.txt1_list1);
            industri = (TextView) itemView.findViewById(R.id.txt2_list1);
            deadline = (TextView) itemView.findViewById(R.id.txt3_list1);
            gambar = (ImageView) itemView.findViewById(R.id.img_list1);
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

