package com.sudhirtheindian.instagramclone.feature.profile.domain.model

data class FollowUser(
    val id: String,
    val username: String,
    val fullName: String,
    val profileImageUrl: String,
    val isFollowing: Boolean = false,
    val isMe: Boolean = false
)
