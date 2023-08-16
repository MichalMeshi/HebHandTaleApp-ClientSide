package com.example.test1;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;


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

    public void translateWord(String word, String languageCode, String language, GoogleSignInAccount acct, TranslationCallback callback) {
        new TranslationTask(callback).execute(word, languageCode, language, acct);
    }


        private static class TranslationTask extends AsyncTask<Object, Void, String> {
            private TranslationCallback callback;

            public TranslationTask(TranslationCallback callback) {
                this.callback = callback;
            }

            @Override
            protected String doInBackground(Object... params) {
                String word = (String) params[0];
                String languageCode = (String) params[1];
                String language = (String) params[2];
                GoogleSignInAccount acct = (GoogleSignInAccount) params[3];

                OkHttpClient client = new OkHttpClient();

                String url;
                if (acct != null) {
                    url = BASE_URL + "/translate_word_with_google_api/" + word + "/" + languageCode + "/" + language + "/" + acct.getEmail();
                } else {
                    url = BASE_URL + "/translate_word_with_google_api/" + word + "/" + languageCode + "/" + language;
                }

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

                try {
                    Response response = client.newCall(request).execute();
                    try {
                        if (response.isSuccessful()) {
                            return response.body().string();
                        } else {
                            return "Translation failed. Status code: " + response.code();
                        }
                    } finally {
                        response.close(); // Close the response body to release resources
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
