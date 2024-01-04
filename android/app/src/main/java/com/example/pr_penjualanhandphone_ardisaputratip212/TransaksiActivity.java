package com.example.pr_penjualanhandphone_ardisaputratip212;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Permission;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransaksiActivity extends AppCompatActivity {

    Transaksi transaksi = new Transaksi();
    Barang barang = new Barang();
    Petugas petugas = new Petugas();
    ImageButton btback;
    Button bttambah,btrefrest,buttonSearch,buttonClearSearch;
    JSONArray arrayData, arrayDataPetugas, arrayDataBarang;
    LinearLayout container;
    EditText search;
    private Spinner listBarang, spinnerpetugas;
    private String detailbarang;
    ExecutorService pool = Executors.newFixedThreadPool(5);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        btback = (ImageButton) findViewById(R.id.btback);
        btrefrest = (Button) findViewById(R.id.btrefresh);
        bttambah = (Button) findViewById(R.id.bttambah);
        search = (EditText) findViewById(R.id.search);
        container = (LinearLayout) findViewById(R.id.container);
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

        index();
    }

    public void performSearch(){
        String textSearch = search.getText().toString();
        pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    arrayData = new JSONArray(transaksi.search(textSearch));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < arrayData.length(); i++) {
                                MaterialCardView cards = Card();
                                TextView invoiceNumber = new TextView(TransaksiActivity.this);
                                TextView tglTransaksi = new TextView(TransaksiActivity.this);

                                JSONObject jsonSearch = null;
                                try {
                                    jsonSearch = arrayData.getJSONObject(i);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                String invoice = jsonSearch.optString("invoice");
                                String tanggal = jsonSearch.optString("tanggal_transaksi");
                                String id = jsonSearch.optString("idpembelian");

                                invoiceNumber.setText(invoice);
                                tglTransaksi.setText(tanggal);

                                invoiceNumber.setPadding(
                                        (int) (15 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (20 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (10 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (0 * getResources().getDisplayMetrics().density + 0.5f)
                                );
                                tglTransaksi.setPadding(
                                        (int) (170 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (20 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (10 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (0 * getResources().getDisplayMetrics().density + 0.5f)
                                );

                                int textColor = Color.parseColor("#464646");
                                invoiceNumber.setTextColor(textColor);
                                tglTransaksi.setTextColor(textColor);

                                invoiceNumber.setTextSize(16);
                                tglTransaksi.setTextSize(16);

                                Button detail = new Button(TransaksiActivity.this);
                                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        120
                                );
                                buttonParams.setMargins(
                                        (int) (270 * getResources().getDisplayMetrics().density + 0.5f),
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
                                        if (!isFinishing()){
                                            String barangStringID = (String) v.getTag();
                                            int barangID = Integer.parseInt(barangStringID);
                                            getDetail(barangID);
                                        }
                                    }
                                });
                                detail.setTextColor(textColor);
                                detail.setTextSize(12);

                                cards.addView(invoiceNumber);
                                cards.addView(tglTransaksi);
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
        });
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
    public void index(){
        transaksi.index(new koneksi.ResponseCallback() {
            @Override
            public void onResponse(String responseData) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            arrayData = new JSONArray(responseData);
                            for(int i = 0; i < arrayData.length(); i++){
                                MaterialCardView cards = Card();
                                TextView invoiceNumber = new TextView(TransaksiActivity.this);
                                TextView tglTransaksi = new TextView(TransaksiActivity.this);

                                JSONObject jsonData = arrayData.getJSONObject(i);
                                String strInvoiceNumber = jsonData.optString("invoice");
                                String strTglTransaksi = jsonData.optString("tanggal_transaksi");
                                String id = jsonData.optString("idpembelian");

                                invoiceNumber.setText(strInvoiceNumber);
                                tglTransaksi.setText(strTglTransaksi);

                                invoiceNumber.setPadding(
                                        (int) (15 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (20 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (10 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (0 * getResources().getDisplayMetrics().density + 0.5f)
                                );
                                tglTransaksi.setPadding(
                                        (int) (170 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (20 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (10 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (0 * getResources().getDisplayMetrics().density + 0.5f)
                                );

                                int textColor = Color.parseColor("#464646");
                                invoiceNumber.setTextColor(textColor);
                                tglTransaksi.setTextColor(textColor);

                                invoiceNumber.setTextSize(16);
                                tglTransaksi.setTextSize(16);

                                Button detail = new Button(TransaksiActivity.this);
                                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        120
                                );
                                buttonParams.setMargins(
                                        (int) (270 * getResources().getDisplayMetrics().density + 0.5f),
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
                                        if (!isFinishing()){
                                            String barangStringID = (String) v.getTag();
                                            int barangID = Integer.parseInt(barangStringID);
                                            getDetail(barangID);
                                        }
                                    }
                                });
                                detail.setTextColor(textColor);
                                detail.setTextSize(12);

                                cards.addView(invoiceNumber);
                                cards.addView(tglTransaksi);
                                cards.addView(detail);

                                container.addView(cards);
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }
    public Spinner listbarang(){
        listBarang = new Spinner(this);
        barang.index(new koneksi.ResponseCallback() {
            @Override
            public void onResponse(String responseData) {
                try {
                    arrayDataBarang = new JSONArray(responseData);
                    List<String> spinnerDataList = new ArrayList<>();

                    for (int i = 0; i < arrayDataBarang.length(); i++){
                        JSONObject jsonData = arrayDataBarang.getJSONObject(i);
                        String merk = jsonData.optString("MERK");
                        String kode = jsonData.optString("KDBARANG");

                        String formattedString = merk + " - " + kode;
                        spinnerDataList.add(formattedString);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(TransaksiActivity.this, R.layout.spinner_item, spinnerDataList);
                    adapter.setDropDownViewResource(R.layout.spinner_item);
                    adapter.notifyDataSetChanged();
                    listBarang.setAdapter(adapter);
                    listBarang.setSelection(0);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });

        return listBarang;
    }
    public Spinner datapetugas(){
        spinnerpetugas = new Spinner(this);
        petugas.index(new koneksi.ResponseCallback() {
            @Override
            public void onResponse(String responseData) {
                try {
                    arrayDataPetugas = new JSONArray(responseData);
                    List<String> spinnerDataList = new ArrayList<>();

                    for (int i = 0; i < arrayDataPetugas.length(); i++){
                        JSONObject jsonData = arrayDataPetugas.getJSONObject(i);
                        String nama = jsonData.optString("nama_petugas");
                        String id = jsonData.optString("id_petugas");

                        spinnerDataList.add(id + " - " + nama);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(TransaksiActivity.this, R.layout.spinner_item, spinnerDataList);
                    adapter.setDropDownViewResource(R.layout.spinner_item);
                    adapter.notifyDataSetChanged();
                    spinnerpetugas.setAdapter(adapter);
                    spinnerpetugas.setSelection(0);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });

        return spinnerpetugas;
    }
    public Spinner methodeBayar(){
        Spinner methodebayar = new Spinner(this);
        List<String> datamethod = Arrays.asList("Cash", "Card");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(TransaksiActivity.this, R.layout.spinner_item, datamethod);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        adapter.notifyDataSetChanged();
        methodebayar.setAdapter(adapter);
        methodebayar.setSelection(0);

        return methodebayar;
    }
    public void tambah(){
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        ScrollView scroll = new ScrollView(this);
//        ExecutorService pool = Executors.newFixedThreadPool(5);
        Spinner listbarang = listbarang();
        Spinner namaPetugas = datapetugas();
        Spinner methodebayar = methodeBayar();

        TextView title = new TextView(this);
        title.setTextColor(Color.BLACK);
        title.setTextSize(26);
        title.setText("Tambah Transaksi");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        title.setPadding(0,0,0,50);

        EditText namaCustomer = new EditText(this);
        EditText noTelpCustomer = new EditText(this);
        TextView kdbarang = new TextView(this);
        TextView merkbarang = new TextView(this);
        TextView tipebarang = new TextView(this);
        TextView warnabarang = new TextView(this);
        TextView rambarang = new TextView(this);
        TextView storagebarang = new TextView(this);
        TextView harga = new TextView(this);
        EditText JumlahBeli  = new EditText(this);
        TextView total = new TextView(this);
        TextView idbarangtxt = new TextView(this);
        TextView methodeBayar = new TextView(this);
        TextView idpetugas = new TextView(this);
        TextView namapetugas = new TextView(this);

        listbarang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object selectedItem = listbarang.getSelectedItem();
                String sselectedbarang = selectedItem.toString();
                String[] parts = sselectedbarang.split("-");
                detailbarang = parts[1].trim();

                pool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonDetailBarang = new JSONObject(barang.selectDataonkd(detailbarang));
                            String strkdbarang = jsonDetailBarang.optString("KDBARANG");
                            String strmerkbarang = jsonDetailBarang.optString("MERK");
                            String strtipebarang = jsonDetailBarang.optString("TIPE");
                            String strwarnabarang = jsonDetailBarang.optString("WARNA");
                            String strrambarang = jsonDetailBarang.optString("RAM");
                            String strstoragebarang = jsonDetailBarang.optString("STORAGE");
                            String strharga = jsonDetailBarang.optString("HARGA_JUAL");
                            String strid = jsonDetailBarang.optString("IDBARANG");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    kdbarang.setText("Kode : " + strkdbarang);
                                    merkbarang.setText("Merk : " +strmerkbarang);
                                    tipebarang.setText("Tipe : " + strtipebarang);
                                    warnabarang.setText("Warna : " + strwarnabarang);
                                    rambarang.setText("RAM : " + strrambarang);
                                    storagebarang.setText("Storage : " + strstoragebarang);
                                    harga.setText("Harga : "+ strharga);
                                    idbarangtxt.setText(strid);
                                }
                            });
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        methodebayar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object selecteditem = methodebayar.getSelectedItem();
                String sselectedmethode = selecteditem.toString();
                methodeBayar.setText(sselectedmethode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        namaPetugas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object selecteditem = namaPetugas.getSelectedItem();
                String sselectedpetugas = selecteditem.toString();
                String[] parts = sselectedpetugas.split("-");
                idpetugas.setText(parts[0].trim());
                namapetugas.setText(parts[1].trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        namaCustomer.setTextColor(Color.BLACK);
        noTelpCustomer.setTextColor(Color.BLACK);
        int colordetail = Color.parseColor("#3d3d3d");
        idbarangtxt.setTextColor(colordetail);
        kdbarang.setTextColor(colordetail);
        merkbarang.setTextColor(colordetail);
        tipebarang.setTextColor(colordetail);
        warnabarang.setTextColor(colordetail);
        rambarang.setTextColor(colordetail);
        storagebarang.setTextColor(colordetail);
        harga.setTextColor(colordetail);
        JumlahBeli.setTextColor(Color.BLACK);
        total.setTextColor(Color.BLACK);

        kdbarang.setPadding(35,0,0,10);
        merkbarang.setPadding(35,0,0,10);
        tipebarang.setPadding(35,0,0,10);
        warnabarang.setPadding(35,0,0,10);
        rambarang.setPadding(35,0,0,10);
        storagebarang.setPadding(35,0,0,10);
        harga.setPadding(35,0,0,10);
        total.setPadding(35,0,0,10);

        kdbarang.setTextSize(16);
        merkbarang.setTextSize(16);
        tipebarang.setTextSize(16);
        warnabarang.setTextSize(16);
        rambarang.setTextSize(16);
        storagebarang.setTextSize(16);
        harga.setTextSize(16);
        total.setTextSize(18);

        namaCustomer.setHint("Nama Customer");
        noTelpCustomer.setHint("No Telp Customer");
        JumlahBeli.setHint("Jumlah pembelian");
        JumlahBeli.setHintTextColor(Color.GRAY);
        namaCustomer.setHintTextColor(Color.GRAY);
        noTelpCustomer.setHintTextColor(Color.GRAY);

        JumlahBeli.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int jumlahbrg = Integer.parseInt(JumlahBeli.getText().toString());
                    String[] hargaawalnya = harga.getText().toString().split(": ");
                    int hargaawal = Integer.parseInt(hargaawalnya[1]);
                    int totalharga = hargaawal * jumlahbrg;
                    String formattedTotal = String.format("%,d", totalharga);

                    total.setText(formattedTotal);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
        JumlahBeli.setInputType(InputType.TYPE_CLASS_NUMBER);


        container.setPadding(25,0, 25,15);
        container.addView(namaCustomer);
        container.addView(noTelpCustomer);
        container.addView(listbarang);
        container.addView(kdbarang);
        container.addView(merkbarang);
        container.addView(tipebarang);
        container.addView(warnabarang);
        container.addView(rambarang);
        container.addView(storagebarang);
        container.addView(harga);
        container.addView(JumlahBeli);
        container.addView(total);
        container.addView(methodebayar);
        container.addView(namaPetugas);
        scroll.addView(container);

        MaterialAlertDialogBuilder formData = new MaterialAlertDialogBuilder(this, R.style.MyDialogStyle);
        //ColorDrawable putih = new ColorDrawable(Color.WHITE);
        //formData.setBackground(putih);
        formData.setCustomTitle(title);


        formData.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pool.execute(new Runnable() {
                    @Override
                    public void run() {
                        Random rand = new Random();
                        String formattedDate = null;
                        String anotherFormattedDate = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            LocalDateTime currentDate = LocalDateTime.now();
                            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("ddMMyy");
                            DateTimeFormatter cetakDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                            anotherFormattedDate = currentDate.format(cetakDate);
                            formattedDate = currentDate.format(dateFormatter);
                        }
                        int result = rand.nextInt(1000);
                        String formatNumber = String.format("%04d", result);
                        String invoice = "INV-" + formattedDate + formatNumber;

                        String[] hargabarangperbuahnormal = harga.getText().toString().split(": ");
                        String namaCust = namaCustomer.getText().toString();
                        String noTelpCust = noTelpCustomer.getText().toString();
                        String idBarang = idbarangtxt.getText().toString();
                        String jmlBeli = JumlahBeli.getText().toString();
                        String Total = total.getText().toString();
                        String methodBayar = methodeBayar.getText().toString();
                        String idPetugas = idpetugas.getText().toString();
                        String namaTugas = namapetugas.getText().toString();
                        String kdBrg = kdbarang.getText().toString();
                        String merk = merkbarang.getText().toString();
                        String tipe = tipebarang.getText().toString();
                        String warna = warnabarang.getText().toString();
                        String ram = rambarang.getText().toString();
                        String storage = storagebarang.getText().toString();

                        String data = transaksi.store(invoice, namaCust, noTelpCust, idBarang, hargabarangperbuahnormal[1].trim(),
                                jmlBeli, Total, methodBayar, idPetugas);

                        String finalAnotherFormattedDate = anotherFormattedDate;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TransaksiActivity.this, data, Toast.LENGTH_SHORT).show();
                                cetakTransaksi(invoice, finalAnotherFormattedDate, namaCust, kdBrg, merk, tipe, warna, ram,
                                        storage, hargabarangperbuahnormal[1].trim(), jmlBeli, Total, methodBayar, namaTugas);
                            }
                        });
                    }
                });
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

        formData.setView(scroll);

        formData.show();
    }

    public void getDetail(int id){
        pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonDetail = new JSONObject(transaksi.getData(id));
                    String sinvoice = jsonDetail.optString("invoice");
                    String stanggal = jsonDetail.optString("tanggal_transaksi");
                    String snama_customer = jsonDetail.optString("nama_customer");
                    String skodebarang = jsonDetail.optString("KDBARANG");
                    String smerk = jsonDetail.optString("MERK");
                    String stipe = jsonDetail.optString("TIPE");
                    String swarna = jsonDetail.optString("WARNA");
                    String sram = jsonDetail.optString("RAM");
                    String sstorage = jsonDetail.optString("STORAGE");
                    String sharga_per_barang = jsonDetail.optString("harga_per_barang");
                    String sjumlah_beli = jsonDetail.optString("jumlah_beli");
                    String stotal = jsonDetail.optString("total_transaksi");
                    String smethod = jsonDetail.optString("methode_bayar");
                    String snama_petugas = jsonDetail.optString("nama_petugas");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayout container = new LinearLayout(TransaksiActivity.this);
                            container.setOrientation(LinearLayout.VERTICAL);
                            ScrollView scroll = new ScrollView(TransaksiActivity.this);

                            TextView invoice = new TextView(TransaksiActivity.this);
                            TextView tanggal = new TextView(TransaksiActivity.this);
                            TextView namacustomer = new TextView(TransaksiActivity.this);
                            TextView kdbarang = new TextView(TransaksiActivity.this);
                            TextView merk = new TextView(TransaksiActivity.this);
                            TextView tipe = new TextView(TransaksiActivity.this);
                            TextView warna = new TextView(TransaksiActivity.this);
                            TextView ram = new TextView(TransaksiActivity.this);
                            TextView storage = new TextView(TransaksiActivity.this);
                            TextView hargaperbarang = new TextView(TransaksiActivity.this);
                            TextView jumlah = new TextView(TransaksiActivity.this);
                            TextView total = new TextView(TransaksiActivity.this);
                            TextView method = new TextView(TransaksiActivity.this);
                            TextView namapetugas = new TextView(TransaksiActivity.this);

                            invoice.setTextSize(15);
                            tanggal.setTextSize(15);
                            namacustomer.setTextSize(15);
                            kdbarang.setTextSize(15);
                            merk.setTextSize(15);
                            tipe.setTextSize(15);
                            warna.setTextSize(15);
                            ram.setTextSize(15);
                            storage.setTextSize(15);
                            hargaperbarang.setTextSize(15);
                            jumlah.setTextSize(15);
                            total.setTextSize(15);
                            method.setTextSize(15);
                            namapetugas.setTextSize(15);

                            invoice.setTextColor(Color.BLACK);
                            tanggal.setTextColor(Color.BLACK);
                            namacustomer.setTextColor(Color.BLACK);
                            kdbarang.setTextColor(Color.BLACK);
                            merk.setTextColor(Color.BLACK);
                            tipe.setTextColor(Color.BLACK);
                            warna.setTextColor(Color.BLACK);
                            ram.setTextColor(Color.BLACK);
                            storage.setTextColor(Color.BLACK);
                            hargaperbarang.setTextColor(Color.BLACK);
                            jumlah.setTextColor(Color.BLACK);
                            total.setTextColor(Color.BLACK);
                            method.setTextColor(Color.BLACK);
                            namapetugas.setTextColor(Color.BLACK);

                            double hargaPerBuah = Double.parseDouble(sharga_per_barang);
                            double hargaTotal = Double.parseDouble(stotal);

                            String fHPBB = String.format("%,.2f", hargaPerBuah);
                            String fHTB = String.format("%,.2f", hargaTotal);

                            invoice.setText("Invoice : " + sinvoice);
                            tanggal.setText("Tanggal Beli : " + stanggal);
                            namacustomer.setText("Nama Customer : " + snama_customer);
                            kdbarang.setText("Kode Barang : " + skodebarang);
                            merk.setText("Merk : " + smerk);
                            tipe.setText("Tipe : " + stipe);
                            warna.setText("Warna : " + swarna);
                            ram.setText("RAM : " + sram + " GB");
                            storage.setText("Storage : " + sstorage + " GB");
                            hargaperbarang.setText("Harga per Buah : " + fHPBB);
                            jumlah.setText("Jumlah Beli : " + sjumlah_beli);
                            total.setText("Total Pembelian : " + fHTB);
                            method.setText("Methode Bayar : " + smethod);
                            namapetugas.setText("Nama Petugas : " + snama_petugas);

                            invoice.setPadding(25,15,25,15);
                            tanggal.setPadding(25,15,25,15);
                            namacustomer.setPadding(25,15,25,15);
                            kdbarang.setPadding(25,15,25,15);
                            merk.setPadding(25,15,25,15);
                            tipe.setPadding(25,15,25,15);
                            warna.setPadding(25,15,25,15);
                            ram.setPadding(25,15,25,15);
                            storage.setPadding(25,15,25,15);
                            hargaperbarang.setPadding(25,15,25,15);
                            jumlah.setPadding(25,15,25,15);
                            total.setPadding(25,15,25,15);
                            method.setPadding(25,15,25,15);
                            namapetugas.setPadding(25,15,25,150);

                            View v = new View(TransaksiActivity.this);
                            v.setLayoutParams(new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    5
                            ));
                            v.setBackgroundColor(Color.parseColor("#B3B3B3"));

                            container.setPadding(15,15,15,20);

                            container.addView(invoice);
                            container.addView(tanggal);
                            container.addView(namacustomer);
                            container.addView(kdbarang);
                            container.addView(merk);
                            container.addView(tipe);
                            container.addView(warna);
                            container.addView(ram);
                            container.addView(storage);
                            container.addView(hargaperbarang);
                            container.addView(jumlah);
                            container.addView(total);
                            container.addView(method);
                            container.addView(namapetugas);
                            container.addView(v);

                            TextView title = new TextView(TransaksiActivity.this);
                            title.setTextColor(Color.BLACK);
                            title.setTypeface(title.getTypeface(), Typeface.BOLD);
                            title.setTextSize(18);
                            title.setText(sinvoice);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            }
                            title.setPadding(0, 35, 0, 75);

                            MaterialAlertDialogBuilder detail = new MaterialAlertDialogBuilder(TransaksiActivity.this, R.style.MyDialogStyle);
                            detail.setCustomTitle(title);

                            LinearLayout buttonLayout = new LinearLayout(TransaksiActivity.this);
                            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
                            buttonLayout.setPadding(25,25,25,0);

                            Button close = new Button(TransaksiActivity.this);
                            Button cetakPdf = new Button(TransaksiActivity.this);
                            int buttonColor = Color.parseColor("#507CFF");
                            close.setTextColor(buttonColor);
                            close.setText("CLOSE");
                            close.setBackgroundColor(Color.TRANSPARENT);
                            close.setPadding(
                                    (int) (65 * getResources().getDisplayMetrics().density + 0.5f),
                                    (int) (0 * getResources().getDisplayMetrics().density + 0.5f),
                                    (int) (10 * getResources().getDisplayMetrics().density + 0.5f),
                                    (int) (0 * getResources().getDisplayMetrics().density + 0.5f));
                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                    startActivity(getIntent());
                                }
                            });
                            cetakPdf.setTextColor(buttonColor);
                            cetakPdf.setText("Cetak Ulang");
                            cetakPdf.setBackgroundColor(Color.TRANSPARENT);
                            cetakPdf.setPadding(
                                    (int) (50 * getResources().getDisplayMetrics().density + 0.5f),
                                    (int) (0 * getResources().getDisplayMetrics().density + 0.5f),
                                    (int) (10 * getResources().getDisplayMetrics().density + 0.5f),
                                    (int) (0 * getResources().getDisplayMetrics().density + 0.5f)
                            );
                            cetakPdf.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    cetakTransaksi(sinvoice, stanggal, snama_customer, skodebarang, smerk, stipe, swarna, sram,
                                            sstorage, sharga_per_barang, sjumlah_beli, stotal, smethod, snama_petugas);
                                }
                            });

                            buttonLayout.addView(close);
                            buttonLayout.addView(cetakPdf);
                            container.addView(buttonLayout);
                            scroll.addView(container);
                            detail.setView(scroll);

                            if (!isFinishing()) {
                                detail.show();
                            }
                        }
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    public void cetakTransaksi(String invoice, String tanggal, String nama_customer, String kodebarang, String merk,
                               String tipe, String warna, String ram, String storage, String harga_per_barang,
                               String jumlah_beli, String total, String method, String nama_petugas){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            PdfDocument pdfDocument = new PdfDocument();
            Paint paint = new Paint();
            Paint titlePaint = new Paint();

            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(750,1280,1).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);

            Canvas canvas = page.getCanvas();
            Double hargaPerBarangVal = Double.parseDouble(harga_per_barang);
            String HBPB = String.format("%,.2f", hargaPerBarangVal);

            //barisjudul
            titlePaint.setTextAlign(Paint.Align.CENTER);
            titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            titlePaint.setTextSize(28);
            canvas.drawText(invoice, 350, 50, titlePaint);

            //baris isi
            paint.setColor(Color.BLACK);
            paint.setTextSize(24);
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawText("Invoice", 100, 150, paint);
            canvas.drawText(": " + invoice, 380, 150, paint);

            canvas.drawText("Tanggal", 100, 180, paint);
            canvas.drawText(": " + tanggal, 380, 180, paint);

            canvas.drawText("Nama Customer", 100, 210, paint);
            canvas.drawText(": " + nama_customer, 380, 210, paint);

            canvas.drawText("Kode Barang", 100, 240, paint);
            canvas.drawText(": " + kodebarang, 380, 240, paint);

            canvas.drawText("Merk", 100, 270, paint);
            canvas.drawText(": " + merk, 380, 270, paint);

            canvas.drawText("Tipe", 100, 300, paint);
            canvas.drawText(": " + tipe, 380, 300, paint);

            canvas.drawText("Warna", 100, 330, paint);
            canvas.drawText(": " + warna, 380, 330, paint);

            canvas.drawText("RAM/Storage", 100, 360, paint);
            canvas.drawText(": " + ram + "/" + storage, 380, 360, paint);

            canvas.drawText("Harga Barang", 100, 390, paint);
            canvas.drawText(": Rp. " + HBPB, 380, 390, paint);

            canvas.drawText("Jumlah Beli", 100, 420, paint);
            canvas.drawText(": " + jumlah_beli, 380, 420, paint);

            canvas.drawText("Total Harga", 100, 450, paint);
            canvas.drawText(": Rp. " + total, 380, 450, paint);

            canvas.drawText("Methode Bayar", 100, 480, paint);
            canvas.drawText(": " + method, 380, 480, paint);

            canvas.drawText("Menyatakan bahwa data diatas benar", 100, 640, paint);

            canvas.drawText("Semarang, " + tanggal, 100, 780, paint);
            canvas.drawText(nama_petugas , 100,820, paint);


            pdfDocument.finishPage(page);

            if (ActivityCompat.checkSelfPermission(TransaksiActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                File directory = new File(Environment.getExternalStorageDirectory(), "/PenjualanHandphone");
                if (!directory.exists()) {
                    directory.mkdirs();
                }
            }
            File file = new File(Environment.getExternalStorageDirectory(),"PenjualanHandphone/" + invoice + nama_customer + ".pdf");
            try {
                pdfDocument.writeTo(new FileOutputStream(file));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
            pdfDocument.close();
        }else {
            Toast.makeText(TransaksiActivity.this, "Android harus versi 4.4 atau diatasnya", Toast.LENGTH_SHORT).show();
        }
        finish();
        startActivity(getIntent());
    }
}