package com.arindom.imagesearchapp.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.StatFs
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.arindom.imagesearchapp.R
import com.arindom.imagesearchapp.network.GenericApiListener
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.*
import com.squareup.picasso.Target
import okhttp3.Cache
import okhttp3.Credentials
import okhttp3.OkHttpClient
import java.io.File
import java.lang.IllegalArgumentException
import kotlin.math.max
import kotlin.math.min

class ImageLoader {
    private var mPicasso: Picasso

    private constructor(mContext: Context) {
        val mLruCache = LruCache(mContext)
        val httpCacheDirectory = File(mContext.cacheDir, "picasso-cache")
        val okHttpClient = OkHttpClient.Builder()
            .cache(Cache(httpCacheDirectory, calculateDiskCacheSize(httpCacheDirectory)))
            .build()


        val builder = Picasso.Builder(mContext)
        builder.memoryCache(mLruCache)
        builder.downloader(OkHttp3Downloader(okHttpClient))
        this.mPicasso = builder.build()
    }

    private fun calculateDiskCacheSize(dir: File): Long {
        var size = (5 * 1024 * 1024).toLong()
        try {
            val statFs = StatFs(dir.absolutePath)
            val available = statFs.blockCountLong * statFs.blockSizeLong
            // Target 2% of the total space.
            size = available / 50
        } catch (ignored: IllegalArgumentException) {
            ignored.printStackTrace()
        }

        // Bound inside min/max size for disk cache.
        return max(min(size, (50 * 1024 * 1024).toLong()), (5 * 1024 * 1024).toLong())
    }

    fun loadImageFromURL(
        url: String?,
        mImageView: ImageView,
        apiListener: GenericApiListener<Boolean, String?>
    ) {
        mPicasso.load(url)
            .error(R.drawable.default_image)
            .placeholder(R.drawable.default_image)
            .into(mImageView, object : Callback {
                override fun onSuccess() {
                    apiListener.onSuccess(true)
                }

                override fun onError() {
                    apiListener.onFailure(null)
                }

            })

    }

    fun loadImageDrawables(@DrawableRes resource: Int, mImageView: ImageView) {
        mPicasso.load(resource)
            .placeholder(R.drawable.default_image)
            .error(R.drawable.default_image)
            .resize(
                642,
                1080
            )
            .into(mImageView)
    }

    fun loadRoundedImageFromURL(url: String, mImageView: ImageView, apiListener: GenericApiListener<Boolean, String?>) {
        mPicasso.load(url)
            .placeholder(R.drawable.default_image)
            .error(R.drawable.default_image)
            .transform(CircleTransform())
            .into(mImageView, object : Callback {
                override fun onSuccess() {
                    apiListener.onSuccess(true)
                }

                override fun onError() {
                    apiListener.onFailure(null)
                }

            })

    }

    fun getBitmap(URL: String, apiListener: GenericApiListener<Bitmap, String?>) {
        mPicasso
            .load(URL)
            .placeholder(R.drawable.default_image)
            .error(R.drawable.default_image)
            .into(object : Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                }

                override fun onBitmapFailed(errorDrawable: Drawable?) {
                    apiListener.onFailure(null)
                }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    if (bitmap != null) {
                        apiListener.onSuccess(bitmap)
                    } else {
                        apiListener.onFailure(null)
                    }

                }


            })
    }

    companion object {
        @Volatile
        private var INSTANCE: ImageLoader? = null

        @JvmStatic
        fun getInstance(mContext: Context): ImageLoader =
            INSTANCE ?: synchronized(this)
            {
                INSTANCE ?: ImageLoader(mContext).also { INSTANCE = it }
            }
    }

    internal inner class CircleTransform : Transformation {
        override fun transform(source: Bitmap): Bitmap {
            val size = Math.min(source.width, source.height)

            val x = (source.width - size) / 2
            val y = (source.height - size) / 2

            val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
            if (squaredBitmap != source) {
                source.recycle()
            }

            val bitmap = Bitmap.createBitmap(size, size, source.config)

            val canvas = Canvas(bitmap)
            val paint = Paint()
            val shader = BitmapShader(
                squaredBitmap,
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP
            )
            paint.setShader(shader)
            paint.setAntiAlias(true)

            val r = size / 2f
            canvas.drawCircle(r, r, r, paint)

            squaredBitmap.recycle()
            return bitmap
        }

        override fun key(): String {
            return "circle"
        }
    }

}