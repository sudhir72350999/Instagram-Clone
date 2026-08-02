package com.sudhirtheindian.instagramclone.feature.notification.domain.model

enum class NotificationType {
    LIKE,
    COMMENT,
    FOLLOW,
    MENTION
}

data class Notification(
    val id: String,
    val type: NotificationType,
    val username: String,
    val userAvatar: String,
    val content: String? = null,
    val postImage: String? = null,
    val timestamp: String,
    val isFollowing: Boolean = false
)
