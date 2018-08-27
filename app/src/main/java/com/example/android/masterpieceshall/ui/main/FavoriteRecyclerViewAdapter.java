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
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.masterpieceshall.R;
import com.example.android.masterpieceshall.model.ArtObject;
import com.example.android.masterpieceshall.model.HeaderImage;
import com.example.android.masterpieceshall.model.WebImage;
import com.example.android.masterpieceshall.ui.main.FavoritesFragment.OnListFragmentInteractionListener;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.masterpieceshall.utilities.UiUtils.loadImage;

/**
 * {@link RecyclerView.Adapter} that can display a ArtObject and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class FavoriteRecyclerViewAdapter extends
        RecyclerView.Adapter<FavoriteRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private Cursor cursor;

    private final OnListFragmentInteractionListener listener;

    FavoriteRecyclerViewAdapter(Context context, OnListFragmentInteractionListener listener) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_favorites, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        final ArtObject artObject = holder.getCurrentArtObject(cursor);
        holder.longTitleTv.setText(artObject.getLongTitle());
        String imageUrl = artObject.getWebImage().getUrl();

        loadImage(imageUrl, holder.headerIv);

        holder.holderRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    listener.onListFragmentInteraction(artObject);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == cursor) {
            return 0;
        }

        return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final View holderRoot;

        @BindView(R.id.headerIv)
        ImageView headerIv;
        @BindView(R.id.longTitleTv)
        TextView longTitleTv;

        ViewHolder(View view) {
            super(view);
            holderRoot = view;

            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + longTitleTv.getText() + "'";
        }

        private ArtObject getCurrentArtObject(Cursor cursor) {
            ArtObject artObject = new ArtObject();
            artObject.setObjectNumber(cursor.getString(FavoritesFragment.INDEX_ART_OBJECT_NUMBER));
            artObject.setPrincipalOrFirstMaker(cursor.getString(FavoritesFragment.INDEX_ART_AUTHOR));
            artObject.setTitle(cursor.getString(FavoritesFragment.INDEX_ART_TITLE));
            artObject.setLongTitle(cursor.getString(FavoritesFragment.INDEX_ART_LONG_TITLE));

            WebImage currentWebImage = new WebImage();
            currentWebImage.setUrl(cursor.getString(FavoritesFragment.INDEX_ART_IMAGE_PATH));
            artObject.setWebImage(currentWebImage);

            HeaderImage currentHeaderImage = new HeaderImage();
            currentHeaderImage.setUrl(cursor.getString(FavoritesFragment.INDEX_ART_HEADER_PATH));
            artObject.setHeaderImage(currentHeaderImage);

            return artObject;
        }
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
