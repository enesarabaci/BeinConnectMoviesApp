package com.example.beinconnectmoviesapp.View

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import com.example.beinconnectmoviesapp.Model.Movie
import com.example.beinconnectmoviesapp.R
import com.example.beinconnectmoviesapp.Util.Util.VIDEO_URL
import com.example.beinconnectmoviesapp.databinding.ActivityPlayerBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.coroutines.runBlocking

class PlayerActivity : AppCompatActivity(), Player.Listener {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var simpleExoPlayer: SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movie = intent.getSerializableExtra("movie") as Movie

        val toolbar = findViewById<Toolbar>(R.id.exo_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setTitle(movie.title)
            setDisplayHomeAsUpEnabled(true)
        }

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        simpleExoPlayer = SimpleExoPlayer.Builder(this).build()
        binding.exoPlayer.player = simpleExoPlayer
        val mediaItem =
            MediaItem.fromUri(VIDEO_URL)
        simpleExoPlayer.apply {
            addMediaItem(mediaItem)
            prepare()
            playWhenReady = true
            addListener(this@PlayerActivity)
        }
        simpleExoPlayer.addMediaItem(mediaItem)
        simpleExoPlayer.prepare()
        simpleExoPlayer.playWhenReady = true
    }

    override fun onStop() {
        simpleExoPlayer.pause()
        super.onStop()
    }

    override fun onBackPressed() {
        runBlocking {
            @Suppress("DEPRECATION")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.show(WindowInsets.Type.statusBars())
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
        }

        super.onBackPressed()
    }

    override fun onDestroy() {
        simpleExoPlayer.stop()
        simpleExoPlayer.clearMediaItems()

        super.onDestroy()
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        binding.exoPb.isVisible = playbackState == Player.STATE_BUFFERING

        super.onPlayerStateChanged(playWhenReady, playbackState)
    }



}