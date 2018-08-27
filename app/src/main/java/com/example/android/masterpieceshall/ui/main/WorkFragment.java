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
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.android.masterpieceshall.R;
import com.example.android.masterpieceshall.api.ApiClientGenerator;
import com.example.android.masterpieceshall.api.ApiInterface;
import com.example.android.masterpieceshall.model.ArtObject;
import com.example.android.masterpieceshall.model.WorkData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.android.masterpieceshall.BuildConfig.API_KEY;
import static com.example.android.masterpieceshall.utilities.Constants.ART_TYPE_PAINTING;
import static com.example.android.masterpieceshall.utilities.Constants.FORMAT_JSON;
import static com.example.android.masterpieceshall.utilities.Constants.IMG_ONLY_TRUE;
import static com.example.android.masterpieceshall.utilities.Constants.KEY_API_KEY;
import static com.example.android.masterpieceshall.utilities.Constants.KEY_ART_TYPE;
import static com.example.android.masterpieceshall.utilities.Constants.KEY_FORMAT;
import static com.example.android.masterpieceshall.utilities.Constants.KEY_IMG_ONLY;
import static com.example.android.masterpieceshall.utilities.Constants.KEY_OBJECTS_NUMBER;
import static com.example.android.masterpieceshall.utilities.Constants.KEY_PAGE_NUMBER;
import static com.example.android.masterpieceshall.utilities.Constants.OBJECTS_NUMBER_VALUE;
import static com.example.android.masterpieceshall.utilities.Constants.PAGE_NUMBER_VALUE;

/**
 * A fragment representing a list of ArtObjects.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class WorkFragment extends Fragment {

    private static final String LOG_TAG = WorkFragment.class.getSimpleName();

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_ART_TYPE = "art-type";
    private int columnCount = 2;
    private String artType = ART_TYPE_PAINTING;

    private WorkData workData;
    private ArrayList<ArtObject> worksList = new ArrayList<>();
    private WorkListRecyclerViewAdapter viewAdapter;

    @BindView(R.id.worksList)
    RecyclerView recyclerView;
    @BindView(R.id.loading_pb)
    ProgressBar loadingPb;
    @BindView(R.id.no_internet_main)
    ConstraintLayout noInternet;
    @BindView(R.id.no_data)
    ConstraintLayout noData;

    private OnListFragmentInteractionListener listener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WorkFragment() {
    }

    @SuppressWarnings("unused")
    public static WorkFragment newInstance(int columnCount, String artType) {
        WorkFragment fragment = new WorkFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_ART_TYPE, artType);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressWarnings("unused")
    public static WorkFragment newInstance(int columnCount) {
        WorkFragment fragment = new WorkFragment();
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
            artType = getArguments().getString(ARG_ART_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_list, container, false);

        ButterKnife.bind(this, view);

        recyclerView.setNestedScrollingEnabled(false);
        viewAdapter = new WorkListRecyclerViewAdapter(getActivity(), listener);

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
        recyclerView.setAdapter(viewAdapter);
        fetchArtObjects(artType);
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

    private Map<String, String> getQueryMap(String artType) {

        Map<String, String> params = new HashMap<>();

        params.put(KEY_API_KEY, API_KEY);
        params.put(KEY_FORMAT, FORMAT_JSON);
        params.put(KEY_IMG_ONLY, IMG_ONLY_TRUE);
        params.put(KEY_PAGE_NUMBER, PAGE_NUMBER_VALUE);
        params.put(KEY_OBJECTS_NUMBER, OBJECTS_NUMBER_VALUE);
        params.put(KEY_ART_TYPE, artType);

        return params;
    }

    private void fetchArtObjects(String artType) {
        showLoadingView();

        Map<String, String> params = getQueryMap(artType);
        ApiInterface apiInterface = ApiClientGenerator.getClient(getActivity()).create(ApiInterface.class);
        Call<WorkData> call = apiInterface.getWorkData(params);
        call.enqueue(new Callback<WorkData>() {
            @Override
            public void onResponse(Call<WorkData> call, Response<WorkData> response) {
                if (response.isSuccessful()) {
                    workData = null;
                    workData = response.body();
                    if (workData != null) {

                        hideLoadingView();

                        worksList = (ArrayList<ArtObject>) workData.getArtObjects();
                        if (worksList != null && worksList.size() > 0) {

                            hideNoDataView();


                            viewAdapter.setArtObjects(worksList);
                            recyclerView.setAdapter(viewAdapter);
                        }
                        else {
                            showNoDataView();
                        }
                    }
                }
                else {
                    hideLoadingView();
                    showNoInternetView();
                }
            }

            @Override
            public void onFailure(Call<WorkData> call, Throwable t) {
                if (t instanceof IOException) {
                    hideLoadingView();
                    showNoInternetView();
                } else {
                    Log.d(LOG_TAG, " Conversion error: ", t);
                }
            }
        });
    }

    @OnClick(R.id.retryButton)
    public void reloadData() {
        recyclerView.setVisibility(View.VISIBLE);
        noInternet.setVisibility(View.GONE);
        fetchArtObjects(artType);
    }

    // Helper methods to handle view visibility before and after fetching data
    private void showLoadingView() {
        loadingPb.setVisibility(View.VISIBLE);
        noInternet.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
    }

    private void hideLoadingView () {
        loadingPb.setVisibility(View.GONE);
        noInternet.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
    }

    private void showNoDataView() {
        recyclerView.setVisibility(View.GONE);
        noData.setVisibility(View.VISIBLE);
    }

    private void hideNoDataView() {
        noData.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showNoInternetView() {
        loadingPb.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        noInternet.setVisibility(View.VISIBLE);
        Log.d(LOG_TAG, " No internet connection");
    }
}
