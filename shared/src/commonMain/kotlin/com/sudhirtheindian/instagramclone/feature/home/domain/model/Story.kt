package com.sudhirtheindian.instagramclone.feature.home.domain.model

data class Story(
    val id: String,
    val userId: String,
    val username: String,
    val userProfileImageUrl: String?,
    val imageUrl: String,
    val isSeen: Boolean,
    val isVerified: Boolean = false,
    val createdAt: Long
)
