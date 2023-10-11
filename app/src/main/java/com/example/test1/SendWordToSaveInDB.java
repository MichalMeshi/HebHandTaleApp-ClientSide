package com.example.test1;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class SendWordToSaveInDB {
    // Method to make the GET request and send the word to the server
    public static void sendWordToServer(String word) {
        // Modify the base URL as needed
        String baseUrl = "https://PORT:IP/add_word_to_popular_words_db/";
        String fullUrl = baseUrl + word;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(fullUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // Handle the failure
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    // Handle the server response as needed
                    System.out.println("Server Response: " + responseBody);
                } else {
                    // Handle the unsuccessful response
                    System.out.println("Server Response Code: " + response.code());
                }
            }
        });
    }
}
