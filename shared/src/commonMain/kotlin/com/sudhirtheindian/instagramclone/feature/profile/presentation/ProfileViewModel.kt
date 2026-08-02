package com.sudhirtheindian.instagramclone.feature.profile.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import com.sudhirtheindian.instagramclone.feature.auth.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Highlight(
    val id: String,
    val title: String,
    val coverUrl: String
)

data class ProfilePost(
    val id: String,
    val imageUrl: String
)

data class ProfileUiState(
    val user: User? = null,
    val postCount: Int = 0,
    val followerCount: Int = 0,
    val followingCount: Int = 0,
    val highlights: List<Highlight> = emptyList(),
    val posts: List<ProfilePost> = emptyList(),
    val isLoading: Boolean = false
)

class ProfileViewModel : ScreenModel {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        // Mock data for premium profile
        val mockUser = User(
            id = "1",
            email = "sudhir@example.com",
            username = "sudhir_prajapati",
            fullName = "Sudhir Prajapati",
            profileImageUrl = "https://i.pravatar.cc/300?u=sudhir",
            bio = "Android Developer | UI/UX Enthusiast | Kotlin Multiplatform",
            website = "https://github.com/sudhirtheindian"
        )

        val mockHighlights = listOf(
            Highlight("1", "Travel", "https://picsum.photos/200/200?random=10"),
            Highlight("2", "Food", "https://picsum.photos/200/200?random=11"),
            Highlight("3", "Code", "https://picsum.photos/200/200?random=12"),
            Highlight("4", "Gym", "https://picsum.photos/200/200?random=13"),
            Highlight("5", "Friends", "https://picsum.photos/200/200?random=14")
        )

        val mockPosts = List(18) { index ->
            ProfilePost(index.toString(), "https://picsum.photos/400/400?random=$index")
        }

        _uiState.value = ProfileUiState(
            user = mockUser,
            postCount = 18,
            followerCount = 1250,
            followingCount = 450,
            highlights = mockHighlights,
            posts = mockPosts
        )
    }
}
