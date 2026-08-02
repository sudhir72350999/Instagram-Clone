package com.sudhirtheindian.instagramclone.feature.home.domain.repository

import com.sudhirtheindian.instagramclone.feature.home.domain.model.Post
import com.sudhirtheindian.instagramclone.feature.home.domain.model.Story
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getFeedPosts(): Flow<List<Post>>
    fun getStories(): Flow<List<Story>>
    suspend fun refreshFeed(): Result<Unit>
    suspend fun likePost(postId: String, isLiked: Boolean): Result<Unit>
    suspend fun savePost(postId: String, isSaved: Boolean): Result<Unit>
}
