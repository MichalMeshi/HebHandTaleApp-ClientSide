package com.example.test1;

import android.os.AsyncTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class HistoryTableConnection {
    // Define a callback interface
    public interface HistoryTableCallback {
        void onHistoryTableReceived(String historyTable);
        void onHistoryTableError(String errorMessage);
    }

    public static void getHistoryTableOfUser(String user, HistoryTableCallback callback) {
        // Define the URL you want to make a GET request to
        String url = "https://PORT:IP/get_history_by_user/" + user;

        // Create an AsyncTask to perform the network request in the background
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String userUrl = params[0];
                // Create an OkHttpClient
                OkHttpClient client = new OkHttpClient();

                // Create a GET request with the specified URL
                Request request = new Request.Builder()
                        .url(userUrl)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    // Check if the request was successful (status code 200)
                    if (response.isSuccessful()) {
                        // Read the response content as a string
                        return response.body().string();
                    } else {
                        return "Request failed with code: " + response.code();
                    }
                } catch (IOException e) {
                    return e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String result) {
                // This method runs on the main thread, so you can update UI elements here
                if (result.startsWith("Request failed") || result.startsWith("Error:")) {
                    callback.onHistoryTableError(result);
                } else {
                    callback.onHistoryTableReceived(result);
                }
            }
        }.execute(url);
    }
}
