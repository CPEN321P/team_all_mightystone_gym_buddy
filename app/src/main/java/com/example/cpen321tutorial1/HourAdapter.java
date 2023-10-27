package com.example.cpen321tutorial1;

import android.content.Context;
import android.util.Log;
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

        setHour(convertView, event.StartTime);
        setEvents(convertView, event.events);

        return convertView;
    }

    private void setHour(View convertView, LocalTime startTime) {
        TextView time = convertView.findViewById(R.id.time);
        time.setText(CalendarUtils.formattedShortTime(startTime));
    }

    private void setEvents(View convertView, ArrayList<Event> events) {
        TextView event1 = convertView.findViewById(R.id.event1);
        TextView event2 = convertView.findViewById(R.id.event2);
        TextView event3 = convertView.findViewById(R.id.event3);
        TextView event4 = convertView.findViewById(R.id.event4);
        TextView event5 = convertView.findViewById(R.id.event5);

        if(events.size() == 0){
            hideEvent(event1);
            hideEvent(event2);
            hideEvent(event3);
            hideEvent(event4);
            hideEvent(event5);
        } else if(events.size() == 1) {
            setEvent(event1, events.get(0));
            hideEvent(event2);
            hideEvent(event3);
            hideEvent(event4);
            hideEvent(event5);
        } else if(events.size() == 2) {
            setEvent(event1, events.get(0));
            setEvent(event2, events.get(1));
            hideEvent(event3);
            hideEvent(event4);
            hideEvent(event5);
        } else if(events.size() == 3) {
            setEvent(event1, events.get(0));
            setEvent(event2, events.get(1));
            setEvent(event3, events.get(2));
            hideEvent(event4);
            hideEvent(event5);
        } else if(events.size() == 4) {
            setEvent(event1, events.get(0));
            setEvent(event2, events.get(1));
            setEvent(event3, events.get(2));
            setEvent(event4, events.get(3));
            hideEvent(event5);
        } else if(events.size() == 5) {
            setEvent(event1, events.get(0));
            setEvent(event2, events.get(1));
            setEvent(event3, events.get(2));
            setEvent(event4, events.get(3));
            setEvent(event5, events.get(4));
        } else {
            setEvent(event1, events.get(0));
            setEvent(event2, events.get(1));
            setEvent(event3, events.get(2));
            setEvent(event4, events.get(3));
            event5.setVisibility(View.VISIBLE);
            String eventsNotShown = String.valueOf(events.size()-4);
            eventsNotShown += " More Events";
            event5.setText(eventsNotShown);
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
