package com.sudhirtheindian.instagramclone.feature.reels.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import com.sudhirtheindian.instagramclone.feature.reels.domain.model.Reel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ReelsUiState(
    val reels: List<Reel> = emptyList(),
    val isLoading: Boolean = false
)

class ReelsViewModel : ScreenModel {
    private val _uiState = MutableStateFlow(ReelsUiState())
    val uiState: StateFlow<ReelsUiState> = _uiState.asStateFlow()

    init {
        loadReels()
    }

    private fun loadReels() {
        // Mock data for Reels with real, working sample videos
        val mockReels = listOf(
            Reel(
                id = "1",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                username = "sudhir_prajapati",
                userProfileImageUrl = "https://picsum.photos/200",
                caption = "Beautiful sunset! #nature #reels",
                musicName = "Nature Sounds - Original Audio",
                likeCount = 1200,
                commentCount = 45
            ),
            Reel(
                id = "2",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                username = "android_dev",
                userProfileImageUrl = "https://picsum.photos/201",
                caption = "Coding my first KMM app 🚀",
                musicName = "Lofi Hip Hop - Chill Beats",
                likeCount = 850,
                commentCount = 20
            ),
            Reel(
                id = "3",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
                username = "travel_diaries",
                userProfileImageUrl = "https://picsum.photos/202",
                caption = "Exploring new places ✨ #wanderlust",
                musicName = "Travel Vibes - Acoustic",
                likeCount = 3400,
                commentCount = 112
            )
        )
        _uiState.value = ReelsUiState(reels = mockReels)
    }

    fun toggleLike(reelId: String) {
        _uiState.value = _uiState.value.copy(
            reels = _uiState.value.reels.map {
                if (it.id == reelId) it.copy(isLiked = !it.isLiked) else it
            }
        )
    }
}
