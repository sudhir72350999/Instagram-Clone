package com.sudhirtheindian.instagramclone.feature.home.presentation.story

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class StoryDetailUiState(
    val currentStoryIndex: Int = 0,
    val isPaused: Boolean = false,
    val progress: Float = 0f
)

class StoryDetailViewModel(
    val stories: List<com.sudhirtheindian.instagramclone.feature.home.domain.model.Story>,
    initialIndex: Int
) : ScreenModel {
    private val _uiState = MutableStateFlow(StoryDetailUiState(currentStoryIndex = initialIndex))
    val uiState: StateFlow<StoryDetailUiState> = _uiState.asStateFlow()

    fun nextStory() {
        if (_uiState.value.currentStoryIndex < stories.size - 1) {
            _uiState.value = _uiState.value.copy(
                currentStoryIndex = _uiState.value.currentStoryIndex + 1,
                progress = 0f
            )
        }
    }

    fun previousStory() {
        if (_uiState.value.currentStoryIndex > 0) {
            _uiState.value = _uiState.value.copy(
                currentStoryIndex = _uiState.value.currentStoryIndex - 1,
                progress = 0f
            )
        }
    }
}
