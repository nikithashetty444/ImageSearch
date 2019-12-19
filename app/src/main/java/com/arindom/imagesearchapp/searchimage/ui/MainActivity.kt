package com.arindom.imagesearchapp.searchimage.ui

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.arindom.imagesearchapp.R
import com.arindom.imagesearchapp.common.BaseActivity
import com.arindom.imagesearchapp.searchimage.modals.PhotoPerPage
import com.arindom.imagesearchapp.searchimage.ui.adapters.PaginationListener
import com.arindom.imagesearchapp.searchimage.ui.adapters.SearchedListAdapter
import com.arindom.imagesearchapp.searchimage.ui.controllers.MainActivityController
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {
    private lateinit var mSearchedListAdapter: SearchedListAdapter
    private lateinit var gridLayoutManager: GridLayoutManager
    private var mMainActivityController: MainActivityController? = null
    private var mCurrentPage = 0
    private var isLoading = false
    private var isLastPage = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mMainActivityController = MainActivityController()
        init()
    }

    override fun onStart() {
        super.onStart()
        mMainActivityController?.create(this)
    }

    private fun init() {
        setAdapter()
        setListeners()
    }

    private fun setAdapter() {
        mSearchedListAdapter = SearchedListAdapter(mImageLoader, emptyList())
        gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        rv_search_results.layoutManager = gridLayoutManager
        rv_search_results.setHasFixedSize(true)
        rv_search_results.adapter = mSearchedListAdapter
    }

    private fun setListeners() {
        ib_search.setOnClickListener {
            progress_circular.visibility = View.VISIBLE
            mSearchedListAdapter.clearView()
            et_search_text.clearFocus()
            isLoading = true
            mMainActivityController?.getPhotos(et_search_text.text.toString(), 1)
        }

        et_search_text.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val mInputManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                mInputManager.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }

        rv_search_results.addOnScrollListener(object : PaginationListener(gridLayoutManager) {
            override fun loadMoreItems() {
                println("Call Api")
                mMainActivityController?.getPhotos(et_search_text.text.toString(), mCurrentPage+1)
            }
            override val isLastPage: Boolean
                get() = this@MainActivity.isLastPage
            override val isLoading: Boolean
                get() = this@MainActivity.isLoading
        })
    }


    fun updateSearchRecyclerView(mPhotoPerPage: PhotoPerPage) {
        isLoading = false
        isLastPage = mPhotoPerPage.pageNumber == mPhotoPerPage.totalPages
        progress_circular.visibility = View.GONE
        mCurrentPage = mPhotoPerPage.pageNumber
        mSearchedListAdapter.updateView(mPhotoPerPage.photoList)
    }

    fun onServiceFailed(message: String) {
        progress_circular.visibility = View.GONE
        isLoading = false
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onStop() {
        super.onStop()
        mMainActivityController?.destroy()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMainActivityController = null
    }


}
