package com.example.cpen321tutorial1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class HourAdapter extends ArrayAdapter<HourEvent> {

    final static String TAG = "Hour";

    public HourAdapter(@NonNull Context context, List<HourEvent> hourEvents) {
        super(context, 0, hourEvents);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        HourEvent event = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.hour_cell, parent, false);

        setHour(convertView, event.Time);
        setEvents(convertView, event.events);

        return convertView;
    }

    private void setHour(View convertView, LocalTime startTime) {
        TextView time = convertView.findViewById(R.id.time);
        time.setText(CalendarUtils.formattedShortTime(startTime));
    }

    private void setEvents(View convertView, ArrayList<Event> events) {
        TextView event1 = convertView.findViewById(R.id.event1);
        //TextView event2 = convertView.findViewById(R.id.event2);

        /*
        if(events.size() == 0){
            hideEvent(event1);
            hideEvent(event2);
        } else if(events.size() == 1) {
            setEvent(event1, events.get(0));
            hideEvent(event2);
        } else if(events.size() == 2) {
            setEvent(event1, events.get(0));
            setEvent(event2, events.get(1));
        } else {
            setEvent(event1, events.get(0));
            event2.setVisibility(View.VISIBLE);
            String eventsNotShown = String.valueOf(events.size()-1);
            eventsNotShown += " More Events";
            event2.setText(eventsNotShown);
        }
         */
        if(events.size() == 0){
            hideEvent(event1);
        } else if(events.size() == 1) {
            setEvent(event1, events.get(0));
        }
    }

    private void setEvent(TextView TextView, Event event) {
        TextView.setText(event.getName());
        TextView.setVisibility(View.VISIBLE);
    }

    private void hideEvent(TextView TextView) {
        TextView.setVisibility(View.INVISIBLE);
    }
}
