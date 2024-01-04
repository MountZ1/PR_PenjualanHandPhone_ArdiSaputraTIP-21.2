package com.example.pr_penjualanhandphone_ardisaputratip212;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    ImageButton btpetugas, btbarang, bttransaksi, btcustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btpetugas = (ImageButton) findViewById(R.id.btpetugas);
        btbarang = (ImageButton) findViewById(R.id.btbarang);
        bttransaksi = (ImageButton) findViewById(R.id.bttranskasi);
        btcustomer = (ImageButton) findViewById(R.id.btcustomer);

        btpetugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPetugasActivity();
            }
        });
        btbarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBarangActivity();
            }
        });
        bttransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTransaksiActivity();
            }
        });
        btcustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCustomerActivity();
            }
        });
    }

    public void startPetugasActivity(){
        Intent start = new Intent(getApplicationContext(), PetugasActivity.class);
        startActivity(start);
    }
    public void startBarangActivity() {
        Intent start = new Intent(getApplicationContext(), BarangActivity.class);
        startActivity(start);
    }
    public void startTransaksiActivity(){
        Intent start = new Intent(getApplicationContext(), TransaksiActivity.class);
        startActivity(start);
    }
    public void startCustomerActivity(){
        Intent start = new Intent(getApplicationContext(), CustomerActivity.class);
        startActivity(start);
    }
}