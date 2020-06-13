package id.coba.bkk.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import id.coba.bkk.DetailLowongan;
import id.coba.bkk.R;

public class BerandaAdapter extends RecyclerView.Adapter<BerandaAdapter.ViewHolder>{

    Context context;
    Activity activity;
    public static final String TAG_ID = "id";
    ArrayList<HashMap<String ,String >> list_data;
    ArrayList<HashMap<String ,String >>  filterL;

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

//        Picasso.with(context).load("http://smknprigen.sch.id/bkk/image/default.png").into(holder.imglist);
        holder.information.setText(list_data.get(position).get("judul"));
        holder.industri.setText(list_data.get(position).get("nama"));
        holder.deadline.setText(list_data.get(position).get("tutup"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  id = list_data.get(position).get("id_lowongan");
                Intent detail=new Intent(context,DetailLowongan.class);
                detail.putExtra(TAG_ID,id);
                context.startActivity(detail);
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
