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
            is HomeUiEvent.LikePost -> {} // Implement like
        }
    }

    private fun refresh() {
        screenModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getFeedUseCase.refresh()
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
}
