package com.sudhirtheindian.instagramclone.feature.home.data.mapper

import com.sudhirtheindian.instagramclone.db.GetAllPosts
import com.sudhirtheindian.instagramclone.db.GetAllStories
import com.sudhirtheindian.instagramclone.feature.home.domain.model.Post
import com.sudhirtheindian.instagramclone.feature.home.domain.model.Story

fun GetAllPosts.toDomain(): Post = Post(
    id = id,
    userId = userId,
    username = username,
    userProfileImageUrl = profileImageUrl,
    imageUrl = imageUrl,
    caption = caption,
    likeCount = likeCount.toInt(),
    commentCount = commentCount.toInt(),
    isLiked = isLiked ?: false,
    isSaved = false, // Add saved field to DB if needed
    createdAt = createdAt
)

fun GetAllStories.toDomain(): Story = Story(
    id = id,
    userId = userId,
    username = username,
    userProfileImageUrl = profileImageUrl,
    imageUrl = imageUrl,
    isSeen = isSeen ?: false,
    createdAt = createdAt
)
