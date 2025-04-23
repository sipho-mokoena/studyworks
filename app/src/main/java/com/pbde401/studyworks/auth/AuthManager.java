package com.pbde401.studyworks.auth;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthManager {
    private static final String PREF_NAME = "auth_store";
    private static final String KEY_USER_ROLE = "user_role";
    private static final String KEY_IS_AUTHENTICATED = "is_authenticated";

    private SharedPreferences prefs;
    private static AuthManager instance;

    private AuthManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized AuthManager getInstance(Context context) {
        if (instance == null) {
            instance = new AuthManager(context.getApplicationContext());
        }
        return instance;
    }

    public void setUser(String role, boolean isAuthenticated) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_ROLE, role);
        editor.putBoolean(KEY_IS_AUTHENTICATED, isAuthenticated);
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