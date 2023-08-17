package com.example.test1;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TranslationDisplayActivity extends AppCompatActivity {

    private TextView translationTextView;
    private Button returnButton;
    private ImageView returnToFirstFragmentButton;
    private ImageView audioBtn;

    private TextView home_text;
    private String audioUrl; // Store the audio URL

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation_display);

        translationTextView = findViewById(R.id.translationTextView);
        returnButton = findViewById(R.id.returnButton);
        returnToFirstFragmentButton = findViewById(R.id.returnToFirstFragmentButton);
        audioBtn = findViewById(R.id.audioBtn); // Initialize the audio button
        home_text = findViewById(R.id.home_text);

        Intent intent = getIntent();
        String translation = intent.getStringExtra("translation");
        audioUrl = intent.getStringExtra("audio_url"); // Get the audio URL from extras

        translationTextView.setText("Translation: " + translation);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        returnToFirstFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TranslationDisplayActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        audioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio(); // Play the audio
            }
        });
    }

    private void playAudio() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(audioUrl);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
