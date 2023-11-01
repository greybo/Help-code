package com.example.help_code.presentation.video

import android.annotation.SuppressLint
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentVideoPlayerBinding
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

// https://developer.android.com/codelabs/exoplayer-intro?hl=ru#2
// ID's name  https://exoplayer.dev/doc/reference/com/google/android/exoplayer2/ui/StyledPlayerControlView.html
//https://stackoverflow.com/questions/33647496/custom-ui-on-exoplayer-sample  !!!
//https://stackoverflow.com/questions/72592440/custom-buttons-in-exoplayer
//https://stackoverflow.com/questions/12482203/how-to-create-custom-ui-for-android-mediacontroller
class VideoPlayerFragment :
    BaseBindingFragment<FragmentVideoPlayerBinding>(FragmentVideoPlayerBinding::inflate) {

    private val viewModel: VideoPlayerViewModel by viewModels()
    private var mMediaPlayer: ExoPlayer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("VideoPlayerFragment", "onViewCreated: ")

        initializePlayer()
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
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
//                binding.videoView.videoSurfaceView?.findViewById<ImageView>(R.id.exo_volume)
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
            lifecycleScope.launch {
                if (isPlaying) pollCurrentDuration()
                    .catch {
                        Log.e("VideoPlayerFragment", "pollCurrentDuration: ${it.message}", it)
                    }
                    .onEach {
                        Log.i("VideoPlayerFragment", "pollCurrentDuration: $it")
                    }
            }
        }

    }
    val DURATION_OFFSET = 500
    val DEFAULT_DELAY_MS: (Boolean) -> Unit = {
        Log.i("VideoPlayerFragment", "DEFAULT_DELAY_MS: $it")
    }

    private fun pollCurrentDuration() = flow {
        Log.i("VideoPlayerFragment", "pollCurrentDuration:")
        while (((player?.currentPosition ?: 0) + DURATION_OFFSET) <= (player?.duration ?: 0)) {
            emit((player?.currentPosition ?: 0) + DURATION_OFFSET)
            delay(DEFAULT_DELAY_MS)
        }
    }.conflate()

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