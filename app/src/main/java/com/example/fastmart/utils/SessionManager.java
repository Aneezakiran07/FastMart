package com.example.fastmart.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME    = "FastMartPrefs";
    private static final String KEY_LOGGED_IN   = "isLoggedIn";
    private static final String KEY_USER_ID     = "userId";
    private static final String KEY_NAME        = "name";
    private static final String KEY_EMAIL       = "email";
    private static final String KEY_ACCOUNT     = "accountType";

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs  = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void createLoginSession(String userId, String name, String email, String accountType) {
        editor.putBoolean(KEY_LOGGED_IN, true);
        editor.putString(KEY_USER_ID,    userId);
        editor.putString(KEY_NAME,       name);
        editor.putString(KEY_EMAIL,      email);
        editor.putString(KEY_ACCOUNT,    accountType);
        editor.apply();
    }

    public boolean isLoggedIn()       { return prefs.getBoolean(KEY_LOGGED_IN, false); }
    public String  getUserId()        { return prefs.getString(KEY_USER_ID, null); }
    public String  getName()          { return prefs.getString(KEY_NAME, null); }
    public String  getEmail()         { return prefs.getString(KEY_EMAIL, null); }
    public String  getAccountType()   { return prefs.getString(KEY_ACCOUNT, null); }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}