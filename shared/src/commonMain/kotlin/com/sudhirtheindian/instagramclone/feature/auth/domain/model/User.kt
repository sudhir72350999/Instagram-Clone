package com.sudhirtheindian.instagramclone.feature.auth.domain.model

data class User(
    val id: String,
    val email: String?,
    val username: String?,
    val fullName: String?,
    val profileImageUrl: String?,
    val bio: String?,
    val website: String?
)
