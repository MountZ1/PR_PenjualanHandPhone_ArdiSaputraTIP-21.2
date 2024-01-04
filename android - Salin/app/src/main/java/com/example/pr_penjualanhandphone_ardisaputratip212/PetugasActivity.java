package com.example.pr_penjualanhandphone_ardisaputratip212;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

public class PetugasActivity extends AppCompatActivity {
    Petugas petugas = new Petugas();
    ImageButton btback;
    Button bttambah,btrefrest;
    JSONArray arrayData;
    LinearLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        btback = (ImageButton) findViewById(R.id.btback);
        btrefrest = (Button) findViewById(R.id.btrefresh);
        bttambah = (Button) findViewById(R.id.bttambah);
        //card = (MaterialCardView) findViewById(R.id.card);
        container = (LinearLayout) findViewById(R.id.container);


        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btrefrest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });
        bttambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { form();}
        });

        fetchData();

    }
    public void fetchData(){
        petugas.index(new koneksi.ResponseCallback() {
            @Override
            public void onResponse(String responseData) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        index(responseData);
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
                e.getMessage();
            }
        });
    }
    public void index(String Response){
        try {
            arrayData = new JSONArray(Response);
            for (int i = 0; i < arrayData.length(); i++){
                MaterialCardView cards = Card();

                JSONObject jsonData = arrayData.getJSONObject(i);
                String id = jsonData.optString("id_petugas");
                String nama = jsonData.optString("nama_petugas");
                String jabatan = jsonData.optString("jabatan");

                TextView idPetugas = new TextView(this);
                TextView namaPetugas = new TextView(this);
                TextView jabatanNya = new TextView(this);

                idPetugas.setText(id);
                namaPetugas.setText(nama);
                jabatanNya.setText(jabatan);

                idPetugas.setPadding(
                        (int) (15 * getResources().getDisplayMetrics().density + 0.5f),
                        (int) (20 * getResources().getDisplayMetrics().density + 0.5f),
                        (int) (10 * getResources().getDisplayMetrics().density + 0.5f),
                        (int) (0 * getResources().getDisplayMetrics().density + 0.5f)
                );
                namaPetugas.setPadding(
                        (int) (50 * getResources().getDisplayMetrics().density + 0.5f),
                        (int) (20 * getResources().getDisplayMetrics().density + 0.5f),
                        (int) (10 * getResources().getDisplayMetrics().density + 0.5f),
                        (int) (0 * getResources().getDisplayMetrics().density + 0.5f)
                );
                jabatanNya.setPadding(
                        (int) (180 * getResources().getDisplayMetrics().density + 0.5f),
                        (int) (20 * getResources().getDisplayMetrics().density + 0.5f),
                        (int) (10 * getResources().getDisplayMetrics().density + 0.5f),
                        (int) (0 * getResources().getDisplayMetrics().density + 0.5f));

                int textColor = Color.parseColor("#464646");
                idPetugas.setTextColor(textColor);
                namaPetugas.setTextColor(textColor);
                jabatanNya.setTextColor(textColor);

                idPetugas.setTextSize(16);
                namaPetugas.setTextSize(16);
                jabatanNya.setTextSize(16);

                Button detail = new Button(this);
                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        120
                );
                buttonParams.setMargins(
                        (int) (270 * getResources().getDisplayMetrics().density + 0.5f),
                        (int) (15 * getResources().getDisplayMetrics().density + 0.5f),
                        (int) (10 * getResources().getDisplayMetrics().density + 0.5f),
                        (int) (0 * getResources().getDisplayMetrics().density + 0.5f));
                detail.setLayoutParams(buttonParams);
                detail.setBackgroundColor(Color.WHITE);
                detail.setText("Detail");
                detail.setTag(String.valueOf(id));
                detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isFinishing()){
                            String petugasStringID = (String) v.getTag();
                            int petugasID = Integer.parseInt(petugasStringID);
                            getDetail(petugasID);
                        }
                    }
                });
                detail.setTextColor(textColor);
                detail.setTextSize(12);

                cards.addView(idPetugas);
                cards.addView(namaPetugas);
                cards.addView(jabatanNya);
                cards.addView(detail);


                container.addView(cards);
            }
        } catch (Exception e){
            e.printStackTrace();
            e.getMessage();
        }
    }
    public MaterialCardView Card(){
        MaterialCardView card = new MaterialCardView(this);

        //cards style
        int layoutHeight = 240;
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.MATCH_PARENT,
                layoutHeight
        );
        layoutParams.setMargins(60, 15, 60, 15);
        card.setLayoutParams(layoutParams);
        card.setCardBackgroundColor(Color.WHITE);
        card.setRadius(16);
        card.setStrokeWidth(3);
        card.setCardElevation(16);
        card.setContentPadding(16, 16, 16, 16);
        int strokeColor = Color.parseColor("#bdbdbd");
        card.setStrokeColor(strokeColor);

        return card;
    }
    public void form(){
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);

        TextView title = new TextView(this);
        title.setTextColor(Color.BLACK);
        title.setTextSize(26);
        title.setText("Tambah Petugas");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        title.setPadding(0,0,0,50);

        final EditText namaPetugas = new EditText(this);
        final EditText alamat = new EditText(this);
        final EditText noTelp = new EditText(this);
        final EditText jabatan = new EditText(this);

        namaPetugas.setTextColor(Color.BLACK);
        alamat.setTextColor(Color.BLACK);
        noTelp.setTextColor(Color.BLACK);
        jabatan.setTextColor(Color.BLACK);

        namaPetugas.setHint("Nama Petugas");
        alamat.setHint("Alamat");
        noTelp.setHint("No Telp");
        jabatan.setHint("Jabatan");

        namaPetugas.setHintTextColor(Color.GRAY);
        alamat.setHintTextColor(Color.GRAY);
        noTelp.setHintTextColor(Color.GRAY);
        jabatan.setHintTextColor(Color.GRAY);

        container.setPadding(25,0, 25,15);
        container.addView(namaPetugas);
        container.addView(alamat);
        container.addView(noTelp);
        container.addView(jabatan);

        MaterialAlertDialogBuilder formData = new MaterialAlertDialogBuilder(this, R.style.MyDialogStyle);
        //ColorDrawable putih = new ColorDrawable(Color.WHITE);
        //formData.setBackground(putih);
        formData.setCustomTitle(title);

        formData.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String data = petugas.store(namaPetugas.getText().toString(), alamat.getText().toString(),
                        noTelp.getText().toString(), jabatan.getText().toString());
                Toast.makeText(PetugasActivity.this, data, Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }
        });
        formData.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        formData.setView(container);

        formData.show();
    }
    public void getDetail(final int id) {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);

        //JSONArray arrayPetugas;
        String idDetail = null;
        String namaDetail = null;
        String alamatDetail = null;
        String noTelpDetail = null;
        String jabatanDetail = null;
        try {
            JSONObject jsonDetail = new JSONObject(petugas.getData(id));

            idDetail = jsonDetail.optString("id_petugas");
            namaDetail = jsonDetail.optString("nama_petugas");
            alamatDetail = jsonDetail.optString("alamat");
            noTelpDetail = jsonDetail.optString("no_telp");
            jabatanDetail = jsonDetail.optString("jabatan");

            TextView idPetugas = new TextView(this);
            TextView namaPetugas = new TextView(this);
            TextView alamatPetugas = new TextView(this);
            TextView noPetugas = new TextView(this);
            TextView jabatPetugas = new TextView(this);

            idPetugas.setTextSize(18);
            namaPetugas.setTextSize(18);
            alamatPetugas.setTextSize(18);
            noPetugas.setTextSize(18);
            jabatPetugas.setTextSize(18);

            idPetugas.setTextColor(Color.BLACK);
            namaPetugas.setTextColor(Color.BLACK);
            alamatPetugas.setTextColor(Color.BLACK);
            noPetugas.setTextColor(Color.BLACK);
            jabatPetugas.setTextColor(Color.BLACK);

            idPetugas.setText("ID\u2003\u2003\u2003\u2003\u2003: " + idDetail);
            namaPetugas.setText("Nama\u2003\u2003\u2003\u2002: " + namaDetail);
            alamatPetugas.setText("Alamat\u2003\u2003\u2002\u2002: " + alamatDetail);
            noPetugas.setText("No Telp\u2003\u2003\u2002: " + noTelpDetail);
            jabatPetugas.setText("Jabatan\u2003\u2003\u2002: " + jabatanDetail);

            idPetugas.setPadding(25,15, 25,15);
            namaPetugas.setPadding(25,15, 25,15);
            alamatPetugas.setPadding(25,15, 25,15);
            noPetugas.setPadding(25,15, 25,15);
            jabatPetugas.setPadding(25,15, 25,150);

            View v = new View(this);
            v.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    5
            ));
            v.setBackgroundColor(Color.parseColor("#B3B3B3"));

            container.setPadding(15,15,15,20);
            container.addView(idPetugas);
            container.addView(namaPetugas);
            container.addView(alamatPetugas);
            container.addView(noPetugas);
            container.addView(jabatPetugas);
            container.addView(v);

        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }

        TextView title = new TextView(this);
        title.setTextColor(Color.BLACK);
        title.setTextSize(26);
        title.setText("Petugas " + namaDetail);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        title.setPadding(0, 0, 0, 75);

        MaterialAlertDialogBuilder detail = new MaterialAlertDialogBuilder(this, R.style.MyDialogStyle);
        //ColorDrawable putih = new ColorDrawable(Color.WHITE);
        //detail.setBackground(putih);
        detail.setCustomTitle(title);

        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setPadding(
                (int) (23 * getResources().getDisplayMetrics().density + 0.5f),
                (int) (10 * getResources().getDisplayMetrics().density + 0.5f),
                (int) (10 * getResources().getDisplayMetrics().density + 0.5f),
                (int) (0 * getResources().getDisplayMetrics().density + 0.5f)
        );

        Button close = new Button(this);
        Button update = new Button(this);
        Button delete = new Button(this);
        close.setText("Close");
        update.setText("Update");
        delete.setText("Delete");
        close.setBackgroundColor(Color.TRANSPARENT);
        update.setBackgroundColor(Color.TRANSPARENT);
        delete.setBackgroundColor(Color.TRANSPARENT);
        int buttonColor = Color.parseColor("#507CFF");
        close.setTextColor(buttonColor);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });


        update.setTextColor(buttonColor);
        String finalID = idDetail;
        String finalNamaDetail = namaDetail;
        String finalAlamatDetail = alamatDetail;
        String finalNoTelpDetail = noTelpDetail;
        String finalJabatanDetail = jabatanDetail;
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout container = new LinearLayout(PetugasActivity.this);
                container.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(PetugasActivity.this);
                title.setTextColor(Color.BLACK);
                title.setTextSize(26);
                title.setText("Petugas " + String.valueOf(finalNamaDetail));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                }
                title.setPadding(0,0,0,50);

                final EditText namaPetugas = new EditText(PetugasActivity.this);
                final EditText alamat = new EditText(PetugasActivity.this);
                final EditText noTelp = new EditText(PetugasActivity.this);
                final EditText jabatan = new EditText(PetugasActivity.this);

                namaPetugas.setTextColor(Color.BLACK);
                alamat.setTextColor(Color.BLACK);
                noTelp.setTextColor(Color.BLACK);
                jabatan.setTextColor(Color.BLACK);

                namaPetugas.setText(String.valueOf(finalNamaDetail));
                alamat.setText(String.valueOf(finalAlamatDetail));
                noTelp.setText(String.valueOf(finalNoTelpDetail));
                jabatan.setText(String.valueOf(finalJabatanDetail));

                namaPetugas.setHintTextColor(Color.GRAY);
                alamat.setHintTextColor(Color.GRAY);
                noTelp.setHintTextColor(Color.GRAY);
                jabatan.setHintTextColor(Color.GRAY);

                container.setPadding(25,0, 25,15);
                container.addView(namaPetugas);
                container.addView(alamat);
                container.addView(noTelp);
                container.addView(jabatan);

                MaterialAlertDialogBuilder formData = new MaterialAlertDialogBuilder(PetugasActivity.this, R.style.MyDialogStyle);
                //ColorDrawable putih = new ColorDrawable(Color.WHITE);
                //formData.setBackground(putih);
                formData.setCustomTitle(title);

                formData.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String data = petugas.update(String.valueOf(finalID), namaPetugas.getText().toString(), alamat.getText().toString(),
                                noTelp.getText().toString(), jabatan.getText().toString());
                        Toast.makeText(PetugasActivity.this, data, Toast.LENGTH_SHORT).show();

                        finish();
                        startActivity(getIntent());
                    }
                });
                formData.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });

                formData.setView(container);

                if (!isFinishing()) {
                    formData.show();
                }
            }
        });

        delete.setTextColor(buttonColor);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                petugas.destroy(id);

                finish();
                startActivity(getIntent());
            }
        });
        buttonLayout.addView(close);
        buttonLayout.addView(update);
        buttonLayout.addView(delete);

        container.addView(buttonLayout);
        detail.setView(container);

        if (!isFinishing()) {
            detail.show();
        }
    }
}