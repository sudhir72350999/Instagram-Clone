package com.sudhirtheindian.instagramclone.feature.home.domain.usecase

import com.sudhirtheindian.instagramclone.feature.home.domain.model.Post
import com.sudhirtheindian.instagramclone.feature.home.domain.model.Story
import com.sudhirtheindian.instagramclone.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

data class HomeFeedData(
    val posts: List<Post>,
    val stories: List<Story>
)

class GetFeedUseCase(private val repository: HomeRepository) {
    operator fun invoke(): Flow<HomeFeedData> {
        return combine(
            repository.getFeedPosts(),
            repository.getStories()
        ) { posts, stories ->
            HomeFeedData(posts, stories)
        }
    }

    suspend fun refresh(): Result<Unit> {
        return repository.refreshFeed()
    }
}
