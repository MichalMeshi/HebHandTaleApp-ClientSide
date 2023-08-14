package com.example.test1;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
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
    private static final String BASE_URL = "http:/192.168.68.110:5000";

    public interface TranslationCallback {
        void onTranslationSuccess(String translation);
        void onTranslationFailure(String errorMessage);
    }

    public void translateWord(String word, String languageCode,String language,String user, TranslationCallback callback) {
        new TranslationTask(callback).execute(word, languageCode, language, user);
    }

    private static class TranslationTask extends AsyncTask<String, Void, String> {
        private TranslationCallback callback;

        public TranslationTask(TranslationCallback callback) {
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... params) {
            String word = params[0];
            String languageCode = params[1];
            String language = params[2];
            String user = params[3];

            OkHttpClient client = new OkHttpClient();

            // Build the URL for the GET request
            String url = BASE_URL + "/translate_word_with_google_api/" + word + "/" + languageCode + "/" + language +"/" + user;

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                } else {
                    return "Translation failed. Status code: " + response.code();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Translation failed. Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (callback != null) {
                if (result.startsWith("Translation failed")) {
                    callback.onTranslationFailure(result);
                } else {
                    callback.onTranslationSuccess(result);
                }
            }
        }
    }
}
