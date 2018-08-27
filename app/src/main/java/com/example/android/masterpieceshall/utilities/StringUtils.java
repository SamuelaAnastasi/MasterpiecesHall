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
import android.text.TextUtils;

import com.example.android.masterpieceshall.R;

public class StringUtils {
    private static final String ANONYMOUS = "anonymous";

    private StringUtils() {}

    public static boolean isValidString(String anyString) {
        return !TextUtils.isEmpty(anyString);
    }

    public static boolean isAuthorAnonymous(String authorString) {
        return ANONYMOUS.equals(authorString.toLowerCase());
    }

    public static String getAuthorFromString(String anyString) {
        String authorString = "";
        if (isValidString(anyString)) {
            String[] stringArray = anyString.split(", ");
            if (isValidStringArray(stringArray)) {
                authorString = stringArray[0].trim();
            }
        }
        return authorString;
    }

    public static String getPeriodFromString (String anyString) {
        String periodString = "";
        if (isValidString(anyString)) {
            String[] stringArray = anyString.split(", ");
            if (isValidStringArray(stringArray)) {
                periodString = stringArray[stringArray.length - 1].trim();
            }
        }
        return periodString;
    }

    public static boolean isValidStringArray(String[] stringArray) {
        return (stringArray != null && stringArray.length > 0);
    }

    public static String removeNewLines(String anyString) {
        return anyString.replaceAll("(\n|\r\n\r\n)", " ");
    }

    public static String handleEmptyStateStrings(String original, String replacement) {
        return (isValidString(original)) ? original : replacement;
    }

    public static String setArtAuthor(Context context, String intentAuthor, String detailsAuthor) {
        if (isValidString(intentAuthor) && isAuthorAnonymous(intentAuthor)) {
            return intentAuthor;
        } else if (isValidString(intentAuthor) &&
                !(detailsAuthor.contains(intentAuthor))) {
            return intentAuthor;
        } else {
            return handleEmptyStateStrings(detailsAuthor,
                    context.getString(R.string.authorUnknown));
        }
    }

    public static String setArtDescription(
            Context context,
            String workDescr,
            String plaqueDescr,
            String plaqueDescrEnglish) {

        if (isValidString(workDescr)) {
            return removeNewLines(workDescr);
        } else if (isValidString(plaqueDescr)) {
            return removeNewLines(plaqueDescr);
        } else {
            return removeNewLines(handleEmptyStateStrings(plaqueDescrEnglish,
                    context.getString(R.string.descriptionNotProvided)));
        }
    }

    public static String setArtDescription(
            Context context,
            String plaqueDescr,
            String plaqueDescrEnglish) {

        if (isValidString(plaqueDescr)) {
            return removeNewLines(plaqueDescr);
        } else {
            return removeNewLines(handleEmptyStateStrings(plaqueDescrEnglish,
                    context.getString(R.string.descriptionNotProvided)));
        }
    }

    public static String getDateSubstring(String anyString) {
        String dateString = "";
        if (isValidString(anyString)) {
            return anyString.substring(0, anyString.indexOf('T'));
        }
        return dateString;
    }
}

