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
        return database.instagramDatabaseQueries.getAllPosts()
            .asFlow()
            .mapToList(Dispatchers.Main)
            .map { entities ->
                if (entities.isEmpty()) {
                    generateRandomPosts()
                } else {
                    entities.map { it.toDomain() }
                }
            }
    }

    override fun getStories(): Flow<List<Story>> {
        return database.instagramDatabaseQueries.getAllStories()
            .asFlow()
            .mapToList(Dispatchers.Main)
            .map { entities ->
                if (entities.isEmpty()) {
                    generateRandomStories()
                } else {
                    entities.map { it.toDomain() }
                }
            }
    }

    private fun generateRandomPosts(): List<Post> {
        val usernames = listOf("arvind_mera_sathi", "start_withashutosh", "techcham...", "hiteshcho...", "shradhak...")
        return List(10) { index ->
            val username = usernames.getOrElse(index % usernames.size) { "user_$index" }
            Post(
                id = "post_$index",
                userId = "user_$index",
                username = username,
                userProfileImageUrl = "https://picsum.photos/200/200?random=$index",
                imageUrl = "https://picsum.photos/600/600?random=${index + 100}",
                caption = if (index % 2 == 0) "अंकित की बजाए अगर अरविंद मेरा साथी होता तो अरमान का क्या हाल होता ?" else "Random post caption for post $index #instagram #clone",
                likeCount = (10..1000).random(),
                commentCount = (5..100).random(),
                isLiked = index % 3 == 0,
                isSaved = index % 4 == 0,
                isVerified = index % 2 == 0,
                lastLikedBy = "sairakshith28",
                createdAt = 1722612066000 + (index * 100000)
            )
        }
    }

    private fun generateRandomStories(): List<Story> {
        val usernames = listOf("shradhak...", "hiteshcho...", "techcham...", "manish_tr...", "okate.k", "akash_pr...")
        return List(8) { index ->
            val username = usernames.getOrElse(index % usernames.size) { "user_$index" }
            Story(
                id = "story_$index",
                userId = "user_$index",
                username = username,
                userProfileImageUrl = "https://picsum.photos/200/200?random=${index + 200}",
                imageUrl = "https://picsum.photos/600/1000?random=${index + 300}",
                isSeen = false,
                isVerified = index % 3 == 0,
                createdAt = 1722612066000 + (index * 50000)
            )
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
