package com.example.help_code.presentation.video

import com.example.help_code.base.CompositeViewModel

class VideoPlayerViewModel : CompositeViewModel() {

    var playWhenReady: Boolean = true
    var currentItem: Int = 0
    var videoPlaying: Boolean = false
    var playbackPosition = 0L

}
