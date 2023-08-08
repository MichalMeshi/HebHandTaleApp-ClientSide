package com.example.test1;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TranslateAPI {
    public void translateWord() throws IOException {
        OkHttpClient client = new OkHttpClient();
        System.out.println("In the translate function: ");

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "q=Hello&target=es&source=en");
        Request request = new Request.Builder()
                .url("https://google-translate1.p.rapidapi.com/language/translate/v2")
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("Accept-Encoding", "application/gzip")
                .addHeader("X-RapidAPI-Key", "2e310832a1mshc3c3095fad92cf2p13d8e8jsn272f97d004bf")
                .addHeader("X-RapidAPI-Host", "google-translate1.p.rapidapi.com")
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println("Response: " + responseBody);

        // Parse the JSON response and extract the translated word
        try {
            JSONObject json = new JSONObject(responseBody);
            JSONObject data = json.getJSONObject("data");
            JSONObject translations = data.getJSONArray("translations").getJSONObject(0);
            String translatedWord = translations.getString("translatedText");
            System.out.println("Translated Word: " + translatedWord);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
