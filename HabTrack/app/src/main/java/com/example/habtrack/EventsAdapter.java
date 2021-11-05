package com.example.habtrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class EventsAdapter extends ArrayAdapter<HabitEvents> {

    private ArrayList<HabitEvents> events;
    private Context context;

    public EventsAdapter(Context context, ArrayList<HabitEvents> events) {
        super(context, 0, events);
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        View view = convertView;
        if(view == null) view = LayoutInflater.from(context).inflate(R.layout.content, parent, false);

        // TODO: Need to get HabitEvent Object
        HabitEvents hEvents = events.get(position);

        TextView habitEventTitle = view.findViewById(R.id.eventTitle);
        TextView eventDate = view.findViewById(R.id.eventDate);

        habitEventTitle.setText(hEvents.getTitle() + " Completed");
        eventDate.setText("on "+hEvents.getTimeStamp());

        return view;
    }


}
