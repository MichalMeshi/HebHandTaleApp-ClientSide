package com.example.test1;

import android.content.Context;
import android.content.Intent;
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

    // This method should be called from an Activity or Fragment
    static void sendBase64ImgToServer(Context context, String base64Image) {
        String url = "http://192.168.68.100:5000/recognize_word_by_content";
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
                String decodedResponse = Unicode.decodeUnicode(responseString);
                System.out.println(decodedResponse);

                // Start the WordListActivity and pass the decoded response as an extra
                Intent intent = new Intent(context, WordListActivity.class);
                intent.putExtra("wordList", decodedResponse);
                context.startActivity(intent);

            } else {
                // Handle unsuccessful response here (e.g., log or show an error message)
                Log.e("ServerConnectBase64", "Unsuccessful response: " + response.code() + " " + response.message());
            }
        } catch (IOException e) {
            // Handle IO Exception here (e.g., log or show an error message)
            e.printStackTrace();
        }
    }
}
