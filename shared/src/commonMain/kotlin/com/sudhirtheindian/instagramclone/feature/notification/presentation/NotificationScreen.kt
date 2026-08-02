package com.sudhirtheindian.instagramclone.feature.notification.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.sudhirtheindian.instagramclone.feature.notification.domain.model.Notification
import com.sudhirtheindian.instagramclone.feature.notification.domain.model.NotificationType
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

class NotificationScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<NotificationViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Notifications",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                uiState.notifications.forEach { (group, notifications) ->
                    item {
                        Text(
                            text = group,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(notifications) { notification ->
                        NotificationCard(notification)
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationCard(notification: Notification) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User Avatar
            KamelImage(
                resource = { asyncPainterResource(notification.userAvatar) },
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Notification Text
            val annotatedText = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(notification.username)
                }
                append(" ")
                val actionText = when (notification.type) {
                    NotificationType.LIKE -> "liked your photo."
                    NotificationType.COMMENT -> "commented: ${notification.content}"
                    NotificationType.FOLLOW -> "started following you."
                    NotificationType.MENTION -> "mentioned you in a comment: ${notification.content}"
                }
                append(actionText)
                append(" ")
                withStyle(style = SpanStyle(color = Color.Gray, fontSize = 12.sp)) {
                    append(notification.timestamp)
                }
            }

            Text(
                text = annotatedText,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Right side action (Post thumbnail or Follow button)
            when (notification.type) {
                NotificationType.LIKE, NotificationType.COMMENT, NotificationType.MENTION -> {
                    notification.postImage?.let {
                        KamelImage(
                            resource = { asyncPainterResource(it) },
                            contentDescription = null,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                NotificationType.FOLLOW -> {
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        modifier = Modifier.height(32.dp),
                        colors = if (notification.isFollowing) {
                            ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer)
                        } else {
                            ButtonDefaults.buttonColors()
                        }
                    ) {
                        Text(
                            if (notification.isFollowing) "Following" else "Follow",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
