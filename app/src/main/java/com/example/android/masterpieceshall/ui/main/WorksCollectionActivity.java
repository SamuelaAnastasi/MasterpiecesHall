package com.example.android.masterpieceshall.ui.main;

/*
 * This Capstone project is part of Android Developer Nanodegree Scholarship Program by
 * Udacity and Google
 *
 * The project is licensed under the MIT License(https://opensource.org/licenses/MIT)
 *
 * Copyright (c) 2018 - Samuela Anastasi
 */

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.example.android.masterpieceshall.R;
import com.example.android.masterpieceshall.model.ArtObject;
import com.example.android.masterpieceshall.ui.info.AppInfoActivity;
import com.example.android.masterpieceshall.ui.info.EventsActivity;
import com.example.android.masterpieceshall.ui.info.WorkDetailsActivity;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.android.masterpieceshall.ui.info.EventsActivity.NEXT_DATE_STRING;
import static com.example.android.masterpieceshall.utilities.Constants.ART_TYPE_FIGURE;
import static com.example.android.masterpieceshall.utilities.Constants.ART_TYPE_PAINTING;
import static com.example.android.masterpieceshall.utilities.Constants.DAY_IN_MILLIS;
import static com.example.android.masterpieceshall.utilities.PrefUtils.getLastArtType;
import static com.example.android.masterpieceshall.utilities.PrefUtils.getLastNavOption;
import static com.example.android.masterpieceshall.utilities.PrefUtils.getLastToolbarTitle;
import static com.example.android.masterpieceshall.utilities.PrefUtils.setLastArtType;
import static com.example.android.masterpieceshall.utilities.PrefUtils.setLastNavOption;
import static com.example.android.masterpieceshall.utilities.PrefUtils.setLastToolbarTitle;
import static com.example.android.masterpieceshall.utilities.Utils.isPhoneLand;
import static com.example.android.masterpieceshall.utilities.Utils.isTabletLand;
import static com.example.android.masterpieceshall.utilities.Utils.isTabletPortrait;

public class WorksCollectionActivity extends AppCompatActivity implements
        WorkFragment.OnListFragmentInteractionListener,
        FavoritesFragment.OnListFragmentInteractionListener {

    private static final String LOG_TAG = WorksCollectionActivity.class.getSimpleName();

    public static final String PREF_OPTION_PAINTINGS = "Paintings";
    public static final String PREF_OPTION_FAVORITES = "Favorites";
    public static final String PREF_OPTION_FIGURE = "Figure";
    public static final String NESTED_SCROLL_POSITION = "nestedScrollPosition";

    int columnCount = 1;

    private ActionBar actionBar;
    DatePickerDialog pickerDialog;
    String eventDateString;
    String artType = ART_TYPE_PAINTING;

    // Bind views
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bnv_main_navigation)
    BottomNavigationView navigationView;
    @BindView(R.id.artsNsv)
    NestedScrollView nsv;

    // Bind Resources
    @BindString(R.string.toastBrowserMissingError)
    String toastBrowserMissingMessage;
    @BindString(R.string.toastNoInternetError)
    String toastNoInternetMessage;
    @BindString(R.string.pref_option_paintings)
    String prefOptionPaintings;
    @BindString(R.string.pref_title_paintings)
    String prefTitlePaintings;
    @BindString(R.string.pref_option_favorites)
    String prefOptionFavorites;
    @BindString(R.string.pref_title_favorites)
    String prefTitleFavorites;
    @BindString(R.string.pref_option_figure)
    String prefOptionFigure;
    @BindString(R.string.pref_title_figure)
    String prefTitleFigure;
    @BindColor(R.color.colorAccentFavorite)
    int toastBackgroundColor;

    private FragmentManager manager;
    private WorkFragment workFragment;
    private FavoritesFragment favoritesFragment;
    NavigationItemSelectedListener navigationItemSelectedListener =
            new NavigationItemSelectedListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_works_collection);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (isPhoneLand(this) || isTabletPortrait(this)) {
            columnCount = 2;
        } else if (isTabletLand(this)) {
            columnCount = 4;
        } else {
            columnCount = 1;
        }

        actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle(getLastToolbarTitle(WorksCollectionActivity.this));
        }

        String saveddNavOption = getLastNavOption(WorksCollectionActivity.this);
        setNavSelectedOption(saveddNavOption);
        navigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        artType = getLastArtType(WorksCollectionActivity.this);

        manager = getSupportFragmentManager();
        int navSelectedId = navigationView.getSelectedItemId();
        initFragmentPerSelectedItem(navSelectedId);

        // Get layout params from BottomNavigationView
        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) navigationView.getLayoutParams();
        // Set custom {@link}BottomNavigationBehavior to show hide when scrolling
        layoutParams.setBehavior(new BottomNavigationBehavior());
        // Set listener for BottomNavigation item selected event
        navigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(NESTED_SCROLL_POSITION,
                new int[]{ nsv.getScrollX(), nsv.getScrollY()});
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null) {
            final int[] position = savedInstanceState.getIntArray(NESTED_SCROLL_POSITION);
            if(position != null) {
                nsv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nsv.smoothScrollTo(position[0], position[1]);
                    }
                }, 600);
            }
        }
    }

    @Override
    public void onListFragmentInteraction(ArtObject artObject) {
        Intent detailsIntent = new Intent(WorksCollectionActivity.this,
                WorkDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(WorkDetailsActivity.CURRENT_ART_OBJECT, artObject);
        detailsIntent.putExtras(bundle);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Bundle transitionBundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(detailsIntent, transitionBundle);
        } else {
            startActivity(detailsIntent);
        }
    }

    public void showAppInfo() {
        Intent intent = new Intent(this, AppInfoActivity.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Bundle transitionBundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, transitionBundle);
        } else {
            startActivity(intent);
        }
    }

    public void showEvents() {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        // date pickerDialog dialog
        pickerDialog = new DatePickerDialog(WorksCollectionActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        String dayString = "";
                        String monthString = "";
                        String yearString = "";
                        if (d < 10) {
                            dayString = "0" + d;
                        } else {
                            dayString = d + "";
                        }
                        if (m + 1 < 10) {
                            monthString = "0" + (m + 1);
                        } else {
                            monthString = (m + 1) + "";
                        }

                        eventDateString = "" + y + "-" + monthString + "-" + dayString;
                        Intent intent = new Intent(WorksCollectionActivity.this,
                                EventsActivity.class);
                        intent.putExtra(NEXT_DATE_STRING, eventDateString);
                        startActivity(intent);

                        Log.d(LOG_TAG, "date is: " + eventDateString);

                    }
                }, year, month, day);
        pickerDialog.getDatePicker().setMinDate(new Date().getTime() + DAY_IN_MILLIS);
        pickerDialog.show();
    }

    // Inner class to handle BottomNavigationView item selection
    private class NavigationItemSelectedListener
            implements BottomNavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int itemId = item.getItemId();

            switch (itemId) {
                case R.id.action_paintings:
                    setLastNavOption(WorksCollectionActivity.this, prefOptionPaintings);
                    setLastToolbarTitle(WorksCollectionActivity.this, prefTitlePaintings);
                    setLastArtType(WorksCollectionActivity.this, ART_TYPE_PAINTING);
                    artType = getLastArtType(WorksCollectionActivity.this);

                    if(actionBar != null) {
                        actionBar.setTitle(getLastToolbarTitle(WorksCollectionActivity.this));
                    }

                    workFragment = WorkFragment.newInstance(columnCount, artType);
                    manager.beginTransaction()
                            .replace(R.id.worksFragmentFrameLayout, workFragment)
                            .commit();
                    return true;

                case R.id.action_favorites:
                    setLastNavOption(WorksCollectionActivity.this, prefOptionFavorites);
                    setLastToolbarTitle(WorksCollectionActivity.this, prefTitleFavorites);

                    if(actionBar != null) {
                        actionBar.setTitle(getLastToolbarTitle(WorksCollectionActivity.this));
                    }

                    favoritesFragment = FavoritesFragment.newInstance(columnCount);
                    manager.beginTransaction()
                            .replace(R.id.worksFragmentFrameLayout, favoritesFragment)
                            .commit();
                    return true;
                case R.id.action_figure:
                    setLastNavOption(WorksCollectionActivity.this, prefOptionFigure);
                    setLastToolbarTitle(WorksCollectionActivity.this, prefTitleFigure);
                    setLastArtType(WorksCollectionActivity.this, ART_TYPE_FIGURE);
                    artType = getLastArtType(WorksCollectionActivity.this);

                    if(actionBar != null) {
                        actionBar.setTitle(getLastToolbarTitle(WorksCollectionActivity.this));
                    }

                    workFragment = WorkFragment.newInstance(columnCount, artType);
                    manager.beginTransaction()
                            .replace(R.id.worksFragmentFrameLayout, workFragment)
                            .commit();
                    return true;
            }
            return false;
        }
    }
    // End of inner classes -----------------------------------------------------------------------

    private void initFragmentPerSelectedItem(int navItemSelected) {
        switch (navItemSelected) {
            case R.id.action_paintings:
                workFragment = WorkFragment.newInstance(columnCount, artType);
                manager.beginTransaction().replace(R.id.worksFragmentFrameLayout, workFragment).commit();
                break;
            case R.id.action_favorites:
                favoritesFragment = FavoritesFragment.newInstance(columnCount);
                manager.beginTransaction().replace(R.id.worksFragmentFrameLayout, favoritesFragment).commit();
                break;
            case R.id.action_figure:
                workFragment = WorkFragment.newInstance(columnCount, artType);
                manager.beginTransaction().replace(R.id.worksFragmentFrameLayout, workFragment).commit();
                break;
            default:
                break;

        }
    }

    private void setNavSelectedOption(String navSelectedOption) {
        switch(navSelectedOption) {
            case PREF_OPTION_PAINTINGS:
                navigationView.setSelectedItemId(R.id.action_paintings);
                break;
            case PREF_OPTION_FAVORITES:
                navigationView.setSelectedItemId(R.id.action_favorites);
                break;
            case PREF_OPTION_FIGURE:
                navigationView.setSelectedItemId(R.id.action_figure);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_more:
                showAppInfo();
                break;
            case R.id.action_events:
                showEvents();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
