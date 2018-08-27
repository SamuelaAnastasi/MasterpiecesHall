package com.example.android.masterpieceshall.ui.info;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.android.masterpieceshall.R;
import com.example.android.masterpieceshall.model.Option;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.masterpieceshall.utilities.Constants.EVENTS_AGENDA_URL;
import static com.example.android.masterpieceshall.utilities.StringUtils.isValidString;
import static com.example.android.masterpieceshall.utilities.UiUtils.showToast;
import static com.example.android.masterpieceshall.utilities.Utils.isConnected;
import static com.example.android.masterpieceshall.utilities.Utils.isPhoneLand;
import static com.example.android.masterpieceshall.utilities.Utils.isTabletLand;
import static com.example.android.masterpieceshall.utilities.Utils.isTabletPortrait;

public class EventsActivity extends AppCompatActivity implements
        EventsFragment.OnEventsFragmentInteractionListener {

    public static final String LOG_TAG = EventsActivity.class.getSimpleName();

    public static final String EVENTS_NESTED_SCROLL_POSITION = "eventsNestedScrollPosition";
    public static final String NEXT_DATE_STRING = "nextDateString";

    int columnCount = 1;
    String eventDateString;
    int[] scrollPosition;

    ActionBar actionBar;
    @BindView(R.id.eventsToolbar)
    Toolbar toolbar;
    @BindView(R.id.eventsNsv)
    NestedScrollView eventsNsv;
    @BindString(R.string.events_title)
    String eventsTitle;
    @BindString(R.string.toastBrowserMissingError)
    String toastBrowserMissingMessage;
    @BindString(R.string.toastNoInternetError)
    String toastNoInternetMessage;
    @BindColor(R.color.colorAccentFavorite)
    int toastBackgroundColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle(eventsTitle);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (isPhoneLand(this) || isTabletPortrait(this)) {
            columnCount = 2;
        } else if (isTabletLand(this)) {
            columnCount = 4;
        } else {
            columnCount = 1;
        }
        Intent intent = getIntent();
        eventDateString = intent.getStringExtra(NEXT_DATE_STRING);
        FragmentManager manager = getSupportFragmentManager();

        EventsFragment eventsFragment = EventsFragment.newInstance(columnCount, eventDateString);
        manager.beginTransaction()
                .replace(R.id.eventsFragmentFrameLayout, eventsFragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        scrollPosition = new int[]{ eventsNsv.getScrollX(), eventsNsv.getScrollY()};
        outState.putIntArray(EVENTS_NESTED_SCROLL_POSITION, scrollPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null) {
            scrollPosition = savedInstanceState.getIntArray(EVENTS_NESTED_SCROLL_POSITION);
        }
    }

    @Override
    public void onEventsFragmentInteraction(Option option) {
        String eventUrl = EVENTS_AGENDA_URL;
        if (isValidString(eventUrl)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(eventUrl));
            PackageManager pm = getPackageManager();
            if (isConnected(this)) {
                if(intent.resolveActivity(pm) != null) {
                    this.startActivity(intent);
                } else {
                    showToast(this, toastBrowserMissingMessage, toastBackgroundColor);
                }
            } else {
                showToast(this, toastNoInternetMessage, toastBackgroundColor);
            }
        }
    }

    @Override
    public void onEventsFragmentInteraction() {
        if(scrollPosition != null) {
            eventsNsv.postDelayed(new Runnable() {
                @Override
                public void run() {
                    eventsNsv.smoothScrollTo(scrollPosition[0], scrollPosition[1]);
                }
            }, 600);
        }
    }
}
