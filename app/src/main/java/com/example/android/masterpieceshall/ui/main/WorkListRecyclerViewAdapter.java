package com.example.android.masterpieceshall.ui.main;

/*
 * This Capstone project is part of Android Developer Nanodegree Scholarship Program by
 * Udacity and Google
 *
 * The project is licensed under the MIT License(https://opensource.org/licenses/MIT)
 *
 * Copyright (c) 2018 - Samuela Anastasi
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.masterpieceshall.R;
import com.example.android.masterpieceshall.model.ArtObject;
import com.example.android.masterpieceshall.ui.main.WorkFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.masterpieceshall.utilities.StringUtils.getPeriodFromString;
import static com.example.android.masterpieceshall.utilities.UiUtils.loadImage;

/**
 * {@link RecyclerView.Adapter} that can display a art object and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class WorkListRecyclerViewAdapter extends
        RecyclerView.Adapter<WorkListRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<ArtObject> artObjects;
    private final OnListFragmentInteractionListener mListener;

    WorkListRecyclerViewAdapter(Context context, OnListFragmentInteractionListener listener) {
        mListener = listener;
        this.context = context;
        artObjects = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_work, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.work = artObjects.get(position);
        holder.authorView.setText(artObjects.get(position).getPrincipalOrFirstMaker());
        holder.titleView.setText(artObjects.get(position).getTitle());
        String longTitle = artObjects.get(position).getLongTitle();
        String workYear = getPeriodFromString(longTitle);
        holder.workYearView.setText(workYear);
        String imageUrl = artObjects.get(position).getWebImage().getUrl();

        loadImage(imageUrl, holder.imageView);

        holder.holderRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.work);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return artObjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View holderRoot;

        @BindView(R.id.workImageView)
        public ImageView imageView;
        @BindView(R.id.authorTextView)
        TextView authorView;
        @BindView(R.id.titleTextView)
        TextView titleView;
        @BindView(R.id.workYeartextView)
        TextView workYearView;

        ArtObject work;

        ViewHolder(View view) {
            super(view);
            holderRoot = view;

            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + titleView.getText() + "'";
        }
    }

    public List<ArtObject> getArtObjects() {
        return artObjects;
    }

    void setArtObjects(List<ArtObject> artObjects) {
        this.artObjects = artObjects;
    }
}
