package com.example.android.masterpieceshall.utilities;

/*
 * This Capstone project is part of Android Developer Nanodegree Scholarship Program by
 * Udacity and Google
 *
 * The project is licensed under the MIT License(https://opensource.org/licenses/MIT)
 *
 * Copyright (c) 2018 - Samuela Anastasi
 */

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.masterpieceshall.R;
import com.squareup.picasso.Picasso;

public class UiUtils {
    private UiUtils() {}

    public static void loadImage(String imageUrl, ImageView view) {
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_shape)
                .error(R.drawable.error_shape)
                .into(view);
    }

    public static void showToast (Context context, String message, int color) {
        Toast toast = Toast.makeText(context, message,Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.setBackgroundColor(color);
        view.setPadding(120,24,120,24);
        toast.show();
    }
}
