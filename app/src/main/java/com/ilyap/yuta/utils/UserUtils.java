package com.ilyap.yuta.utils;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.ui.LoginActivity;

public final class UserUtils {
    private static User currentUser;
    private static SharedPreferences sharedPreferences;
    public static final String EMPTY_DATA = "";

    private UserUtils() {
    }

    public static void loadImage(Context context, String path, ImageView imageView){
        Glide.with(context)
                .load(path)
                .dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .priority(Priority.IMMEDIATE)
                .into(imageView);
    }

    public static void setUserId(Activity activity, int id) {
        sharedPreferences = getSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("user_id", id).apply();
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void logOut(Activity activity) {
        sharedPreferences = getSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user_id").apply();

        Intent intent = new Intent(activity, LoginActivity.class);
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    public static int getUserId(Activity activity) {
        return getSharedPreferences(activity).getInt("user_id", -1);
    }

    private static SharedPreferences getSharedPreferences(Activity activity) {
        return activity.getSharedPreferences("session", Context.MODE_PRIVATE);
    }
}