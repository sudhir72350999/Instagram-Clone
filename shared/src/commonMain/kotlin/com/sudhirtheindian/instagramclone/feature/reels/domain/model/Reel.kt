package com.sudhirtheindian.instagramclone.feature.reels.domain.model

data class Reel(
    val id: String,
    val videoUrl: String,
    val username: String,
    val userProfileImageUrl: String?,
    val caption: String,
    val musicName: String,
    val likeCount: Int,
    val commentCount: Int,
    val isLiked: Boolean = false,
    val isFollowed: Boolean = false
)
