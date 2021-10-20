package com.example.habtrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MydayAdapter extends ArrayAdapter<Habit> {
    private ArrayList<Habit> habits;
    private Context context;
    private OnItemClickListener onClickListener;

    public MydayAdapter(Context context, ArrayList<Habit> habits, OnItemClickListener onClickListener) {
        super(context, 0, habits);
        this.habits = habits;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_myday, parent, false);
        }

        Habit habit = habits.get(position);

        CheckBox check = view.findViewById(R.id.myday_checkBox);

        TextView title = view.findViewById(R.id.myday_title_text);
        title.setText(habit.getTitle());

        ProgressBar simpleProgressBar= (ProgressBar) view.findViewById(R.id.myday_progressBar);
        simpleProgressBar.setMax(100); // 100 maximum value for the progress value
        simpleProgressBar.setProgress(habit.getProgress());

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null)
                    onClickListener.onItemClicked(position);
            }
        });

        return view;

    }
}
