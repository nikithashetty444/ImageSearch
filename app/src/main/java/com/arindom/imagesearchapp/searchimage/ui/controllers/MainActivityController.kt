package com.arindom.imagesearchapp.searchimage.ui.controllers

import com.arindom.imagesearchapp.common.ActivityControllers
import com.arindom.imagesearchapp.network.AppApiHandler
import com.arindom.imagesearchapp.network.GenericApiListener
import com.arindom.imagesearchapp.network.modals.SearchResult
import com.arindom.imagesearchapp.searchimage.modals.Photo
import com.arindom.imagesearchapp.searchimage.modals.PhotoPerPage
import com.arindom.imagesearchapp.searchimage.ui.MainActivity

class MainActivityController : ActivityControllers<MainActivity> {
    private var mActivity: MainActivity? = null

    override fun create(mActivity: MainActivity) {
        this.mActivity = mActivity
    }

    fun getPhotos(searchText: String, currentPage: Int) {
        AppApiHandler.getPhotoes(searchText, currentPage,
            object : GenericApiListener<SearchResult.Photos, String> {
                override fun onSuccess(message: SearchResult.Photos) {
                    val mPhotoPerPage =
                        PhotoPerPage(message.page, message.pages, message.photo.map {
                            Photo(
                                it.id,
                                it.title,
                                "https://farm${it.farm}.staticflickr.com/${it.server}/${it.id}_${it.secret}.jpg"
                            )
                        }.toCollection(mutableListOf()))
                    mActivity?.updateSearchRecyclerView(mPhotoPerPage)
                }

                override fun onFailure(message: String) {
                    mActivity?.onServiceFailed(message)
                }
            })
    }

    override fun destroy() {
        mActivity = null
    }
}