package com.sudhirtheindian.instagramclone.feature.chat.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import com.sudhirtheindian.instagramclone.feature.chat.domain.model.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Clock

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val inputText: String = "",
    val isTyping: Boolean = false,
    val chatPartnerName: String = "",
    val chatPartnerAvatar: String = ""
)

class ChatViewModel : ScreenModel {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun initChat(name: String, avatar: String) {
        _uiState.value = _uiState.value.copy(
            chatPartnerName = name,
            chatPartnerAvatar = avatar,
            messages = listOf(
                Message("1", "other", "Hey there!", null, "10:00 AM", false),
                Message("2", "me", "Hi! How's it going?", null, "10:01 AM", true),
                Message("3", "other", "Pretty good, check out this photo!", "https://picsum.photos/400/300?random=20", "10:02 AM", false)
            )
        )
    }

    fun onInputChange(text: String) {
        _uiState.value = _uiState.value.copy(inputText = text)
    }

    fun sendMessage() {
        val currentText = _uiState.value.inputText
        if (currentText.isBlank()) return

        val newMessage = Message(
            id = Clock.System.now().toEpochMilliseconds().toString(),
            senderId = "me",
            text = currentText,
            timestamp = "Now",
            isMe = true
        )

        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages + newMessage,
            inputText = ""
        )
    }

    fun sendImage(url: String) {
        val newMessage = Message(
            id = Clock.System.now().toEpochMilliseconds().toString(),
            senderId = "me",
            imageUrl = url,
            timestamp = "Now",
            isMe = true
        )
        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages + newMessage
        )
    }
}
