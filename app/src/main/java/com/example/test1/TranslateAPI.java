package com.example.test1;

import android.os.AsyncTask;
import android.util.Log;

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

    public static String translateWord(String word, String language){
        OkHttpClient client = new OkHttpClient();

        // Build the URL for the GET request
        String url = BASE_URL + "/translate_word_with_google_api/" + word + "/" + language;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                // The response seems to be a simple string, not a JSON object
                System.out.println("Translation: " + responseBody);
                return responseBody; // Return the response as is
            } else {
                System.out.println("Translation failed. Status code: " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


//    public void translateWord(String selectedWord) throws IOException, KeyManagementException, NoSuchAlgorithmException {
//        // Set the custom SSL context in OkHttpClient
//        OkHttpClient client = new OkHttpClient.Builder()
//                .build();
//        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
//        RequestBody body = RequestBody.create(mediaType, "q=Hello%2C%20world!&target=es&source=en");
//        Request request = new Request.Builder()
//                .url("https://google-translate1.p.rapidapi.com/language/translate/v2")
//                .post(body)
//                .addHeader("content-type", "application/x-www-form-urlencoded")
//                .addHeader("Accept-Encoding", "application/gzip")
//                .addHeader("X-RapidAPI-Key", "cb66237962msh072cf932d6327eep1b0f92jsnc07647de39d9")
//                .addHeader("X-RapidAPI-Host", "google-translate1.p.rapidapi.com")
//                .build();
//
//        Response response = client.newCall(request).execute();
//        int statusCode = response.code();
//        String responseBody = response.body().string();
//        System.out.println("Status Code: " + statusCode);
//        System.out.println("Response Body: " + responseBody);
//    }
}
