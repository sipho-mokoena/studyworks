package com.pbde401.studyworks.auth;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthStore {
    private static final String PREF_NAME = "auth_store";
    private static final String KEY_USER_ROLE = "user_role";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_IS_AUTHENTICATED = "is_authenticated";

    private static AuthStore instance;
    private SharedPreferences prefs;

    private AuthStore(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized AuthStore getInstance(Context context) {
        if (instance == null) {
            instance = new AuthStore(context.getApplicationContext());
        }
        return instance;
    }

    public void setUser(String role, String fullName) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_ROLE, role);
        editor.putString(KEY_USER_NAME, fullName);
        editor.putBoolean(KEY_IS_AUTHENTICATED, true);
        editor.apply();
    }

    public String getUserRole() {
        return prefs.getString(KEY_USER_ROLE, "guest");
    }

    public boolean isAuthenticated() {
        return prefs.getBoolean(KEY_IS_AUTHENTICATED, false);
    }

    public void logout() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
}