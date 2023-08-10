package com.example.test1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

// ... Your existing imports and code ...

// ... Your existing imports and code ...

public class WordListActivity extends AppCompatActivity {
    String selectedWord="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        // Get the decodedResponse from the intent's extras
        String decodedResponse = getIntent().getStringExtra("wordList");
        ArrayList<String> wordList = new ArrayList<>();

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

        // Display the words in a ListView
        ListView listView = findViewById(R.id.listView);

        // Set the message and the first word in the wordTextView
        TextView wordTextView = findViewById(R.id.wordTextView);
        wordTextView.setText("The recognized word: " + wordList.get(0)); // Set the message and first word

        // Handle click event for the wordTextView
        wordTextView.setOnClickListener(view -> {
            selectedWord = wordList.get(0); // First word from the list
            // Show the selected word in the terminal or a message
            System.out.println("Selected word: " + selectedWord);

            Intent intent = new Intent(WordListActivity.this, WordDetailsActivity.class);
            intent.putExtra("selectedWord", selectedWord);
            startActivity(intent);
            // TranslateAPI t = new TranslateAPI();
            // t.translateWord(selectedWord);
        });

        // Exclude the first word from the rest of the list
        ArrayList<String> restOfWordList = new ArrayList<>(wordList.subList(1, wordList.size()));

        // Create an adapter for the rest of the words
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, restOfWordList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            selectedWord = restOfWordList.get(position); // Adjust position for the word list
            // Show the selected word in the terminal (you can replace this with any other action)
            System.out.println("Selected word: " + selectedWord);

            Intent intent = new Intent(WordListActivity.this, WordDetailsActivity.class);
            intent.putExtra("selectedWord", selectedWord);
            startActivity(intent);
//             TranslateAPI t = new TranslateAPI();
//            try {
//                t.translateWord(selectedWord);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
        });
    }
}
