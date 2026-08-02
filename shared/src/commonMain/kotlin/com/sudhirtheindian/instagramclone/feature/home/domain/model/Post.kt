package com.sudhirtheindian.instagramclone.feature.home.domain.model

data class Post(
    val id: String,
    val userId: String,
    val username: String,
    val userProfileImageUrl: String?,
    val imageUrl: String,
    val caption: String?,
    val likeCount: Int,
    val commentCount: Int,
    val repostCount: Int = 0,
    val isLiked: Boolean,
    val isSaved: Boolean,
    val isVerified: Boolean = false,
    val lastLikedBy: String? = null,
    val createdAt: Long
)
