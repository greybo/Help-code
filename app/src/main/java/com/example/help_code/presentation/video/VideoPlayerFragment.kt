package com.example.help_code.presentation.video

import android.annotation.SuppressLint
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.viewModels
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentVideoPlayerBinding

// https://developer.android.com/codelabs/exoplayer-intro?hl=ru#2

//https://stackoverflow.com/questions/33647496/custom-ui-on-exoplayer-sample  !!!
//https://stackoverflow.com/questions/72592440/custom-buttons-in-exoplayer
//https://stackoverflow.com/questions/12482203/how-to-create-custom-ui-for-android-mediacontroller
class VideoPlayerFragment :
    BaseBindingFragment<FragmentVideoPlayerBinding>(FragmentVideoPlayerBinding::inflate) {

    private val viewModel: VideoPlayerViewModel by viewModels()
    private var mMediaPlayer: ExoPlayer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializePlayer()
    }

    private fun initializePlayer() {
        mMediaPlayer = ExoPlayer.Builder(requireContext())
            .build()
            .also { exoPlayer ->
                binding.videoView.player = exoPlayer
                val mediaItem =
//                    MediaItem.fromUri("https://storage.googleapis.com/exoplayer-test-media-0/play.mp3")
                    MediaItem.fromUri("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.playWhenReady = viewModel.playWhenReady
                exoPlayer.seekTo(viewModel.currentItem, viewModel.playbackPosition)
                exoPlayer.prepare()
                exoPlayer.pause()
                exoPlayer.addListener(playerListener)
            }
    }

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            if (playbackState == Player.STATE_ENDED) {
                viewModel.videoPlaying = false
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            viewModel.videoPlaying = isPlaying
        }
    }

    private fun changeStatePlaying() {
        if (viewModel.videoPlaying) {
            mMediaPlayer?.play()
        } else mMediaPlayer?.pause()
    }

    override fun onStart() {
        super.onStart()
        if (SDK_INT > 23) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
//        hideSystemUi()
        changeStatePlaying()
        if ((SDK_INT <= 23 || mMediaPlayer == null)) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
//        showSystemUi()
        if (SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (SDK_INT > 23) {
            releasePlayer()
        }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        WindowInsetsControllerCompat(
            requireActivity().window,
            binding.videoView
        ).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun showSystemUi() {
        WindowInsetsControllerCompat(
            requireActivity().window,
            binding.videoView
        ).let { controller ->
            // Hide both the status bar and the navigation bar
            controller.show(WindowInsetsCompat.Type.systemBars())
            // Configure the behavior of the hidden system bars
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun releasePlayer() {
        mMediaPlayer?.let { exoPlayer ->
            viewModel.playbackPosition = exoPlayer.currentPosition
            viewModel.currentItem = exoPlayer.currentMediaItemIndex
            viewModel.playWhenReady = exoPlayer.playWhenReady
            exoPlayer.removeListener(playerListener)
            exoPlayer.release()
        }
        mMediaPlayer = null
    }
}