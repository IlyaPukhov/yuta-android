package com.ilyap.yuta.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ilyap.yutarefactor.domain.entity.UserUpdateDto;
import com.ilyap.yuta.ui.LoginActivity;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

@UtilityClass
public class UserUtils {
    @Getter
    private static UserUpdateDto currentUserDto;
    private static SharedPreferences sharedPreferences;

    public static void loadImageToImageViewWithCaching(ImageView imageView, String path) {
        Glide.with(imageView).load(getPath(path)).into(imageView);
    }

    public static void loadImageToImageViewWithoutCaching(ImageView imageView, String path) {
        getConfiguredGlideBuilder(Glide.with(imageView).load(getPath(path))).into(imageView);
    }

    public static <T> RequestBuilder<T> getConfiguredGlideBuilder(RequestBuilder<T> requestBuilder) {
        return requestBuilder.dontTransform()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .priority(Priority.IMMEDIATE)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .listener(new RequestListener<T>() {
                    @Override
                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, @Nullable @org.jetbrains.annotations.Nullable Object model, @NonNull @NotNull Target<T> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull @NotNull T resource, @NonNull @NotNull Object model, Target<T> target, @NonNull @NotNull DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                });
    }

    public static String getPath(String path) {
        return RequestUtils.getRootUrl() + path;
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

    public static void setCurrentUserDto(UserUpdateDto currentUserDto) {
        UserUtils.currentUserDto = currentUserDto;
    }
}