package id.coba.bkk.UserProfil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import id.coba.bkk.R;

public class Tentang extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tentang);
        getSupportActionBar().setTitle("Tentang Aplikasi");
    }
}
