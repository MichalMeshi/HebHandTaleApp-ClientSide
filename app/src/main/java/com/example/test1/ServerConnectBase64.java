package com.example.test1;

import android.util.Log;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class ServerConnectBase64 {

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(50, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .build();

    static String sendBase64ImgToServer(String base64Image) throws Exception {
        String decodedResponse = null;
        String url = "http://10.0.2.2:5000/recognize_word_by_content";
        MediaType mediaType = MediaType.parse("image/png"); // Or image/jpeg, depending on the image format
        RequestBody requestBody = RequestBody.create(base64Image, mediaType);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                String responseString = response.body().string();
                decodedResponse = Unicode.decodeUnicode(responseString);
                System.out.println(decodedResponse);
            } else {
                // Handle unsuccessful response here (e.g., log or throw an exception)
                System.err.println("Unsuccessful response: " + response.code() + " " + response.message());
            }
        } catch (IOException e) {
            // Handle IO Exception here (e.g., log or throw a custom exception)
            e.printStackTrace();
        }
        return decodedResponse;
    }
}
