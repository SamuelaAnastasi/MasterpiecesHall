package com.example.android.masterpieceshall.ui.info;

/*
 * This Capstone project is part of Android Developer Nanodegree Scholarship Program by
 * Udacity and Google
 *
 * The project is licensed under the MIT License(https://opensource.org/licenses/MIT)
 *
 * Copyright (c) 2018 - Samuela Anastasi
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.masterpieceshall.R;
import com.example.android.masterpieceshall.model.Option;
import com.example.android.masterpieceshall.ui.info.EventsFragment.OnEventsFragmentInteractionListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.masterpieceshall.utilities.StringUtils.getDateSubstring;
import static com.example.android.masterpieceshall.utilities.StringUtils.removeNewLines;

/**
 * {@link RecyclerView.Adapter} that can display a  and makes a call to the
 * specified {@link OnEventsFragmentInteractionListener}.
 *
 */
public class EventsRecyclerViewAdapter extends
        RecyclerView.Adapter<EventsRecyclerViewAdapter.ViewHolder> {

    private List<Option> eventOptions;
    private final OnEventsFragmentInteractionListener listener;

    EventsRecyclerViewAdapter(List<Option> eventOptions,
                              OnEventsFragmentInteractionListener listener) {
        this.eventOptions = eventOptions;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_events, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.eventOption = eventOptions.get(position);
        holder.eventTitleTv.setText(eventOptions.get(position).getExposition().getName());
        String dateString = eventOptions.get(position).getPeriod().getStartDate();
        holder.eventDateTv.setText(getDateSubstring(dateString));
        holder.eventHourTv.setText(eventOptions.get(position).getPeriod().getText());
        String eventDescription = eventOptions.get(position).getExposition().getDescription();
        holder.eventDescriptionTv.setText(removeNewLines(eventDescription));

        holder.learnMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    listener.onEventsFragmentInteraction(holder.eventOption);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(eventOptions != null) {
            return eventOptions.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View rootView;

        @BindView(R.id.eventTitleTv)
        TextView eventTitleTv;
        @BindView(R.id.eventDateTv)
        TextView eventDateTv;
        @BindView(R.id.eventHourTv)
        TextView eventHourTv;
        @BindView(R.id.eventDescriptionTv)
        TextView eventDescriptionTv;
        @BindView(R.id.learnMoreButton)
        Button learnMoreButton;

        Option eventOption;

        ViewHolder(View view) {
            super(view);
            rootView = view;
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + eventDateTv.getText() + "'";
        }
    }

    void setEventOptions(List<Option> eventOptions) {
        this.eventOptions = eventOptions;
    }
}
