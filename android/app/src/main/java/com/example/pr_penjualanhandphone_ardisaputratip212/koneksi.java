package com.example.pr_penjualanhandphone_ardisaputratip212;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class koneksi {
    private OkHttpClient httpConn = new OkHttpClient();

    public String run(String url){
        Request request = new Request.Builder().
                url(url).
                build();
        try (Response response = httpConn.newCall(request).execute()){
            return response.body().string();
        } catch (Exception e){
            return e.getMessage();
        }
    }
    public interface ResponseCallback {
        void onResponse(String responseData);
        void onFailure(Exception e);
    }
    public void runAsync(String url, ResponseCallback callback){
        Request request = new Request.Builder().url(url).build();
        httpConn.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }

                    String responseData = responseBody.string();
                    callback.onResponse(responseData);
                }
            }
        });
    }
}
