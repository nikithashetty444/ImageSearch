package com.arindom.imagesearchapp.searchimage.ui.adapters

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


internal abstract class PaginationListener
    (private val layoutManager: GridLayoutManager) :
    RecyclerView.OnScrollListener() {

    private var yAxix = -1
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        if (yAxix > 0 && newState == 0) {
            if (!isLoading && !isLastPage) {
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE
                ) {
                    loadMoreItems()
                }
            }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        yAxix = dy

    }

    protected abstract fun loadMoreItems()
    abstract val isLastPage: Boolean
    abstract val isLoading: Boolean

    companion object {
        const val PAGE_START = 1
        private const val PAGE_SIZE = 10
    }

}