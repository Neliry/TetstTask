package com.example.testtask.activities

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBar
import android.view.MenuItem
import com.example.testtask.R
import com.example.testtask.adapters.PlayListAdapter
import com.example.testtask.viewmodels.PlayListViewModel
import com.example.testtask.viewmodels.PlaylistDecoration
import kotlinx.android.synthetic.main.activity_playlist.*
import android.content.IntentFilter
import android.content.BroadcastReceiver

class PlayListActivity : AppCompatActivity() {

    private val playListViewModel: PlayListViewModel by lazy { ViewModelProviders.of(this, ViewModelFactory(application)).get(PlayListViewModel::class.java)}
    private lateinit var drawerLayout: androidx.drawerlayout.widget.DrawerLayout
    lateinit var connMgr : ConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        drawerLayout = findViewById(R.id.drawer_layout)
        setSupportActionBar(playlist_toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }


        nav_view.menu.getItem(0).isChecked = true

        nav_view.setNavigationItemSelectedListener { menuItem ->
            playListViewModel.menuItemSelected(menuItem, drawerLayout)
            true
        }

        playListViewModel.adapter = PlayListAdapter(playListViewModel, connMgr)
        playlist_recycler_view.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        playlist_recycler_view.addItemDecoration(
            PlaylistDecoration(
                this
            )
        )
        playListViewModel.getItems().observe(this, Observer { items ->
            playListViewModel.updateAdapter(playlist_recycler_view, no_items_text , items, load_progress, connMgr)
        })

        playListViewModel.getPlaylist().observe(this, Observer { item ->
            playListViewModel.loadPlaylist(PLAYLISTSID[item], connMgr, load_progress, no_items_text)
        })

        playListViewModel.getVideoId().observe( this, Observer {    itemId ->
            playListViewModel.startPlayer(this@PlayListActivity, itemId)
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        val PLAYLISTSID: List<String> = listOf("PLivjPDlt6ApTHMisqbFv2SmJ7x0333mFz", "PLr6UCB0GvbHB0dumVZipYgcpiSdrMD5UU", "PLr6UCB0GvbHBG3ExQFrj42KdLs5V0sQZp")
    }

    private val networkChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            playListViewModel.networkConnectionChange(PLAYLISTSID, connMgr, load_progress, no_items_text)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("outState", true)
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkChangeReceiver)
    }


}