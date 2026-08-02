package com.sudhirtheindian.instagramclone.feature.home.presentation.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.sudhirtheindian.instagramclone.feature.home.domain.model.Post
import com.sudhirtheindian.instagramclone.feature.home.domain.model.Story
import com.sudhirtheindian.instagramclone.feature.home.domain.usecase.GetFeedUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val posts: List<Post> = emptyList(),
    val stories: List<Story> = emptyList(),
    val error: String? = null
)

sealed class HomeUiEvent {
    object Refresh : HomeUiEvent()
    data class LikePost(val postId: String) : HomeUiEvent()
    data class SavePost(val postId: String) : HomeUiEvent()
    object LoadMore : HomeUiEvent()
}

class HomeViewModel(
    private val getFeedUseCase: GetFeedUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadFeed()
    }

    private fun loadFeed() {
        screenModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getFeedUseCase().collectLatest { data ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    posts = data.posts,
                    stories = data.stories
                )
            }
        }
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.Refresh -> refresh()
            is HomeUiEvent.LikePost -> likePost(event.postId)
            is HomeUiEvent.SavePost -> savePost(event.postId)
            HomeUiEvent.LoadMore -> loadMore()
        }
    }

    private fun likePost(postId: String) {
        screenModelScope.launch {
            // Optimistic UI update
            _uiState.value = _uiState.value.copy(
                posts = _uiState.value.posts.map {
                    if (it.id == postId) {
                        val isLiked = !it.isLiked
                        it.copy(
                            isLiked = isLiked,
                            likeCount = if (isLiked) it.likeCount + 1 else it.likeCount - 1
                        )
                    } else it
                }
            )
            // Real update (mocked for now, normally would call usecase)
            // getFeedUseCase.likePost(postId, isLiked)
        }
    }

    private fun savePost(postId: String) {
        screenModelScope.launch {
            _uiState.value = _uiState.value.copy(
                posts = _uiState.value.posts.map {
                    if (it.id == postId) it.copy(isSaved = !it.isSaved) else it
                }
            )
        }
    }

    private fun loadMore() {
        if (_uiState.value.isLoading) return
        // Implement pagination logic here
    }

    private fun refresh() {
        screenModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getFeedUseCase.refresh()
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
}
