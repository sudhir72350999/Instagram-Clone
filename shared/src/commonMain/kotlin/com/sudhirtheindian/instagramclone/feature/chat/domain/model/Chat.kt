package com.sudhirtheindian.instagramclone.feature.chat.domain.model

data class Chat(
    val id: String,
    val username: String,
    val userAvatar: String,
    val lastMessage: String,
    val timestamp: String,
    val isUnread: Boolean = false,
    val isOnline: Boolean = false,
    val unreadCount: Int = 0
)
