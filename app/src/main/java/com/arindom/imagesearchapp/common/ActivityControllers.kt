package com.arindom.imagesearchapp.common


interface ActivityControllers<in ActivityContext> {
    fun create(mActivity: ActivityContext)
    fun destroy()
}