package com.sudhirtheindian.instagramclone.feature.chat.domain.model

data class Message(
    val id: String,
    val senderId: String,
    val text: String? = null,
    val imageUrl: String? = null,
    val timestamp: String,
    val isMe: Boolean
)
