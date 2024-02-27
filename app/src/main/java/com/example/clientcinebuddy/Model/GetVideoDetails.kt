package com.example.clientcinebuddy.Model

data class GetVideoDetails(
    var video_slide: String = "",
    var video_type: String = "",
    var video_thumb: String = "",
    var video_url: String = "",
    var video_name: String = "",
    var video_description: String = "",
    var video_category: String = ""
)