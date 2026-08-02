package com.sudhirtheindian.instagramclone.feature.chat.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import com.sudhirtheindian.instagramclone.feature.chat.domain.model.Chat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ChatListUiState(
    val chats: List<Chat> = emptyList(),
    val onlineUsers: List<Chat> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false
)

class ChatListViewModel : ScreenModel {
    private val _uiState = MutableStateFlow(ChatListUiState())
    val uiState: StateFlow<ChatListUiState> = _uiState.asStateFlow()

    init {
        loadChats()
    }

    private fun loadChats() {
        val mockChats = listOf(
            Chat("1", "johndoe", "https://i.pravatar.cc/150?u=1", "Hey, how are you?", "2m", true, true, 2),
            Chat("2", "sarah_smith", "https://i.pravatar.cc/150?u=2", "The design looks great! 🔥", "15m", false, true),
            Chat("3", "alex_k", "https://i.pravatar.cc/150?u=3", "Sent a photo", "1h", true, false, 1),
            Chat("4", "mike_ross", "https://i.pravatar.cc/150?u=4", "See you tomorrow", "3h", false, false),
            Chat("5", "emily_blunt", "https://i.pravatar.cc/150?u=5", "Shared a reel", "1d", false, true),
            Chat("6", "david_beckham", "https://i.pravatar.cc/150?u=6", "Thanks!", "2d", false, false)
        )

        _uiState.value = _uiState.value.copy(
            chats = mockChats,
            onlineUsers = mockChats.filter { it.isOnline }
        )
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        // Filter logic could be added here
    }
}
