package com.example.android.masterpieceshall.ui.info;

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
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.android.masterpieceshall.R;
import com.example.android.masterpieceshall.async.EventsAsyncTask;
import com.example.android.masterpieceshall.async.EventsTaskListener;
import com.example.android.masterpieceshall.model.EventsCalendar;
import com.example.android.masterpieceshall.model.Option;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.android.masterpieceshall.utilities.StringUtils.isValidString;

/**
 * A fragment representing a list of Events.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnEventsFragmentInteractionListener}
 * interface.
 */
public class EventsFragment extends Fragment implements EventsTaskListener{
    private static final String LOG_TAG = EventsFragment.class.getSimpleName();

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_QUERY_DATE = "query-date";

    private static final String AD_CLOSED = "ad-closed";
    private int columnCount = 2;
    private String queryDate;

    private OnEventsFragmentInteractionListener listener;

    private List<Option> eventsOptions = new ArrayList<>();
    private EventsCalendar eventsCalendar;

    private EventsRecyclerViewAdapter eventsAdapter;
    private InterstitialAd interstitialAd;

    boolean adClosed = false;

    @BindView(R.id.eventsList)
    RecyclerView recyclerView;
    @BindView(R.id.no_internet_main)
    ConstraintLayout noInternet;
    @BindView(R.id.loading_pb)
    ProgressBar loadingPb;
    @BindView(R.id.no_data)
    ConstraintLayout noData;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventsFragment() {
    }

    @SuppressWarnings("unused")
    public static EventsFragment newInstance(int columnCount, String queryDate) {


        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_QUERY_DATE, queryDate);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressWarnings("unused")
    public static EventsFragment newInstance(int columnCount) {
        EventsFragment fragment = new EventsFragment();
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
            queryDate = getArguments().getString(ARG_QUERY_DATE).trim();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_list, container, false);

        ButterKnife.bind(this, view);

        recyclerView.setNestedScrollingEnabled(false);
        eventsAdapter = new EventsRecyclerViewAdapter(eventsOptions, listener);
        if (columnCount <= 1) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);

        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), columnCount);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setHasFixedSize(true);
        }
        recyclerView.setAdapter(eventsAdapter);

        interstitialAd = new InterstitialAd(getActivity());
        interstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest = new AdRequest.Builder()
                // Check the Logcat to get your device ID to test ad on real device
//                .addTestDevice("YOUR_TEST_DEVICE_ID")
                .build();

        //Load ads into Interstitial Ads
        interstitialAd.loadAd(adRequest);

        AdListener adListener = new AdListener() {
            @Override
            public void onAdClosed() {
                interstitialAd = null;
                adClosed = true;
                startEventsTask();
                if (listener != null) {
                    listener.onEventsFragmentInteraction();
                }

            }

            @Override
            public void onAdFailedToLoad(int i) {
                if (i == AdRequest.ERROR_CODE_NO_FILL) {
                    Log.d(LOG_TAG, "No ads available.");
                } else if (i == AdRequest.ERROR_CODE_INTERNAL_ERROR) {
                    Log.d(LOG_TAG, "Internet required to display ad.");
                } else {
                    Log.d(LOG_TAG, "Ad failed to load! Error code: " + i);
                }

                startEventsTask();
                if (listener != null) {
                    listener.onEventsFragmentInteraction();
                }

            }

            @Override
            public void onAdLoaded() {
                showInterstitial();
            }
        };

        if (savedInstanceState != null) {
            adClosed = savedInstanceState.getBoolean(AD_CLOSED);
            if (adClosed) {
                startEventsTask();
            } else {
                if (interstitialAd != null) {
                    interstitialAd.setAdListener(adListener);
                } else {
                    startEventsTask();
                }
            }
        } else {
            interstitialAd.setAdListener(adListener);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEventsFragmentInteractionListener) {
            listener = (OnEventsFragmentInteractionListener) context;
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(AD_CLOSED, adClosed);
    }

    @Override
    public void onEventsTaskPrepared() {
        showLoadingView();
    }

    @Override
    public void onEventsTaskExecuted(EventsCalendar eventsCalendar) {
        this.eventsCalendar = eventsCalendar;
        if (eventsCalendar != null) {
            hideLoadingView();

            eventsOptions = eventsCalendar.getOptions();
            if (eventsOptions != null && eventsOptions.size() > 0) {

                hideNoDataView();

                List<Option> filteredOptionList = filterEventOptions(eventsOptions);
                eventsAdapter.setEventOptions(filteredOptionList);
                recyclerView.setAdapter(eventsAdapter);

            } else {

                showNoDataView();
            }
            List<Option> filteredOptionList = filterEventOptions(eventsOptions);
            eventsAdapter.setEventOptions(filteredOptionList);
            recyclerView.setAdapter(eventsAdapter);
        } else {
            showNoInternetView();
        }
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
    public interface OnEventsFragmentInteractionListener {
        void onEventsFragmentInteraction(Option option);
        void onEventsFragmentInteraction();
    }

    private void startEventsTask() {
        EventsAsyncTask eventsAsyncTask = new EventsAsyncTask(EventsFragment.this);
        eventsAsyncTask.execute(queryDate);
    }

    @OnClick(R.id.retryButton)
    public void reloadData() {
        noInternet.setVisibility(View.GONE);
        startEventsTask();
    }

    // Helper method filters out the duplicate events by comparing their name
    private List<Option> filterEventOptions(List<Option> optionList) {
        if (optionList != null && optionList.size() > 0) {
            List<Option> filteredOptionList = new ArrayList<>();

            String firstEventName = optionList.get(0).getExposition()
                    .getName().toLowerCase().trim();
            if (isValidString(firstEventName)) {
                // Add first element in the filtered list
                filteredOptionList.add(optionList.get(0));
            }
            Set<String> uniqueNamesSet = new HashSet<>();
            // Add first event name in the unique set
            uniqueNamesSet.add(firstEventName);
            if (optionList.size() >= 1) {
                for(int i = 1; i < optionList.size()-1; i++) {
                    String nextEventName = optionList.get(i).getExposition()
                            .getName().toLowerCase().trim();
                    if (isValidString(nextEventName)) {
                        if(uniqueNamesSet.add(nextEventName)) {
                            filteredOptionList.add(optionList.get(i));
                        }
                    }
                }
            }
            return filteredOptionList;
        }
        return null;
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

    private void showInterstitial() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
            adClosed = true;
            interstitialAd = null;
        } else {
            startEventsTask();
            interstitialAd = null;
        }
    }
}
