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
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.masterpieceshall.R;
import com.example.android.masterpieceshall.database.ArtsContract;
import com.example.android.masterpieceshall.model.ArtObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Favorites.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FavoritesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String LOG_TAG = FavoritesFragment.class.getSimpleName();
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int columnCount = 2;

    private OnListFragmentInteractionListener listener;

    FavoriteRecyclerViewAdapter favoritesAdapter;

    @BindView(R.id.favoritesList)
    RecyclerView recyclerView;
    @BindView(R.id.no_favorites)
    ConstraintLayout noFavorites;

    public static final int ID_FAVORITES_LOADER = 11;

    // Cursor column to return from the db query
    public static final String[] FAVORITES_PROJECTION = {
            ArtsContract.ArtsEntry.COLUMN_ART_OBJECT_NUMBER,
            ArtsContract.ArtsEntry.COLUMN_ART_AUTHOR,
            ArtsContract.ArtsEntry.COLUMN_ART_TITLE,
            ArtsContract.ArtsEntry.COLUMN_ART_LONG_TITLE,
            ArtsContract.ArtsEntry.COLUMN_ART_IMAGE_PATH,
            ArtsContract.ArtsEntry.COLUMN_ART_HEADER_PATH
    };

    // Cursor columns indices as defined in FAVORITES_PROJECTION (i.e. same order)
    public static final int INDEX_ART_OBJECT_NUMBER = 0;
    public static final int INDEX_ART_AUTHOR = 1;
    public static final int INDEX_ART_TITLE = 2;
    public static final int INDEX_ART_LONG_TITLE = 3;
    public static final int INDEX_ART_IMAGE_PATH = 4;
    public static final int INDEX_ART_HEADER_PATH = 5;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FavoritesFragment() {
    }

    @SuppressWarnings("unused")
    public static FavoritesFragment newInstance(int columnCount) {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            columnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites_list, container, false);

        ButterKnife.bind(this, view);

        recyclerView.setNestedScrollingEnabled(false);
        favoritesAdapter = new FavoriteRecyclerViewAdapter(getActivity(), listener);
        if (columnCount <= 1) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
        } else {
            StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(columnCount,
                    StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(sglm);
            recyclerView.setHasFixedSize(true);
        }
        recyclerView.setAdapter(favoritesAdapter);
        getActivity().getSupportLoaderManager()
                .initLoader(ID_FAVORITES_LOADER, null, this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            listener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case ID_FAVORITES_LOADER:
                Uri favoritesQueryUri = ArtsContract.ArtsEntry.CONTENT_URI;
                String[] projection = FavoritesFragment.FAVORITES_PROJECTION;
                return new CursorLoader(getActivity(),
                        favoritesQueryUri,
                        projection,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException(getActivity().getString(R.string.loader_error) + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor.getCount() == 0) {
            showNoFavoritesView();
        } else {
            hideNoFavoritesView();
            favoritesAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        favoritesAdapter.swapCursor(null);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(ArtObject item);
    }

    private void showNoFavoritesView() {
        recyclerView.setVisibility(View.GONE);
        noFavorites.setVisibility(View.VISIBLE);
    }

    private void hideNoFavoritesView() {
        noFavorites.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
