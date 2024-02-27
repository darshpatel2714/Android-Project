package com.example.clientcinebuddy.Model

import android.widget.ImageView

interface MovieItemClickListenerNew {
    fun onMovieClick(getVideoDetails: GetVideoDetails, imageView: ImageView)
}