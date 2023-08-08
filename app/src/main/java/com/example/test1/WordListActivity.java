package com.example.test1;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WordListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        // Get the decodedResponse from the intent's extras
        String decodedResponse = getIntent().getStringExtra("wordList");
        ArrayList<String> wordList=new ArrayList<>();;

        try {
            // Parse the JSON string into a JSON object
            JSONObject jsonObject = new JSONObject(decodedResponse);

            // Get the "words" array from the JSON object
            JSONArray wordsArray = jsonObject.getJSONArray("words");

            // Create an ArrayList to store the words
            // Iterate through the words array and extract each word
            for (int i = 0; i < wordsArray.length(); i++) {
                String word = wordsArray.getString(i);

                // Remove any leading/trailing whitespace and the surrounding quotes (if any)
                String cleanedWord = word.trim().replaceAll("[\"\\[\\]{}]", "");

                if (!cleanedWord.isEmpty()) {
                    wordList.add(cleanedWord);
                }
            }

            // Now, wordList contains the extracted words from the dictionary
            // You can use the wordList ArrayList as needed.

        } catch (JSONException e) {
            // Handle JSON parsing errors if any
            e.printStackTrace();
        }

        // Display the words in a ListView (or use RecyclerView if preferred)
        ListView listView = findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wordList);
        listView.setAdapter(adapter);

        // Set an item click listener for the ListView to handle user selection
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedWord = wordList.get(position);
            // Show the selected word in the terminal (you can replace this with any other action)
            System.out.println("Selected word: " + selectedWord);
        });
    }
}
