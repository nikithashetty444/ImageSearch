package com.arindom.imagesearchapp.network

interface GenericApiListener<in S, in F> {
    fun onSuccess(message: S)
    fun onFailure(message: F)
}