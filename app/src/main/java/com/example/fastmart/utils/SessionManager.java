package com.example.fastmart.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    private static final String PREF_NAME = "FastMartSession";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_NAME = "name";
    private static final String KEY_ACCOUNT_TYPE = "accountType";
    private static final String KEY_EMAIL = "email";

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(String userId, String name, String email, String accountType) {
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ACCOUNT_TYPE, accountType);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    public String getAccountType() {
        return pref.getString(KEY_ACCOUNT_TYPE, "");
    }

    public String getUserId() {
        return pref.getString(KEY_USER_ID, "");
    }

    public String getName() {
        return pref.getString(KEY_NAME, "");
    }

    public String getEmail() {
        return pref.getString(KEY_EMAIL, "");
    }

    public void logoutUser() {
        editor.clear();
        editor.apply();
    }
}