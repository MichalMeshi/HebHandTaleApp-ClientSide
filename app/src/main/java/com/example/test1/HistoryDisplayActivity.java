package com.example.test1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HistoryDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_display);

        // Get the user's email from Google Sign-In (replace with actual logic)

        // Fetch the history table
        fetchAndDisplayHistoryTable(getIntent().getStringExtra("user_email"));
        // Find the back button by its ID
        Button backButton = findViewById(R.id.backButton);

        // Set up click listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the previous fragment (FirstFragment)
                onBackPressed();
            }
        });
    }

    private void fetchAndDisplayHistoryTable(String userEmail) {
        // Fetch the history table using the HistoryTableConnection class
        HistoryTableConnection.getHistoryTableOfUser(userEmail, new HistoryTableConnection.HistoryTableCallback() {
            @Override
            public void onHistoryTableReceived(String responseJson) {
                try {
                    // Parse the JSON response
                    JSONObject jsonObject = new JSONObject(responseJson);
                    JSONArray historyArray = jsonObject.getJSONArray("history");

                    // Process the history list
                    StringBuilder historyStringBuilder = new StringBuilder();
                    for (int i = 0; i < historyArray.length(); i++) {
                        historyStringBuilder.append(historyArray.getString(i)).append("\n");
                    }

                    // Display the history in the TextView
                    TextView historyTextView = findViewById(R.id.historyTextView);
                    historyTextView.setText(historyStringBuilder.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Handle JSON parsing error
                    TextView historyTextView = findViewById(R.id.historyTextView);
                    historyTextView.setText("Error parsing history data");
                }
            }

            @Override
            public void onHistoryTableError(String errorMessage) {
                // Handle error, if any
                TextView historyTextView = findViewById(R.id.historyTextView);
                historyTextView.setText("Error: " + errorMessage);
            }
        });
    }

}
