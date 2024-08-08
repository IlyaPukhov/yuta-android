package com.yuta.app.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.load.engine.GlideException
import com.yuta.app.utils.UserUtils.getPath

object ImageUtils {

    fun loadImageToImageViewWithCaching(imageView: ImageView, path: String) {
        Glide.with(imageView)
            .load(getPath(path))
            .into(imageView)
    }

    fun loadImageToImageViewWithoutCaching(imageView: ImageView, path: String) {
        getConfiguredGlideBuilder(Glide.with(imageView).load(getPath(path)))
            .into(imageView)
    }

    fun <T> getConfiguredGlideBuilder(requestBuilder: RequestBuilder<T>): RequestBuilder<T> {
        return requestBuilder
            .dontTransform()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .priority(Priority.IMMEDIATE)
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .listener(object : RequestListener<T> {
                override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: Target<T>, p3: Boolean): Boolean {
                    return false
                }

                override fun onResourceReady(
                    p0: T & Any,
                    p1: Any,
                    p2: Target<T>?,
                    p3: DataSource,
                    p4: Boolean
                ): Boolean {
                    return false
                }
            })
    }
}
