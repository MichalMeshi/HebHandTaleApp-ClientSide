package com.example.test1;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WordListActivity extends AppCompatActivity {
    private String selectedWord = "";
    private TextView wordTextView;
    private ListView listView;
    private ArrayList<String> wordList;

    private ImageView returnToFirstFragmentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            int barColor = ContextCompat.getColor(this, R.color.barColor);
            actionBar.setBackgroundDrawable(new ColorDrawable(barColor));
        }
        initViews();
        processJsonResponse();
        setupWordTextView();
        setupListView();
    }

    private void initViews() {
        wordTextView = findViewById(R.id.wordTextView);
        listView = findViewById(R.id.listView);
        returnToFirstFragmentButton = findViewById(R.id.returnToFirstFragmentButtonTop);
    }

    private void processJsonResponse() {
        String decodedResponse = getIntent().getStringExtra("wordList");
        wordList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(decodedResponse);
            JSONArray wordsArray = jsonObject.getJSONArray("words");

            for (int i = 0; i < wordsArray.length(); i++) {
                String word = wordsArray.getString(i);
                String cleanedWord = word.trim().replaceAll("[\"\\[\\]{}]", "");

                if (!cleanedWord.isEmpty()) {
                    wordList.add(cleanedWord);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupWordTextView() {
        wordTextView.setText("The recognized word: " + wordList.get(0));

        wordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleWordClick(wordList.get(0));
            }
        });
        returnToFirstFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the activity that hosts the FirstFragment to return to the initial page
                Intent intent = new Intent(WordListActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // This flag clears the activity stack
                startActivity(intent);
                // Finish the current activity if needed
                finish();
            }
        });

    }

    private void setupListView() {
        ArrayList<String> restOfWordList = new ArrayList<>(wordList.subList(1, wordList.size()));
        CustomListAdapter adapter = new CustomListAdapter(this, restOfWordList);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener((parent, view, position, id) -> {
            handleWordClick(restOfWordList.get(position));
        });
    }

    private void handleWordClick(String word) {
        selectedWord = word;
        System.out.println("Selected word: " + selectedWord);
        SendWordToSaveInDB.sendWordToServer(selectedWord);

        Intent intent = new Intent(WordListActivity.this, WordDetailsActivity.class);
        intent.putExtra("selectedWord", selectedWord);
        startActivity(intent);
    }
}
