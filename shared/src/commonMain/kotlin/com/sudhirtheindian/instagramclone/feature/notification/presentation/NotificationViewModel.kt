package com.sudhirtheindian.instagramclone.feature.notification.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import com.sudhirtheindian.instagramclone.feature.notification.domain.model.Notification
import com.sudhirtheindian.instagramclone.feature.notification.domain.model.NotificationType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class NotificationUiState(
    val notifications: Map<String, List<Notification>> = emptyMap()
)

class NotificationViewModel : ScreenModel {
    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        val mockNotifications = listOf(
            Notification(
                id = "1",
                type = NotificationType.LIKE,
                username = "johndoe",
                userAvatar = "https://i.pravatar.cc/150?u=1",
                postImage = "https://picsum.photos/200/200?random=1",
                timestamp = "2h"
            ),
            Notification(
                id = "2",
                type = NotificationType.FOLLOW,
                username = "sarah_smith",
                userAvatar = "https://i.pravatar.cc/150?u=2",
                timestamp = "4h",
                isFollowing = false
            ),
            Notification(
                id = "3",
                type = NotificationType.COMMENT,
                username = "alex_k",
                userAvatar = "https://i.pravatar.cc/150?u=3",
                content = "Great photo!",
                postImage = "https://picsum.photos/200/200?random=2",
                timestamp = "1d"
            ),
            Notification(
                id = "4",
                type = NotificationType.MENTION,
                username = "mike_ross",
                userAvatar = "https://i.pravatar.cc/150?u=4",
                content = "Check this out @user",
                postImage = "https://picsum.photos/200/200?random=3",
                timestamp = "1d"
            ),
            Notification(
                id = "5",
                type = NotificationType.FOLLOW,
                username = "emily_blunt",
                userAvatar = "https://i.pravatar.cc/150?u=5",
                timestamp = "2d",
                isFollowing = true
            )
        )

        val grouped = mapOf(
            "Today" to mockNotifications.filter { it.timestamp.contains("h") },
            "This Week" to mockNotifications.filter { it.timestamp.contains("d") }
        )

        _uiState.value = NotificationUiState(notifications = grouped)
    }
}
