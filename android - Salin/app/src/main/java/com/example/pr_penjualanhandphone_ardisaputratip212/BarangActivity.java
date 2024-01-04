package com.example.pr_penjualanhandphone_ardisaputratip212;

import android.util.TypedValue;
import android.widget.*;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BarangActivity extends AppCompatActivity {
    Barang barang = new Barang();
    ImageButton btback;
    Button bttambah,btrefrest,buttonSearch,buttonClearSearch;
    JSONArray arrayData;
    LinearLayout container;
    EditText search;
    private EditText kdBarang, merk, tipe, warna, ram, storage, hargabeli, hargajual, stok;
    private TextView title;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barang);
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        btback = (ImageButton) findViewById(R.id.btback);
        btrefrest = (Button) findViewById(R.id.btrefresh);
        bttambah = (Button) findViewById(R.id.bttambah);
        search = (EditText) findViewById(R.id.search);
        container = (LinearLayout) findViewById(R.id.container) ;
        buttonSearch= (Button) findViewById(R.id.buttonSearch);
        buttonClearSearch = (Button) findViewById(R.id.buttonClearSearch);

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
            public void onClick(View v) { tambah();}
        });

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    container.removeAllViews();
                    performSearch();
                    buttonClearSearch.setVisibility(View.VISIBLE);
                    buttonClearSearch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                            startActivity(getIntent());
                        }
                    });
                    return true;
                }
                return false;
            }
        });
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.removeAllViews();
                performSearch();
                buttonClearSearch.setVisibility(View.VISIBLE);
                buttonClearSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        startActivity(getIntent());
                    }
                });
            }
        });

        fetchData();
    }
    public void fetchData(){
        barang.index(new koneksi.ResponseCallback() {
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
    public void performSearch(){
        String textSearch = search.getText().toString();
        barang.search(textSearch, new koneksi.ResponseCallback() {
            @Override
            public void onResponse(String responseData) {
                try {
                    arrayData = new JSONArray(responseData);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i =0; i < arrayData.length(); i++){
                                MaterialCardView cards = Card();
                                JSONObject jsonData = null;
                                try {
                                    jsonData = arrayData.getJSONObject(i);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                                String merk = jsonData.optString("MERK");
                                String kodeBarang = jsonData.optString("KDBARANG");
                                String stok = jsonData.optString("STOK_BARANG");
                                String id = jsonData.optString("IDBARANG");

                                TextView merkBarang = new TextView(BarangActivity.this);
                                TextView tipeBarang = new TextView(BarangActivity.this);
                                TextView stokBarang = new TextView(BarangActivity.this);

                                merkBarang.setText(merk);
                                tipeBarang.setText(kodeBarang);
                                stokBarang.setText(stok);

                                merkBarang.setPadding(
                                        (int) (15 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (20 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (10 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (0 * getResources().getDisplayMetrics().density + 0.5f)
                                );
                                tipeBarang.setPadding(
                                        (int) (100 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (20 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (10 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (0 * getResources().getDisplayMetrics().density + 0.5f)
                                );
                                stokBarang.setPadding(
                                        (int) (235 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (20 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (10 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (0 * getResources().getDisplayMetrics().density + 0.5f)
                                );

                                merkBarang.setTextSize(16);
                                tipeBarang.setTextSize(16);
                                stokBarang.setTextSize(16);

                                int textColor = Color.parseColor("#464646");
                                merkBarang.setTextColor(textColor);
                                tipeBarang.setTextColor(textColor);
                                stokBarang.setTextColor(textColor);

                                Button detail = new Button(BarangActivity.this);
                                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        120
                                );
                                buttonParams.setMargins(
                                        (int) (280 * getResources().getDisplayMetrics().density + 0.5f),
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
                                            String barangStringID = (String) v.getTag();
                                            int barangID = Integer.parseInt(barangStringID);
                                            getDetail(barangID);
                                        }
                                    }
                                });
                                detail.setTextColor(textColor);
                                detail.setTextSize(12);

                                cards.addView(merkBarang);
                                cards.addView(tipeBarang);
                                cards.addView(stokBarang);
                                cards.addView(detail);


                                container.addView(cards);
                            }
                        }
                    });

                } catch (Exception e){
                    e.printStackTrace();
                    e.getMessage();
                }
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
                e.getMessage();
            }
        });

    }

    public void index(String data) {
        try {
            arrayData = new JSONArray(data);
            for (int i = 0; i < arrayData.length(); i++) {
                MaterialCardView cards = Card();

                JSONObject jsonData = arrayData.getJSONObject(i);
                String merk = jsonData.optString("MERK");
                String tipe = jsonData.optString("KDBARANG");
                String stok = jsonData.optString("STOK_BARANG");
                String id = jsonData.optString("IDBARANG");

                TextView merkBarang = new TextView(BarangActivity.this);
                TextView tipeBarang = new TextView(BarangActivity.this);
                TextView stokBarang = new TextView(BarangActivity.this);

                merkBarang.setText(merk);
                tipeBarang.setText(tipe);
                stokBarang.setText(stok);

                merkBarang.setPadding(
                        (int) (15 * getResources().getDisplayMetrics().density + 0.5f),
                        (int) (20 * getResources().getDisplayMetrics().density + 0.5f),
                        (int) (10 * getResources().getDisplayMetrics().density + 0.5f),
                        (int) (0 * getResources().getDisplayMetrics().density + 0.5f)
                );
                tipeBarang.setPadding(
                        (int) (100 * getResources().getDisplayMetrics().density + 0.5f),
                        (int) (20 * getResources().getDisplayMetrics().density + 0.5f),
                        (int) (10 * getResources().getDisplayMetrics().density + 0.5f),
                        (int) (0 * getResources().getDisplayMetrics().density + 0.5f)
                );
                stokBarang.setPadding(
                        (int) (235 * getResources().getDisplayMetrics().density + 0.5f),
                        (int) (20 * getResources().getDisplayMetrics().density + 0.5f),
                        (int) (10 * getResources().getDisplayMetrics().density + 0.5f),
                        (int) (0 * getResources().getDisplayMetrics().density + 0.5f)
                );

                merkBarang.setTextSize(16);
                tipeBarang.setTextSize(16);
                stokBarang.setTextSize(16);

                int textColor = Color.parseColor("#464646");
                merkBarang.setTextColor(textColor);
                tipeBarang.setTextColor(textColor);
                stokBarang.setTextColor(textColor);

                Button detail = new Button(BarangActivity.this);
                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        120
                );
                buttonParams.setMargins(
                        (int) (280 * getResources().getDisplayMetrics().density + 0.5f),
                        (int) (15 * getResources().getDisplayMetrics().density + 0.5f),
                        (int) (10 * getResources().getDisplayMetrics().density + 0.5f),
                        (int) (0 * getResources().getDisplayMetrics().density + 0.5f)
                );
                detail.setLayoutParams(buttonParams);
                detail.setBackgroundColor(Color.WHITE);
                detail.setText("Detail");
                detail.setTag(String.valueOf(id));
                detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isFinishing()) {
                            String barangStringID = (String) v.getTag();
                            int barangID = Integer.parseInt(barangStringID);
                            getDetail(barangID);
                        }
                    }
                });
                detail.setTextColor(textColor);
                detail.setTextSize(12);

                cards.addView(merkBarang);
                cards.addView(tipeBarang);
                cards.addView(stokBarang);
                cards.addView(detail);

                container.addView(cards);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    public ScrollView form(){
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        ScrollView scroll = new ScrollView(this);

        title = new TextView(this);
        title.setTextColor(Color.BLACK);
        title.setTextSize(26);
        title.setText("Tambah Barang");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        title.setPadding(0,0,0,50);

        kdBarang = new EditText(this);
        merk = new EditText(this);
        tipe = new EditText(this);
        warna = new EditText(this);
        ram = new EditText(this);
        storage = new EditText(this);
        hargabeli = new EditText(this);
        hargajual = new EditText(this);
        stok = new EditText(this);

        kdBarang.setTextColor(Color.BLACK);
        merk.setTextColor(Color.BLACK);
        tipe.setTextColor(Color.BLACK);
        warna.setTextColor(Color.BLACK);
        ram.setTextColor(Color.BLACK);
        storage.setTextColor(Color.BLACK);
        hargabeli.setTextColor(Color.BLACK);
        hargajual.setTextColor(Color.BLACK);
        stok.setTextColor(Color.BLACK);

        kdBarang.setHint("Kode Barang");
        merk.setHint("Merk");
        tipe.setHint("Tipe");
        warna.setHint("Warna");
        ram.setHint("RAM");
        storage.setHint("Storage");
        hargabeli.setHint("Harga Beli");
        hargajual.setHint("Harga Jual");
        stok.setHint("Stok Barang");

        kdBarang.setHintTextColor(Color.GRAY);
        merk.setHintTextColor(Color.GRAY);
        tipe.setHintTextColor(Color.GRAY);
        warna.setHintTextColor(Color.GRAY);
        ram.setHintTextColor(Color.GRAY);
        storage.setHintTextColor(Color.GRAY);
        hargabeli.setHintTextColor(Color.GRAY);
        hargajual.setHintTextColor(Color.GRAY);
        stok.setHintTextColor(Color.GRAY);

        hargabeli.setInputType(InputType.TYPE_CLASS_NUMBER);
        hargajual.setInputType(InputType.TYPE_CLASS_NUMBER);
        stok.setInputType(InputType.TYPE_CLASS_NUMBER);

        container.setPadding(25,0, 25,15);
        container.addView(kdBarang);
        container.addView(merk);
        container.addView(tipe);
        container.addView(warna);
        container.addView(ram);
        container.addView(storage);
        container.addView(hargabeli);
        container.addView(hargajual);
        container.addView(stok);
        scroll.addView(container);

        return scroll;
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
    public void tambah(){
        ScrollView scrollForm = form();

        MaterialAlertDialogBuilder formData = new MaterialAlertDialogBuilder(this, R.style.MyDialogStyle);
        //ColorDrawable putih = new ColorDrawable(Color.WHITE);
        //formData.setBackground(putih);
        formData.setCustomTitle(title);


        formData.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String data = barang.store(kdBarang.getText().toString(), merk.getText().toString(),
                        tipe.getText().toString(), warna.getText().toString(), ram.getText().toString(),
                        storage.getText().toString(), hargabeli.getText().toString(), hargajual.getText().toString(),
                        stok.getText().toString());
                Toast.makeText(BarangActivity.this, data, Toast.LENGTH_SHORT).show();
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

        formData.setView(scrollForm);

        formData.show();
    }

    public void getDetail(final int id){
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);

        //JSONArray arrayBarang;
        String kdBarangDetail = "";
        String merkDetail = "";
        String tipeDetail = "";
        String warnaDetail = "";
        String ramDetail = "";
        String storageDetail = "";
        String hargabeliDetail = "";
        String hargajualDetail = "";
        String stokDetail = "";
        String idDetail = "";
        try {
            JSONObject jsonDetail = new JSONObject(barang.getData(id));

            kdBarangDetail = jsonDetail.optString("KDBARANG");
            merkDetail = jsonDetail.optString("MERK");
            tipeDetail = jsonDetail.optString("TIPE");
            warnaDetail = jsonDetail.optString("WARNA");
            ramDetail = jsonDetail.optString("RAM");
            storageDetail = jsonDetail.optString("STORAGE");
            hargabeliDetail = jsonDetail.optString("HARGA_BELI");
            hargajualDetail = jsonDetail.optString("HARGA_JUAL");
            stokDetail = jsonDetail.optString("STOK_BARANG");
            idDetail = jsonDetail.optString("IDBARANG");

            TextView idBarang = new TextView(this);
            TextView kdBarang = new TextView(this);
            TextView merkBarang = new TextView(this);
            TextView tipeBarang = new TextView(this);
            TextView warnaBarang = new TextView(this);
            TextView ramBarang = new TextView(this);
            TextView storageBarang = new TextView(this);
            TextView hargaBeliBarang = new TextView(this);
            TextView hargaJualBarang = new TextView(this);
            TextView stokBarang = new TextView(this);

            idBarang.setTextSize(15);
            merkBarang.setTextSize(15);
            kdBarang.setTextSize(15);
            tipeBarang.setTextSize(15);
            warnaBarang.setTextSize(15);
            ramBarang.setTextSize(15);
            storageBarang.setTextSize(15);
            hargaBeliBarang.setTextSize(15);
            hargaJualBarang.setTextSize(15);
            stokBarang.setTextSize(15);

            double hargaBeli = Double.parseDouble(hargabeliDetail);
            double hargaJual = Double.parseDouble(hargajualDetail);

            String fHBB = String.format("%,.2f", hargaBeli);
            String fHJB = String.format("%,.2f", hargaJual);

            idBarang.setText("ID\u2003\u2003\u2003\u2003\u2003\u2003: " + idDetail);
            kdBarang.setText("Kode Barang\u2003: " + kdBarangDetail);
            merkBarang.setText("Merk\u2003\u2003\u2003\u2003\u2002: " + merkDetail);
            tipeBarang.setText("Tipe\u2003\u2003\u2002\u2002\u2003: " + tipeDetail);
            warnaBarang.setText("Warna\u2003\u2003\u2002\u2002: " + warnaDetail);
            ramBarang.setText("Ram\u2003\u2003\u2002\u2003: " + ramDetail + " GB");
            storageBarang.setText("Storage\u2003\u2003\u2002 :" + storageDetail + " GB");
            hargaBeliBarang.setText("Harga Beli\u2003 : " + "Rp. " + fHBB);
            hargaJualBarang.setText("Harga Jual\u2003: " +  "Rp. " + fHJB);
            stokBarang.setText("Jumlah Stok\u2003 : " + stokDetail);

            idBarang.setPadding(25,15, 25,15);
            kdBarang.setPadding(25,15, 25,15);
            merkBarang.setPadding(25,15, 25,15);
            tipeBarang.setPadding(25,15, 25,15);
            warnaBarang.setPadding(25,15, 25,15);
            ramBarang.setPadding(25,15, 25,15);
            storageBarang.setPadding(25,15, 25,15);
            hargaBeliBarang.setPadding(25,15, 25,15);
            hargaJualBarang.setPadding(25,15, 25,15);
            stokBarang.setPadding(25,15, 25,150);

            idBarang.setTextColor(Color.BLACK);
            merkBarang.setTextColor(Color.BLACK);
            kdBarang.setTextColor(Color.BLACK);
            tipeBarang.setTextColor(Color.BLACK);
            warnaBarang.setTextColor(Color.BLACK);
            ramBarang.setTextColor(Color.BLACK);
            storageBarang.setTextColor(Color.BLACK);
            hargaBeliBarang.setTextColor(Color.BLACK);
            hargaJualBarang.setTextColor(Color.BLACK);
            stokBarang.setTextColor(Color.BLACK);

            View v = new View(this);
            v.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    5
            ));
            v.setBackgroundColor(Color.parseColor("#B3B3B3"));

            container.setPadding(15,15,15,20);
            container.addView(idBarang);
            container.addView(kdBarang);
            container.addView(merkBarang);
            container.addView(tipeBarang);
            container.addView(warnaBarang);
            container.addView(ramBarang);
            container.addView(storageBarang);
            container.addView(hargaBeliBarang);
            container.addView(hargaJualBarang);
            container.addView(stokBarang);
            container.addView(v);

        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }

        TextView title = new TextView(this);
        title.setTextColor(Color.BLACK);
        title.setTextSize(18);
        title.setText(merkDetail + " " + tipeDetail);
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
        String finalKdBarangDetail = kdBarangDetail;
        String finalMerkDetail = merkDetail;
        String finalTipeDetail = tipeDetail;
        String finalWarnaDetail = warnaDetail;
        String finalramDetail = ramDetail;
        String finalStorageDetail = storageDetail;
        String finalHargaBeliDetail = hargabeliDetail;
        String finalHargaJualDetail = hargajualDetail;
        String finalStokDetail = stokDetail;
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout container = new LinearLayout(BarangActivity.this);
                container.setOrientation(LinearLayout.VERTICAL);
                ScrollView scroll = new ScrollView(BarangActivity.this);

                TextView title = new TextView(BarangActivity.this);
                title.setTextColor(Color.BLACK);
                title.setTextSize(26);
                title.setText("Update Barang");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                }
                title.setPadding(0,0,0,50);

                final EditText kdBarang = new EditText(BarangActivity.this);
                final EditText merk = new EditText(BarangActivity.this);
                final EditText tipe = new EditText(BarangActivity.this);
                final EditText warna = new EditText(BarangActivity.this);
                final EditText ram = new EditText(BarangActivity.this);
                final EditText storage = new EditText(BarangActivity.this);
                final EditText hargabeli = new EditText(BarangActivity.this);
                final EditText hargajual = new EditText(BarangActivity.this);
                final EditText stok = new EditText(BarangActivity.this);

                hargabeli.setInputType(InputType.TYPE_CLASS_NUMBER);
                hargajual.setInputType(InputType.TYPE_CLASS_NUMBER);
                stok.setInputType(InputType.TYPE_CLASS_NUMBER);

                kdBarang.setText(String.valueOf(finalKdBarangDetail));
                merk.setText(String.valueOf(finalMerkDetail));
                tipe.setText(String.valueOf(finalTipeDetail));
                warna.setText(String.valueOf(finalWarnaDetail));
                ram.setText(String.valueOf(finalramDetail));
                storage.setText(String.valueOf(finalStorageDetail));
                hargabeli.setText(String.valueOf(finalHargaBeliDetail));
                hargajual.setText(String.valueOf(finalHargaJualDetail));
                stok.setText(String.valueOf(finalStokDetail));

                kdBarang.setTextColor(Color.BLACK);
                merk.setTextColor(Color.BLACK);
                tipe.setTextColor(Color.BLACK);
                warna.setTextColor(Color.BLACK);
                ram.setTextColor(Color.BLACK);
                storage.setTextColor(Color.BLACK);
                hargabeli.setTextColor(Color.BLACK);
                hargajual.setTextColor(Color.BLACK);
                stok.setTextColor(Color.BLACK);

                container.setPadding(25,0, 25,15);
                container.addView(kdBarang);
                container.addView(merk);
                container.addView(tipe);
                container.addView(warna);
                container.addView(ram);
                container.addView(storage);
                container.addView(hargabeli);
                container.addView(hargajual);
                container.addView(stok);

                MaterialAlertDialogBuilder formData = new MaterialAlertDialogBuilder(BarangActivity.this, R.style.MyDialogStyle);
//                //ColorDrawable putih = new ColorDrawable(Color.WHITE);
//                //formData.setBackground(putih);
                formData.setCustomTitle(title);

                formData.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String data = barang.update(String.valueOf(finalID), kdBarang.getText().toString(),merk.getText().toString(),
                                tipe.getText().toString(), warna.getText().toString(), ram.getText().toString(),
                                storage.getText().toString(), hargabeli.getText().toString(), hargajual.getText().toString(),
                                stok.getText().toString());
                        Toast.makeText(BarangActivity.this, data, Toast.LENGTH_SHORT).show();

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

                scroll.addView(container);
                formData.setView(scroll);

                formData.show();
            }
        });

        delete.setTextColor(buttonColor);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barang.destroy(id);

                finish();
                startActivity(getIntent());
            }
        });
        buttonLayout.addView(close);
        buttonLayout.addView(update);
        buttonLayout.addView(delete);

        container.addView(buttonLayout);
        ScrollView scrollDetail = new ScrollView(this);
        scrollDetail.addView(container);
        detail.setView(scrollDetail);

        if (!isFinishing()) {
            detail.show();
        }
    }
}