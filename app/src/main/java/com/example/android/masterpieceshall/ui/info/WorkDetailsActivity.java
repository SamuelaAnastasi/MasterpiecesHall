package com.example.android.masterpieceshall.ui.info;

/*
 * This Capstone project is part of Android Developer Nanodegree Scholarship Program by
 * Udacity and Google
 *
 * The project is licensed under the MIT License(https://opensource.org/licenses/MIT)
 *
 * Copyright (c) 2018 - Samuela Anastasi
 */

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.masterpieceshall.R;
import com.example.android.masterpieceshall.analytics.Analytics;
import com.example.android.masterpieceshall.api.ApiClientGenerator;
import com.example.android.masterpieceshall.api.ApiInterface;
import com.example.android.masterpieceshall.app.MyApplication;
import com.example.android.masterpieceshall.database.ArtsContract;
import com.example.android.masterpieceshall.database.ArtsContract.ArtsEntry;
import com.example.android.masterpieceshall.model.ArtObject;
import com.example.android.masterpieceshall.model.ArtObjectDetails;
import com.example.android.masterpieceshall.model.ArtObjectPage;
import com.example.android.masterpieceshall.model.Label;
import com.example.android.masterpieceshall.model.WebImage;
import com.example.android.masterpieceshall.model.WorkDetails;
import com.example.android.masterpieceshall.widget.ArtWidgetService;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.android.masterpieceshall.BuildConfig.API_KEY;
import static com.example.android.masterpieceshall.utilities.Constants.FORMAT_JSON;
import static com.example.android.masterpieceshall.utilities.Constants.KEY_API_KEY;
import static com.example.android.masterpieceshall.utilities.Constants.KEY_FORMAT;
import static com.example.android.masterpieceshall.utilities.StringUtils.getAuthorFromString;
import static com.example.android.masterpieceshall.utilities.StringUtils.getPeriodFromString;
import static com.example.android.masterpieceshall.utilities.StringUtils.handleEmptyStateStrings;
import static com.example.android.masterpieceshall.utilities.StringUtils.isValidString;
import static com.example.android.masterpieceshall.utilities.StringUtils.setArtAuthor;
import static com.example.android.masterpieceshall.utilities.StringUtils.setArtDescription;
import static com.example.android.masterpieceshall.utilities.UiUtils.loadImage;
import static com.example.android.masterpieceshall.utilities.UiUtils.showToast;
import static com.example.android.masterpieceshall.utilities.Utils.isConnected;
import static com.example.android.masterpieceshall.utilities.Utils.isPhoneLand;
import static com.example.android.masterpieceshall.utilities.Utils.isPhonePortrait;
import static com.example.android.masterpieceshall.utilities.Utils.isTabletLand;
import static com.example.android.masterpieceshall.utilities.Utils.isTabletPortrait;

public class WorkDetailsActivity extends AppCompatActivity {

    private static final String LOG_TAG = WorkDetailsActivity.class.getSimpleName();

    public static final String CURRENT_ART_OBJECT = "CURRENT_ART_OBJECT";

    // SavedInstanceState bundle keys
    public static final String KEY_BOTTOM_SHEET_STATE = "bottomSheetState";
    public static final String KEY_IS_FAVORITE = "isFavorite";
    public static final String KEY_IS_DETAILS_IMAGE_URL = "isDetailsImageUrl";
    public static final String KEY_IS_SCALE_CHANGED = "isScaleChanged";

    private ArtObject artObject = new ArtObject();

    private Map<String, String> params;
    private WorkDetails workDetails;
    private ArtObjectDetails artObjectDetails;
    private ArtObjectPage artObjectPage;

    View decorView;
    PhotoView workIv;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.authorTv)
    TextView authorTV;
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.mediumTv)
    TextView mediumTv;
    @BindView(R.id.dimensionsTv)
    TextView dimensionsTv;
    @BindView(R.id.periodTv)
    TextView periodTv;
    @BindView(R.id.view)
    View descriptionDivider;
    @BindView(R.id.descriptionTv)
    TextView descriptionTv;
    @BindView(R.id.favorite_fab)
    FloatingActionButton fab;
    @BindView(R.id.expandButton)
    ImageButton expandButton;
    @BindView(R.id.retryButton)
    Button retryButton;
    @BindView(R.id.shareButton)
    ImageButton shareButton;
    @BindView(R.id.adContainerLayout)
    LinearLayout adMobContainer;
    @BindView(R.id.workInfoCoordinator)
    CoordinatorLayout workInfoCoordinator;
    @BindView(R.id.nsv)
    NestedScrollView nsv;
    @BindView(R.id.bottomSheet)
    ConstraintLayout layoutBottomSheet;
    @BindView(R.id.no_internet)
    ConstraintLayout noInternet;

    // Bind resources
    @BindColor(R.color.fab_state_selector_normal)
    ColorStateList fabColorNormal;
    @BindColor(R.color.fab_state_selector_favorite)
    ColorStateList fabColorFavorite;
    @BindColor(R.color.fab_state_selector_disabled)
    ColorStateList fabColorDisabled;
    @BindColor(R.color.colorAccentFavorite)
    int fabFavoriteBackgroundColor;
    @BindString(R.string.removed_from_favorites)
    String removedFromFavorites;
    @BindString(R.string.added_to_favorites)
    String addedToFavorites;
    @BindString(R.string.workInfoTitle)
    String workInfoTitle;
    @BindString(R.string.authorUnknown)
    String authorUnknown;
    @BindString(R.string.untitledWork)
    String untitledWork;
    @BindString(R.string.techniqueNotProvided)
    String techniqueNotProvided;
    @BindString(R.string.periodUnknown)
    String periodUnknown;
    @BindString(R.string.unknownDimensions)
    String unknownDimensions;
    @BindString(R.string.toastNoInternetError)
    String noInternetMessage;

    BottomSheetBehavior sheetBehavior;

    private String objectNumber;
    private String intentAuthor;
    private String title;
    private String longTitle;
    private String imageUrl;
    private String headerUrl;
    private String workPeriod;


    private String workDescription;
    private String workDimensions;
    private String physicalMedium;
    private String authorFromDetails;
    private String plaqueDescription;
    private String plaqueDescriptionEnglish;

    private String author;

    int bottomSheetState;
    boolean isScaleChanged = false;
    boolean showFab = true;
    boolean isFavorite = false;
    boolean isDetailsImageUrl;
    Intent intent;

    MyApplication app;
    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_details);

        ButterKnife.bind(this);

        app = (MyApplication) getApplication();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                app.loadAd(adMobContainer);
            }
        }, 100);

        decorView = getWindow().getDecorView();
        workIv = findViewById(R.id.workIv);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(workInfoTitle);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        intent = getIntent();
        Bundle bundle = intent.getExtras();
        artObject = bundle.getParcelable(CURRENT_ART_OBJECT);

        if (artObject != null) {
            objectNumber = artObject.getObjectNumber();
            title = artObject.getTitle();
            intentAuthor = artObject.getPrincipalOrFirstMaker();
            longTitle = artObject.getLongTitle();
            workPeriod = getPeriodFromString(artObject.getLongTitle());
            imageUrl = artObject.getWebImage().getUrl();
            headerUrl = artObject.getHeaderImage().getUrl();
            Log.d(LOG_TAG, "header url: " + headerUrl);

            if(!isConnected(this)) {
                populateViewsFromIntent();
            }
        }

        workIv.setOnPhotoTapListener(new PhotoTapListener());

        // FAB animation
        final Animation growAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_grow);
        final Animation shrinkAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_shrink);
        fab.setVisibility(View.VISIBLE);
        fab.startAnimation(growAnimation);

        if (isPhoneLand(this)) {

            shrinkAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    fab.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
        }

        // BottomSheet behavior
        sheetBehavior = BottomSheetBehavior.from(nsv);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {

                if (!isTabletPortrait(WorkDetailsActivity.this)) {

                    switch (newState) {

                        case BottomSheetBehavior.STATE_DRAGGING:
                            if (showFab)
                                fab.startAnimation(shrinkAnimation);
                            break;

                        case BottomSheetBehavior.STATE_COLLAPSED:
                            showFab = true;
                            fab.setVisibility(View.VISIBLE);
                            fab.startAnimation(growAnimation);
                            break;

                        case BottomSheetBehavior.STATE_EXPANDED:
                            showFab = false;
                            fab.setVisibility(View.GONE);
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
                expandButton.setRotation(slideOffset * 180);
            }
        });

        // Collapse expand button on BottomSheet
        expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int bottomSheetState = sheetBehavior.getState();

                if (bottomSheetState == BottomSheetBehavior.STATE_COLLAPSED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    if (isPhoneLand(WorkDetailsActivity.this)) {
                        fab.startAnimation(shrinkAnimation);
                    }
                } else if (bottomSheetState == BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        setFullScreen();

        if (savedInstanceState != null) {
            bottomSheetState = savedInstanceState.getInt(KEY_BOTTOM_SHEET_STATE);
            sheetBehavior.setState(bottomSheetState);
            if (isPhoneLand(this) && bottomSheetState == BottomSheetBehavior.STATE_EXPANDED) {
                fab.startAnimation(shrinkAnimation);
            }
            isFavorite = savedInstanceState.getBoolean(KEY_IS_FAVORITE);
            if (isFavorite) {
                setFabColorFavorite();
            }
            isDetailsImageUrl = savedInstanceState.getBoolean(KEY_IS_DETAILS_IMAGE_URL);
            isScaleChanged = savedInstanceState.getBoolean(KEY_IS_SCALE_CHANGED);
        } else {
            isFavorite = isArtFavorite(artObject);
            if (isFavorite) {
                setFabColorFavorite();
            }
        }
        params = getQueryMap();
        fetchWorkDetails();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        bottomSheetState = sheetBehavior.getState();
        outState.putInt(KEY_BOTTOM_SHEET_STATE, bottomSheetState);
        outState.putBoolean(KEY_IS_FAVORITE, isFavorite);
        outState.putBoolean(KEY_IS_DETAILS_IMAGE_URL, isDetailsImageUrl);
        outState.putBoolean(KEY_IS_SCALE_CHANGED, isScaleChanged);
    }

    // Fetch data fro API
    private void fetchWorkDetails() {
        ApiInterface apiInterface = ApiClientGenerator.getClient(this).create(ApiInterface.class);
        Call<WorkDetails> call = apiInterface.getWorkDetails(objectNumber, params);
        call.enqueue(new Callback<WorkDetails>() {
            @Override
            public void onResponse(Call<WorkDetails> call, Response<WorkDetails> response) {
                if (response.isSuccessful()) {
                    workDetails = null;
                    workDetails = response.body();
                    if (workDetails != null) {
                        artObjectDetails = workDetails.getArtObject();
                        artObjectPage = workDetails.getArtObjectPage();
                        plaqueDescription = artObjectPage.getPlaqueDescription();
                        plaqueDescriptionEnglish = artObjectDetails.getPlaqueDescriptionEnglish();

                        Label label = artObjectDetails.getLabel();
                        if (label != null) {

                            String makerLine = label.getMakerLine();
                            authorFromDetails = getAuthorFromString(makerLine);
                            author = setArtAuthor(WorkDetailsActivity.this, intentAuthor,
                                    authorFromDetails);
                            workDescription = label.getDescription();
                            workDescription = setArtDescription(WorkDetailsActivity.this,
                                    workDescription, plaqueDescription, plaqueDescriptionEnglish);

                        } else  {

                            author = handleEmptyStateStrings(intentAuthor,
                                    authorUnknown);
                            workDescription = setArtDescription(WorkDetailsActivity.this,
                                    plaqueDescription, plaqueDescriptionEnglish);
                        }

                        title = handleEmptyStateStrings(title, untitledWork);
                        physicalMedium = (handleEmptyStateStrings(artObjectDetails.getPhysicalMedium(),
                                techniqueNotProvided));
                        workPeriod = handleEmptyStateStrings(workPeriod,
                                periodUnknown);
                        workDimensions = handleEmptyStateStrings(artObjectDetails.getSubTitle(),
                                unknownDimensions);
                        WebImage webImage = artObjectDetails.getWebImage();
                        if (webImage != null) {
                            imageUrl = handleEmptyStateStrings(
                                    artObjectDetails.getWebImage().getUrl(), "");
                            isDetailsImageUrl = true;
                        }
                        populateUi();
                        Analytics.logEventViewItem(WorkDetailsActivity.this, artObject);
                    }

                } else {

                    if (!isConnected(WorkDetailsActivity.this)) {
                        showNoInternetView();
                    }
                }
            }

            @Override
            public void onFailure(Call<WorkDetails> call, Throwable t) {
                Log.d(LOG_TAG, " error is: ", t);
                if (t instanceof IOException) {
                    showNoInternetView();
                } else {
                    Log.d(LOG_TAG, "Conversion error ", t);
                }
            }
        });
    }

    // Inner classes ----------------------------------------------------------------------
    private class PhotoTapListener implements OnPhotoTapListener {

        @Override
        public void onPhotoTap(ImageView view, float x, float y) {
            int bottomSheetVisibility = layoutBottomSheet.getVisibility();
            int fabVisibility = fab.getVisibility();
            int toolbarVisibility = toolbar.getVisibility();

            if (bottomSheetVisibility == View.VISIBLE &&
                    fabVisibility == View.VISIBLE &&
                    toolbarVisibility == View.VISIBLE) {

                layoutBottomSheet.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
                if (isScaleChanged) {
                    workIv.setScaleType(ImageView.ScaleType.CENTER);
                }

            } else if (bottomSheetVisibility == View.GONE &&
                    fabVisibility == View.GONE &&
                    toolbarVisibility == View.GONE) {

                layoutBottomSheet.setVisibility(View.VISIBLE);
                fab.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.VISIBLE);
                workIv.setScaleType(ImageView.ScaleType.FIT_CENTER);

                int bottomSheetState = sheetBehavior.getState();
                if (bottomSheetState == BottomSheetBehavior.STATE_COLLAPSED) {
                    app.loadAd(adMobContainer);
                    nsv.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            nsv.fling(0);
                            nsv.smoothScrollTo(0, 0);
                        }
                    }, 200);
                }
            }
        }
    }

    // DB CRUD operations private methods --------------------------------------------------------

    private Uri insertToFavorites(ArtObject artObject) {
        ContentValues values = new ContentValues();
        values.put(ArtsEntry.COLUMN_ART_OBJECT_NUMBER, artObject.getObjectNumber());
        values.put(ArtsEntry.COLUMN_ART_AUTHOR, artObject.getPrincipalOrFirstMaker());
        values.put(ArtsEntry.COLUMN_ART_TITLE, artObject.getTitle());
        values.put(ArtsEntry.COLUMN_ART_LONG_TITLE, artObject.getLongTitle());
        values.put(ArtsEntry.COLUMN_ART_IMAGE_PATH, artObject.getWebImage().getUrl());
        values.put(ArtsEntry.COLUMN_ART_HEADER_PATH, artObject.getHeaderImage().getUrl());
        // Insert to db through resolver
        return getContentResolver().insert(ArtsEntry.CONTENT_URI, values);
    }

    private int deleteFromFavorites(ArtObject artObject) {
        String artObjectNumber = artObject.getObjectNumber();
        String selection = ArtsEntry.COLUMN_ART_OBJECT_NUMBER + "=?";
        String[] selectionArgs = new String[] {artObjectNumber};
        // delete artModel through content resolver
        return getContentResolver().delete(ArtsEntry.CONTENT_URI, selection, selectionArgs);
    }

    private boolean isArtFavorite(ArtObject artObject) {
        Cursor cursor = getContentResolver().query(
                ArtsContract.ArtsEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String favoriteArtObjectNumber = cursor.getString(
                        cursor.getColumnIndex(ArtsEntry.COLUMN_ART_OBJECT_NUMBER));
                if ((artObject != null && (artObject.getObjectNumber()).equals(favoriteArtObjectNumber))) {
                    return true;
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return false;
    }

    // End of DB CRUD operations private methods ------------------------------------------------

    // Buttons helper methods -------------------------------------------------------------------

    @OnClick(R.id.shareButton)
    public void shareWorkInfo() {
        if (!isConnected(this)) {
            showToast(this, noInternetMessage, fabFavoriteBackgroundColor);
        } else {
            String contentType = "text/plain";
            String shareInfo = title + "\n" + author;
            startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(WorkDetailsActivity.this)
                    .setType(contentType)
                    .setText(shareInfo)
                    .getIntent(), getString(R.string.action_share)));

            Analytics.logEventShare(this, contentType);
        }
    }

    @OnClick(R.id.retryButton)
    public void retryLoading() {
        Animation slideUpAnimation = AnimationUtils.loadAnimation(
                WorkDetailsActivity.this, R.anim.slide_up_no_internet);
        noInternet.startAnimation(slideUpAnimation);
        noInternet.setVisibility(View.GONE);
        fetchWorkDetails();
    }

    @OnClick(R.id.favorite_fab)
    public void setIsFavorite() {
        if(isFavorite) {
            isFavorite = false;
            setFabColorNormal();
            int rowsDeleted = deleteFromFavorites(artObject);
            if (rowsDeleted > 0) {
                showToast(this, removedFromFavorites, fabFavoriteBackgroundColor);
                ArtWidgetService.startActionLoadArtObject(this);
            }
        } else {
            isFavorite = true;
            setFabColorFavorite();
            Uri uri = insertToFavorites(artObject);
            if (uri != null) {
                showToast(this, addedToFavorites, fabFavoriteBackgroundColor);
                ArtWidgetService.startActionLoadArtObject(this);
            }
        }
    }

    // Handle FAB background color --------------------------------------------------------------
    private void setFabColorFavorite() {
        fab.setBackgroundTintList(fabColorFavorite);
        fab.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.ic_favorites_button_white));
    }

    private void setFabColorNormal() {
        fab.setBackgroundTintList(fabColorNormal);
        fab.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.ic_favorites_button));
    }

    private void populateViewsFromIntent() {
        titleTv.setText(title);
        authorTV.setText(intentAuthor);
    }

    public void setFullScreen() {

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private HashMap<String, String> getQueryMap() {
        HashMap<String, String> params = new HashMap<>();
        params.put(KEY_API_KEY, API_KEY);
        params.put(KEY_FORMAT, FORMAT_JSON);
        return params;
    }

    private void populateUi() {
        authorTV.setText(author);
        titleTv.setText(title);
        mediumTv.setText(physicalMedium);
        dimensionsTv.setText(workDimensions);
        periodTv.setText(workPeriod);
        descriptionTv.setText(workDescription);

        if (isValidString(imageUrl)) {
            loadImage(imageUrl, workIv);
            if (isDetailsImageUrl) {
                workIv.setScaleType(ImageView.ScaleType.CENTER);
                isScaleChanged = true;
                Log.d("Details info", " image view: " + workIv.getScaleType());
            }
        }

        fab.setEnabled(true);
        if (isFavorite) {
            fab.setBackgroundTintList(fabColorFavorite);
        } else {
            fab.setBackgroundTintList(fabColorNormal);
        }
        noInternet.setVisibility(View.GONE);
        shareButton.setVisibility(View.VISIBLE);
        descriptionDivider.setVisibility(View.VISIBLE);
        descriptionTv.setVisibility(View.VISIBLE);
        shareButton.setEnabled(true);
    }

    private void showNoInternetView() {
        fab.setEnabled(false);
        fab.setBackgroundTintList(fabColorDisabled);
        Animation slideDownAnimation = AnimationUtils.loadAnimation(
                WorkDetailsActivity.this, R.anim.slide_down_no_internet);
        noInternet.startAnimation(slideDownAnimation);
        noInternet.setVisibility(View.VISIBLE);
        descriptionDivider.setVisibility(View.GONE);
        descriptionTv.setVisibility(View.GONE);
        shareButton.setVisibility(View.GONE);
        shareButton.setEnabled(false);
        Log.d(LOG_TAG, "No internet connection");
    }
}
