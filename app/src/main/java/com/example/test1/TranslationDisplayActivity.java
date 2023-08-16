package com.example.test1;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

public class TranslationDisplayActivity extends AppCompatActivity {

    private TextView translationTextView;
    private Button returnButton;
    private ImageView returnToFirstFragmentButton; // New button for returning to the first fragment

    private TextView home_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation_display);

        translationTextView = findViewById(R.id.translationTextView);
        returnButton = findViewById(R.id.returnButton);
        returnToFirstFragmentButton = findViewById(R.id.returnToFirstFragmentButton); // Initialize the new button
        home_text = findViewById(R.id.home_text);
        // Get the translation from the intent
        Intent intent = getIntent();
        String translation = intent.getStringExtra("translation");
        translationTextView.setText("Translation: " + translation);

        // Handle the return button click
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity to return to the previous activity
                finish();
            }
        });

        // Handle the new button (returnToFirstFragmentButton) click
        returnToFirstFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the activity that hosts the FirstFragment to return to the initial page
                Intent intent = new Intent(TranslationDisplayActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // This flag clears the activity stack
                startActivity(intent);
                // Finish the current activity if needed
                finish();
            }
        });

    }

}
