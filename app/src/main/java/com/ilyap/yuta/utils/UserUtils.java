package com.ilyap.yuta.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.ui.LoginActivity;
import lombok.experimental.UtilityClass;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

@UtilityClass
public class UserUtils {
    private User currentUser;
    private SharedPreferences sharedPreferences;

    public static void loadImageToImageView(ImageView imageView, String path) {
        getConfiguredGlideBuilder(Glide.with(imageView).load(path)).into(imageView);
    }

    public static <T> RequestBuilder<T> getConfiguredGlideBuilder(RequestBuilder<T> requestBuilder) {
        return requestBuilder.dontTransform()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .priority(Priority.IMMEDIATE)
                .apply(RequestOptions.centerInsideTransform());
    }

    public static void setUserId(Context context, int id) {
        if (id >= 0) {
            sharedPreferences = getSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("user_id", id).apply();
        }
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

    public static int getUserId(Context context) {
        return getSharedPreferences(context).getInt("user_id", -1);
    }

    public static SharedPreferences getSharedPreferences(@NonNull Context context) {
        return context.getSharedPreferences("session", Context.MODE_PRIVATE);
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        UserUtils.currentUser = currentUser;
    }
}