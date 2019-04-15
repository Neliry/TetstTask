package com.example.testtask.viewmodels

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.net.ConnectivityManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testtask.R
import com.example.testtask.activities.YoutubePlayerActivity
import com.example.testtask.adapters.PlayListAdapter
import com.example.testtask.entities.VideoEntity
import com.example.testtask.model.PlaylistResponse
import com.example.testtask.repository.Repository
import com.example.testtask.utils.OnViewClickListener
import com.neliry.banancheg.videonotes.viewmodels.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.graphics.drawable.BitmapDrawable

class PlayListViewModel(application: Application): BaseViewModel(application), OnViewClickListener {

    private val repository: Repository = Repository(getApplication())
    lateinit var adapter: PlayListAdapter
    var isClicked = false

    internal val currentPlaylist: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    private val videoId : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    private val playList :MutableLiveData<List<VideoEntity>>

    init {
        playList = repository.getSavedVideo()
        currentPlaylist.value = 0
    }

    override fun onViewClicked(view: View?, item: VideoEntity, image: ImageView, connMgr: ConnectivityManager)  {
        val networkInfo = connMgr.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            val bitmap = (image.drawable as BitmapDrawable).bitmap
            repository.saveClickedVideo(item, bitmap, getApplication())
            isClicked = true
            videoId.value = item.videoId
        }
    }

    fun getItems(): LiveData<List<VideoEntity>> {
        return playList
    }

    fun getVideoId(): LiveData<String>{
        return videoId
    }

    fun getPlaylist(): LiveData<Int>{
        return currentPlaylist
    }

    fun updateAdapter(playlist_recycler_view: RecyclerView, no_item_text: TextView, items : List<VideoEntity>?, load_progress: ProgressBar,  connMgr: ConnectivityManager){
        load_progress.visibility = View.GONE
        if (items != null){
            if(items.isEmpty()){
                no_item_text.visibility = View.VISIBLE
            }
            else{
                no_item_text.visibility = View.GONE
            }
            val networkInfo = connMgr.activeNetworkInfo
            adapter.isNetworck = networkInfo != null && networkInfo.isConnected
            adapter.setVideos(items)
            playlist_recycler_view.adapter = adapter
        }
    }

    fun menuItemSelected(menuItem: MenuItem, drawerLayout: androidx.drawerlayout.widget.DrawerLayout){
        menuItem.isChecked = true
        drawerLayout.closeDrawers()
        var menuItemID = 0
        when (menuItem.itemId){
            R.id.nav_item_1 -> {menuItemID = 0}
            R.id.nav_item_2 -> {menuItemID = 1}
            R.id.nav_item_3 -> {menuItemID = 2}
        }
        currentPlaylist.value = menuItemID
    }

    fun networkConnectionChange(playListsId:  List<String>, connMgr: ConnectivityManager, load_progress: ProgressBar, no_item_text: TextView){
        if (currentPlaylist.value != null)
            loadPlaylist(playListsId[currentPlaylist.value!!], connMgr, load_progress, no_item_text)
    }

    fun startPlayer(context: Context, itemId: String){
        if(isClicked){
            val intent = Intent(context, YoutubePlayerActivity::class.java)
            intent.putExtra("videoId", itemId)
            context.startActivity(intent)
            isClicked = false
        }
    }

    fun loadPlaylist(playlistId: String, connMgr: ConnectivityManager, load_progress: ProgressBar, no_item_text: TextView) {
        no_item_text.visibility = View.GONE
        load_progress.visibility = View.VISIBLE
        val networkInfo = connMgr.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            repository.getPlayList(playlistId).enqueue(object : Callback<PlaylistResponse> {
                override fun onResponse(
                    call: Call<PlaylistResponse>,
                    response: Response<PlaylistResponse>
                ) {
                    if (response.body() != null){
                        val playlistItems = arrayListOf<VideoEntity>()
                        for (item in response.body()!!.items){
                            val video= VideoEntity(item.snippet.title, item.snippet.description, item.snippet.thumbnail.standardThumbnail.thumbnailUrl, item.snippet.resourceId.videoId ,playlistId)
                            playlistItems.add(video)
                        }
                        playList.value = playlistItems
                    }
                }

                override fun onFailure(call: Call<PlaylistResponse>, t: Throwable) {
                    Log.i("myLog", "onFailure")
                }
            })
        }
        else {
            repository.loadVideosByPlaylist(playlistId)
        }
    }
}