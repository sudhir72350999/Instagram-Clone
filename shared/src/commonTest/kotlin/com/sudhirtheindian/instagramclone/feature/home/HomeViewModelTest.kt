package com.sudhirtheindian.instagramclone.feature.home

import com.sudhirtheindian.instagramclone.feature.home.domain.model.Post
import com.sudhirtheindian.instagramclone.feature.home.domain.model.Story
import com.sudhirtheindian.instagramclone.feature.home.domain.repository.HomeRepository
import com.sudhirtheindian.instagramclone.feature.home.domain.usecase.GetFeedUseCase
import com.sudhirtheindian.instagramclone.feature.home.presentation.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `loadFeed should update state with posts and stories`() = runTest {
        val posts = listOf(Post("1", "u1", "user1", null, "img1", "caption", 10, 5, false, false, 12345))
        val stories = listOf(Story("s1", "u1", "user1", null, "simg1", false, 12345))
        
        val repository = object : HomeRepository {
            override fun getFeedPosts(): Flow<List<Post>> = flowOf(posts)
            override fun getStories(): Flow<List<Story>> = flowOf(stories)
            override suspend fun refreshFeed(): Result<Unit> = Result.success(Unit)
            override suspend fun likePost(p: String, l: Boolean): Result<Unit> = Result.success(Unit)
            override suspend fun savePost(p: String, s: Boolean): Result<Unit> = Result.success(Unit)
        }
        
        val useCase = GetFeedUseCase(repository)
        val viewModel = HomeViewModel(useCase)

        advanceUntilIdle()

        assertEquals(posts, viewModel.uiState.value.posts)
        assertEquals(stories, viewModel.uiState.value.stories)
        assertEquals(false, viewModel.uiState.value.isLoading)
    }
}
