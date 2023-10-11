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
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ServerConnectBase64 {
    // Create a TrustManager that trusts your server's certificate
    private static final TrustManager[] trustManagers = new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                    // Add your server certificate validation logic here
                }
            }
    };

    // Create an SSL context with the TrustManager
    private static SSLContext sslContext;
    static {
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(50, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustManagers[0]) // Use the first trust manager
            .hostnameVerifier((hostname, session) -> true) // Bypass hostname verification
            .build();



    // This method should be called from an Activity or Fragment
    static void sendBase64ImgToServer(Context context, String base64Image) {
        String url = "https://PORT:IP/recognize_word_by_content";
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
                Log.e("ServerConnectBase64", "Unsuccessful response: " + response.code() + " " + response.message());
                if (response.body() != null) {
                    Log.e("ServerConnectBase64", "Response Body: " + response.body().string());
                }
            }
        } catch (IOException e) {
            // Handle IO Exception here (e.g., log or show an error message)
            e.printStackTrace();
        }
    }
}
