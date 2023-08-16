package com.example.test1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HistoryDisplayActivity extends AppCompatActivity {

    private RecyclerView historyRecyclerView;
    private HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_display);

        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        historyAdapter = new HistoryAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        historyRecyclerView.setLayoutManager(layoutManager);
        historyRecyclerView.setAdapter(historyAdapter);

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
                    System.out.println(historyArray);

                    // Update the adapter with the new history data
                    historyAdapter.setHistoryData(historyArray);

                } catch (JSONException e) {
                    e.printStackTrace();
                    // Handle JSON parsing error
                }
            }

            @Override
            public void onHistoryTableError(String errorMessage) {
                // Handle error, if any
            }
        });
    }


    // Custom RecyclerView Adapter for displaying history cells
    private class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

        private JSONArray historyData;

        public void setHistoryData(JSONArray historyData) {
            this.historyData = historyData;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_cell, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            try {
                JSONObject historyItem = historyData.getJSONObject(position);
                int id = historyItem.getInt("id");
                String word = historyItem.getString("word");
                String translation = historyItem.getString("translation");
                String language = historyItem.getString("language");

                holder.wordTextView.setText("Word: " + word);
                holder.translationTextView.setText("Translation: " + translation);
                holder.languageTextView.setText("Language: " + language);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        @Override
        public int getItemCount() {
            return (historyData != null) ? historyData.length() : 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView wordTextView;
            public TextView translationTextView;
            public TextView languageTextView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                wordTextView = itemView.findViewById(R.id.wordTextView);
                translationTextView = itemView.findViewById(R.id.translationTextView);
                languageTextView = itemView.findViewById(R.id.languageTextView);
            }
        }
    }
}
