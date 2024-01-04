package com.example.pr_penjualanhandphone_ardisaputratip212;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomerActivity extends AppCompatActivity {

    Customer customer = new Customer();
    ImageButton btback;
    Button buttonSearch,buttonClearSearch;
    JSONArray arrayData;
    LinearLayout container;
    EditText search;
    ExecutorService pool = Executors.newFixedThreadPool(5);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        btback = (ImageButton) findViewById(R.id.btback);
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
        pool.execute(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            arrayData = new JSONArray(customer.index());
                            for (int i = 0; i < arrayData.length(); i++){
                                MaterialCardView cards = Card();
                                TextView namacust = new TextView(CustomerActivity.this);
                                TextView notelpcust = new TextView(CustomerActivity.this);

                                JSONObject jsonData = arrayData.getJSONObject(i);
                                String strNamaCust = jsonData.optString("nama_customer");
                                String strNoTelpCust = jsonData.optString("no_telp_customer");

                                namacust.setText(strNamaCust);
                                notelpcust.setText(strNoTelpCust);

                                namacust.setPadding(
                                        (int) (25 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (20 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (10 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (0 * getResources().getDisplayMetrics().density + 0.5f)
                                );
                                notelpcust.setPadding(
                                        (int) (150 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (15 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (10 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (0 * getResources().getDisplayMetrics().density + 0.5f)
                                );

                                int textColor = Color.parseColor("#464646");
                                namacust.setTextColor(textColor);
                                notelpcust.setTextColor(textColor);

                                namacust.setTextSize(16);
                                notelpcust.setTextSize(16);

                                cards.addView(namacust);
                                cards.addView(notelpcust);

                                container.addView(cards);
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                            e.getMessage();
                        }
                    }
                });
            }
        });
    }
    public void performSearch(){
        String textSearch = search.getText().toString();
        pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    arrayData = new JSONArray(customer.search(textSearch));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < arrayData.length(); i++){
                                MaterialCardView cards = Card();
                                TextView namacust = new TextView(CustomerActivity.this);
                                TextView notelpcust = new TextView(CustomerActivity.this);

                                JSONObject jsonData = null;
                                try {
                                    jsonData = arrayData.getJSONObject(i);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                String strNamaCust = jsonData.optString("nama_customer");
                                String strNoTelpCust = jsonData.optString("no_telp_customer");

                                namacust.setText(strNamaCust);
                                notelpcust.setText(strNoTelpCust);

                                namacust.setPadding(
                                        (int) (25 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (20 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (10 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (0 * getResources().getDisplayMetrics().density + 0.5f)
                                );
                                notelpcust.setPadding(
                                        (int) (150 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (20 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (10 * getResources().getDisplayMetrics().density + 0.5f),
                                        (int) (0 * getResources().getDisplayMetrics().density + 0.5f)
                                );

                                int textColor = Color.parseColor("#464646");
                                namacust.setTextColor(textColor);
                                notelpcust.setTextColor(textColor);

                                namacust.setTextSize(16);
                                notelpcust.setTextSize(16);

                                cards.addView(namacust);
                                cards.addView(notelpcust);

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
}