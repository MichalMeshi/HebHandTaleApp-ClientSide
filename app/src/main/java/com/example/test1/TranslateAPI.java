package com.example.test1;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TranslateAPI {
    public void translateWord(String selectedWord) {
        new TranslateAsyncTask().execute(selectedWord);
    }
    private class TranslateAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String selectedWord = params[0];

            try {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "q=" + selectedWord + "&target=en&source=he");
                Request request = new Request.Builder()
                        .url("https://google-translate1.p.rapidapi.com/language/translate/v2")
                        .post(body)
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("Accept-Encoding", "application/gzip")
                        .addHeader("X-RapidAPI-Key", "cb66237962msh072cf932d6327eep1b0f92jsnc07647de39d9")
                        .addHeader("X-RapidAPI-Host", "google-translate1.p.rapidapi.com")
                        .build();

                Response response = client.newCall(request).execute();
                Log.d("Response", response.toString()); // Log the response for debugging
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
