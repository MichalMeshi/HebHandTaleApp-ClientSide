package com.example.test1;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.test1.R;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<String> {
    public CustomListAdapter(Context context, ArrayList<String> data) {
        super(context, R.layout.list_item_word, data);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.list_item_word, parent, false);
        }

        TextView wordTextView = view.findViewById(R.id.wordText);
        wordTextView.setText(getItem(position));

        return view;
    }
}

