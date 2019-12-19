package com.arindom.imagesearchapp.searchimage.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arindom.imagesearchapp.R
import com.arindom.imagesearchapp.network.GenericApiListener
import com.arindom.imagesearchapp.searchimage.modals.Photo
import com.arindom.imagesearchapp.utils.ImageLoader
import kotlinx.android.synthetic.main.item_search_results.view.*

class SearchedListAdapter(private val mImageLoader: ImageLoader, photoList: List<Photo>) :
    RecyclerView.Adapter<SearchedListAdapter.SearchedViewHolder>() {
    private var mPhotoList = mutableListOf<Photo>()

    init {
        this.mPhotoList.addAll(photoList)
    }

    inner class SearchedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mPhoto: Photo) {
            itemView.tv_search_name.text = mPhoto.name
            mImageLoader.loadImageFromURL(mPhoto.url,itemView.iv_search_result,object :GenericApiListener<Boolean,String?>{
                override fun onSuccess(message: Boolean) {

                }

                override fun onFailure(message: String?) {

                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchedViewHolder {
        return SearchedViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_search_results,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mPhotoList.size
    }

    override fun onBindViewHolder(holder: SearchedViewHolder, position: Int) {
        holder.bind(mPhotoList[position])
    }

    fun updateView(photoList: List<Photo>) {
        this.mPhotoList.addAll(photoList)
        notifyDataSetChanged()
    }
    fun clearView(){
        mPhotoList.clear()
        notifyDataSetChanged()
    }
}