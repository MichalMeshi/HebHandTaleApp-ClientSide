package com.example.test1;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerConnect {
    static void sendImageNameToServer(String imageName) {
        String serverURL = "http://10.0.2.2:5000/recognize_word_by_name/michtav";

        AsyncTask.execute(() -> {
            try {
                URL url = new URL(serverURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Get the response from the server
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    // Process the response here if needed

                    reader.close();
                    inputStream.close();

                    Log.d("ServerResponse", "Server returned successful response: " + response.toString());
                } else {
                    // Handle the error response here
                    Log.e("ServerResponse", "Server returned error: " + responseCode);
                }

                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("ServerResponse", "Failed to send image name: " + e.getMessage());
            }
        });
    }
}
