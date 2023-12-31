package com.example.test1;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordDetailsActivity extends AppCompatActivity {

    private MaterialAutoCompleteTextView languageSpinner;
    private TextView wordTextView;
    private Button translateButton;
    private TextView translationTextView;
    private ImageView returnToFirstFragmentButton;

    private Map<String, String> languageMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_details);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            int barColor = ContextCompat.getColor(this, R.color.barColor);
            actionBar.setBackgroundDrawable(new ColorDrawable(barColor));
        }
        languageSpinner = findViewById(R.id.languageSpinner);
        wordTextView = findViewById(R.id.wordTextView);
        translateButton = findViewById(R.id.translateButton);
        returnToFirstFragmentButton = findViewById(R.id.returnToFirstFragmentButton);
        initializeLanguageMap();

        // Get the selected word from the intent
        Intent intent = getIntent();
        String selectedWord = intent.getStringExtra("selectedWord");
        wordTextView.setText("Text: " + selectedWord);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item, new ArrayList<>(languageMap.keySet()));

// Specify the layout to use for the dropdown items
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the spinner
        languageSpinner.setAdapter(adapter);

// Handle spinner item selection
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedLanguage = parentView.getItemAtPosition(position).toString();
                String languageCode = languageMap.get(selectedLanguage);
                displayLanguageCode(languageCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
        // Inside your translateButton OnClickListener
        // Inside your translateButton OnClickListener
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected language and the word from the UI
                String selectedLanguage = languageSpinner.getText().toString();
                String languageCode = languageMap.get(selectedLanguage);
                String selectedWord = wordTextView.getText().toString().replace("Text: ", "");

                // Check if the user is signed in
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(WordDetailsActivity.this);
                // Call the translateWord method with the user's account
                TranslateAPI translateAPI = new TranslateAPI();
                translateAPI.translateWord(selectedWord, languageCode, selectedLanguage, acct, new TranslateAPI.TranslationCallback() {
                    @Override
                    public void onTranslationSuccess(String translation,String audioUrl) {
                        Intent translationIntent = new Intent(WordDetailsActivity.this, TranslationDisplayActivity.class);
                        translationIntent.putExtra("translation", translation);
                        translationIntent.putExtra("audio_url", audioUrl); // Add the audio URL to the intent extras
                        startActivity(translationIntent);
                    }
                    @Override
                    public void onTranslationFailure(String errorMessage) {
                        // Handle the translation failure
                        System.out.println("Translation failed: " + errorMessage);
                    }
                });
            }
        });
        returnToFirstFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WordDetailsActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }

    private void initializeLanguageMap() {
        // Initialize the language map with language names as keys and language codes as values
        languageMap = new HashMap<>();
        languageMap.put("Afrikaans", "af");
        languageMap.put("Albanian", "sq");
        languageMap.put("Amharic", "am");
        languageMap.put("Arabic", "ar");
        languageMap.put("Armenian", "hy");
        languageMap.put("Azerbaijani", "az");
        languageMap.put("Basque", "eu");
        languageMap.put("Belarusian", "be");
        languageMap.put("Bengali", "bn");
        languageMap.put("Bosnian", "bs");
        languageMap.put("Bulgarian", "bg");
        languageMap.put("Catalan", "ca");
        languageMap.put("Cebuano", "ceb");
        languageMap.put("Chinese (Simplified)", "zh-CN");
        languageMap.put("Chinese (Traditional)", "zh-TW");
        languageMap.put("Corsican", "co");
        languageMap.put("Croatian", "hr");
        languageMap.put("Czech", "cs");
        languageMap.put("Danish", "da");
        languageMap.put("Dutch", "nl");
        languageMap.put("English", "en");
        languageMap.put("Esperanto", "eo");
        languageMap.put("Estonian", "et");
        languageMap.put("Finnish", "fi");
        languageMap.put("French", "fr");
        languageMap.put("Frisian", "fy");
        languageMap.put("Galician", "gl");
        languageMap.put("Georgian", "ka");
        languageMap.put("German", "de");
        languageMap.put("Greek", "el");
        languageMap.put("Gujarati", "gu");
        languageMap.put("Haitian Creole", "ht");
        languageMap.put("Hausa", "ha");
        languageMap.put("Hawaiian", "haw");
        languageMap.put("Hebrew", "he");
        languageMap.put("Hindi", "hi");
        languageMap.put("Hmong", "hmn");
        languageMap.put("Hungarian", "hu");
        languageMap.put("Icelandic", "is");
        languageMap.put("Igbo", "ig");
        languageMap.put("Indonesian", "id");
        languageMap.put("Irish", "ga");
        languageMap.put("Italian", "it");
        languageMap.put("Japanese", "ja");
        languageMap.put("Javanese", "jw");
        languageMap.put("Kannada", "kn");
        languageMap.put("Kazakh", "kk");
        languageMap.put("Khmer", "km");
        languageMap.put("Korean", "ko");
        languageMap.put("Kurdish", "ku");
        languageMap.put("Kyrgyz", "ky");
        languageMap.put("Lao", "lo");
        languageMap.put("Latin", "la");
        languageMap.put("Latvian", "lv");
        languageMap.put("Lithuanian", "lt");
        languageMap.put("Luxembourgish", "lb");
        languageMap.put("Macedonian", "mk");
        languageMap.put("Malagasy", "mg");
        languageMap.put("Malay", "ms");
        languageMap.put("Malayalam", "ml");
        languageMap.put("Maltese", "mt");
        languageMap.put("Maori", "mi");
        languageMap.put("Marathi", "mr");
        languageMap.put("Mongolian", "mn");
        languageMap.put("Myanmar (Burmese)", "my");
        languageMap.put("Nepali", "ne");
        languageMap.put("Norwegian", "no");
        languageMap.put("Nyanja (Chichewa)", "ny");
        languageMap.put("Pashto", "ps");
        languageMap.put("Persian", "fa");
        languageMap.put("Polish", "pl");
        languageMap.put("Portuguese", "pt");
        languageMap.put("Punjabi", "pa");
        languageMap.put("Romanian", "ro");
        languageMap.put("Russian", "ru");
        languageMap.put("Samoan", "sm");
        languageMap.put("Scots Gaelic", "gd");
        languageMap.put("Serbian", "sr");
        languageMap.put("Sesotho", "st");
        languageMap.put("Shona", "sn");
        languageMap.put("Sindhi", "sd");
        languageMap.put("Sinhala", "si");
        languageMap.put("Slovak", "sk");
        languageMap.put("Slovenian", "sl");
        languageMap.put("Somali", "so");
        languageMap.put("Spanish", "es");
        languageMap.put("Sundanese", "su");
        languageMap.put("Swahili", "sw");
        languageMap.put("Swedish", "sv");
        languageMap.put("Tagalog", "tl");
        languageMap.put("Tajik", "tg");
        languageMap.put("Tamil", "ta");
        languageMap.put("Telugu", "te");
        languageMap.put("Thai", "th");
        languageMap.put("Turkish", "tr");
        languageMap.put("Ukrainian", "uk");
        languageMap.put("Urdu", "ur");
        languageMap.put("Uzbek", "uz");
        languageMap.put("Vietnamese", "vi");
        languageMap.put("Welsh", "cy");
        languageMap.put("Xhosa", "xh");
        languageMap.put("Yiddish", "yi");
        languageMap.put("Yoruba", "yo");
        languageMap.put("Zulu", "zu"); }

    private void displayLanguageCode(String code) {
        // Display the language code
        System.out.println(code);

    }

}