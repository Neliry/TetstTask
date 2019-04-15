package com.example.testtask.adapters

import android.net.ConnectivityManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.testtask.R
import com.example.testtask.entities.VideoEntity
import com.example.testtask.utils.OnViewClickListener
import com.squareup.picasso.Picasso

class PlayListAdapter (private val onViewClickListener: OnViewClickListener, val connMgr: ConnectivityManager) : androidx.recyclerview.widget.RecyclerView.Adapter<PlayListAdapter.ViewHolder>() {

    lateinit var videoList: List<VideoEntity>
    var isNetworck = false

    inner class ViewHolder internal constructor(pageView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(pageView), View.OnClickListener {
        internal val titleView: TextView = pageView.findViewById(R.id.title_view)
        internal val descriptionView: TextView = pageView.findViewById(R.id.description_view)
        internal val thumbnailView: ImageView = pageView.findViewById(R.id.thumbnail_view)
        init {
            pageView.setOnClickListener(this)
        }
        override fun onClick(view: View) {
            onViewClickListener.onViewClicked(view, videoList[layoutPosition], thumbnailView, connMgr)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onBindViewHolder(holder: PlayListAdapter.ViewHolder, position: Int) {
        holder.titleView.text = videoList[position].videoTitle
        holder.descriptionView.text = videoList[position].videoDescription
        if (isNetworck){
            Picasso.get()
                .load(videoList[position].videoThumbnail)
                .fit()
                .into( holder.thumbnailView)
        }
        else{
            holder.thumbnailView.setImageURI(Uri.parse(videoList[position].videoThumbnail))
        }
    }

    fun setVideos(list: List<VideoEntity>) {
            this.videoList = list
            notifyDataSetChanged()
    }
}