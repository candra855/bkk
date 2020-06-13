package id.coba.bkk.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import id.coba.bkk.R;

public class ProfilAdapter extends BaseAdapter {
    private String[] menu_profil;
    private Integer[] logo_menu_profil;
    private Activity activity;

    public ProfilAdapter(Activity fragmentProfil, String[] menu_profil, Integer[] logo_menu_profil) {
        this.menu_profil = menu_profil;
        this.logo_menu_profil = logo_menu_profil;
        activity = fragmentProfil;
    }

    @Override
    public int getCount() {
        return menu_profil.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.costumlist_profil, null);

        TextView nama_hewan = (TextView) v.findViewById(R.id.txthewan);
        ImageView gambar_hewan = (ImageView) v.findViewById(R.id.imglist);

        // tampilkan data pada komponen
        nama_hewan.setText(menu_profil[i]);
        gambar_hewan.setImageResource(logo_menu_profil[i]);

        return v;
    }
}

