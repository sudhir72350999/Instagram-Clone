package com.sudhirtheindian.instagramclone.feature.profile.followers.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import com.sudhirtheindian.instagramclone.feature.profile.domain.model.FollowUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class FollowersUiState(
    val users: List<FollowUser> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false
)

class FollowersViewModel : ScreenModel {
    private val _uiState = MutableStateFlow(FollowersUiState())
    val uiState: StateFlow<FollowersUiState> = _uiState.asStateFlow()

    private var allUsers = listOf<FollowUser>()

    fun loadUsers(isFollowers: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        
        // Mock data
        val prefix = if (isFollowers) "follower" else "following"
        allUsers = List(20) { index ->
            FollowUser(
                id = "$index",
                username = "${prefix}_user_$index",
                fullName = "Full Name $index",
                profileImageUrl = "https://i.pravatar.cc/150?u=$prefix$index",
                isFollowing = !isFollowers || index % 2 == 0
            )
        }
        
        _uiState.value = _uiState.value.copy(
            users = allUsers,
            isLoading = false
        )
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        filterUsers(query)
    }

    private fun filterUsers(query: String) {
        val filtered = if (query.isEmpty()) {
            allUsers
        } else {
            allUsers.filter { 
                it.username.contains(query, ignoreCase = true) || 
                it.fullName.contains(query, ignoreCase = true) 
            }
        }
        _uiState.value = _uiState.value.copy(users = filtered)
    }

    fun toggleFollow(userId: String) {
        allUsers = allUsers.map {
            if (it.id == userId) it.copy(isFollowing = !it.isFollowing) else it
        }
        filterUsers(_uiState.value.searchQuery)
    }
    
    fun removeFollower(userId: String) {
        allUsers = allUsers.filter { it.id != userId }
        filterUsers(_uiState.value.searchQuery)
    }
}
