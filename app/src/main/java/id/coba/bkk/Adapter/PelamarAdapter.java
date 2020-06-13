package id.coba.bkk.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import id.coba.bkk.AdminMenu.MgPelamar;
import id.coba.bkk.R;

public class PelamarAdapter extends RecyclerView.Adapter<PelamarAdapter.ViewHolder>{

    Context context;

    public static final String TAG_ID = "id";
    ArrayList<HashMap<String ,String >> list_data;
    ArrayList<HashMap<String ,String >>  filterL;

    public PelamarAdapter(MgPelamar applicantUser, ArrayList<HashMap<String ,String >>list_data)
    {
        this.context = applicantUser;
        this.list_data = list_data;
        this.filterL = list_data;

    }
    @Override
    public PelamarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.costumlist_pelamar, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

//        Picasso.with(context).load("http://smknprigen.sch.id/bkk/image/default.png").into(holder.imglist);
        holder.user_id.setText(list_data.get(position).get("user_id"));
        holder.status.setText(list_data.get(position).get("status"));
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String  id = list_data.get(position).get("id");
//                Intent detail=new Intent(context,Detail.class);
//                detail.putExtra(TAG_ID,id);
//                context.startActivity(detail);
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return list_data.size();
//        return filter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView user_id,status;
//        ImageView imglist;

        public ViewHolder(View itemView) {
            super(itemView);

            user_id = (TextView) itemView.findViewById(R.id.user_name_read);
            status = (TextView) itemView.findViewById(R.id.status_read);
//            imglist = (ImageView) itemView.findViewById(R.id.imglist);
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
