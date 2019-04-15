package com.example.testtask.activities

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import com.example.testtask.R
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import kotlinx.android.synthetic.main.activity_youtube_player.*

class YoutubePlayerActivity : AppCompatActivity() , YouTubePlayer.OnInitializedListener {

    val apiKey = "AIzaSyCSCXmD56eUWosTVtH1oMxsKS1TIYvJDnU"
    lateinit var videoId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_player)

        val getIntent = intent
        videoId = getIntent.getStringExtra("videoId")

        setSupportActionBar(player_toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        val frag = supportFragmentManager.findFragmentById(com.example.testtask.R.id.youtube_fragment) as YouTubePlayerSupportFragment?
        frag!!.initialize(apiKey, this)
    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {

    }

    override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, youTubePlayer: YouTubePlayer?, p2: Boolean) {
        youTubePlayer!!.loadVideo(videoId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId === android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
