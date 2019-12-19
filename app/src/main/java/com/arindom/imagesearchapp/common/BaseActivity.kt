package com.arindom.imagesearchapp.common

import androidx.appcompat.app.AppCompatActivity
import com.arindom.imagesearchapp.utils.ImageLoader

open class BaseActivity : AppCompatActivity() {
    protected val mImageLoader by lazy { ImageLoader.getInstance(this) }

}