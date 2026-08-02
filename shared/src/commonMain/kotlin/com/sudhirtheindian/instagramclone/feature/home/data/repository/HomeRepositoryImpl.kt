package com.sudhirtheindian.instagramclone.feature.home.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.sudhirtheindian.instagramclone.db.InstagramDatabase
import com.sudhirtheindian.instagramclone.feature.home.data.mapper.toDomain
import com.sudhirtheindian.instagramclone.feature.home.domain.model.Post
import com.sudhirtheindian.instagramclone.feature.home.domain.model.Story
import com.sudhirtheindian.instagramclone.feature.home.domain.repository.HomeRepository
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HomeRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val database: InstagramDatabase
) : HomeRepository {

    override fun getFeedPosts(): Flow<List<Post>> {
        return database.instagramDatabaseQueries.getAllPosts().asFlow().mapToList(Dispatchers.Main).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getStories(): Flow<List<Story>> {
        return database.instagramDatabaseQueries.getAllStories().asFlow().mapToList(Dispatchers.Main).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshFeed(): Result<Unit> {
        return try {
            // In a real app, we'd fetch from firestore and update the local database
            // val remotePosts = firestore.collection("posts").get().documents
            // update local database...
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun likePost(postId: String, isLiked: Boolean): Result<Unit> {
        // Implement Firestore like logic
        return Result.success(Unit)
    }

    override suspend fun savePost(postId: String, isSaved: Boolean): Result<Unit> {
        // Implement Firestore save logic
        return Result.success(Unit)
    }
}

// Extension to map SQLDelight entities to Domain models is now handled by PostMapper.kt
