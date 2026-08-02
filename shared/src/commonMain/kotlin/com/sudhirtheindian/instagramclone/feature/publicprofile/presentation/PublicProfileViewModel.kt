package com.sudhirtheindian.instagramclone.feature.publicprofile.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import com.sudhirtheindian.instagramclone.feature.auth.domain.model.User
import com.sudhirtheindian.instagramclone.feature.profile.presentation.Highlight
import com.sudhirtheindian.instagramclone.feature.profile.presentation.ProfilePost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class PublicProfileUiState(
    val user: User? = null,
    val postCount: Int = 0,
    val followerCount: Int = 0,
    val followingCount: Int = 0,
    val highlights: List<Highlight> = emptyList(),
    val posts: List<ProfilePost> = emptyList(),
    val isFollowing: Boolean = false,
    val isLoading: Boolean = false
)

class PublicProfileViewModel : ScreenModel {
    private val _uiState = MutableStateFlow(PublicProfileUiState())
    val uiState: StateFlow<PublicProfileUiState> = _uiState.asStateFlow()

    fun loadProfile(userId: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        
        // Mock data for another user
        val mockUser = User(
            id = userId,
            email = "user@example.com",
            username = "travel_junkie",
            fullName = "Alex Travel",
            profileImageUrl = "https://i.pravatar.cc/300?u=travel",
            bio = "Exploring the world one city at a time 🌍 | Photographer | Storyteller",
            website = "https://alex-travels.com"
        )

        val mockHighlights = listOf(
            Highlight("1", "Paris", "https://picsum.photos/200/200?random=21"),
            Highlight("2", "Bali", "https://picsum.photos/200/200?random=22"),
            Highlight("3", "Tokyo", "https://picsum.photos/200/200?random=23")
        )

        val mockPosts = List(15) { index ->
            ProfilePost(index.toString(), "https://picsum.photos/400/400?random=${index + 50}")
        }

        _uiState.value = PublicProfileUiState(
            user = mockUser,
            postCount = 15,
            followerCount = 8900,
            followingCount = 120,
            highlights = mockHighlights,
            posts = mockPosts,
            isFollowing = false,
            isLoading = false
        )
    }

    fun toggleFollow() {
        _uiState.value = _uiState.value.copy(
            isFollowing = !_uiState.value.isFollowing,
            followerCount = if (_uiState.value.isFollowing) _uiState.value.followerCount - 1 else _uiState.value.followerCount + 1
        )
    }
}
