package com.sudhirtheindian.instagramclone.feature.chat.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.sudhirtheindian.instagramclone.feature.chat.domain.model.Chat
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

class ChatListScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<ChatListViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.current

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Messages",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator?.pop() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Create, contentDescription = "New Message")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Search Bar
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.onSearchQueryChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Search") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    singleLine = true
                )

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    // Online Users Row
                    item {
                        Text(
                            "Online",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        LazyRow(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(uiState.onlineUsers) { user ->
                                OnlineUserItem(user)
                            }
                        }
                    }

                    // Chats List
                    items(uiState.chats) { chat ->
                        ChatItem(chat)
                    }
                }
            }
        }
    }
}

@Composable
fun OnlineUserItem(user: Chat) {
    val navigator = LocalNavigator.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { navigator?.push(ChatScreen(user.username, user.userAvatar)) }
    ) {
        Box {
            KamelImage(
                resource = { asyncPainterResource(user.userAvatar) },
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            // Online Green Dot
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .background(Color.Green, CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .align(Alignment.BottomEnd)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(user.username, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun ChatItem(chat: Chat) {
    val navigator = LocalNavigator.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navigator?.push(ChatScreen(chat.username, chat.userAvatar)) }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            KamelImage(
                resource = { asyncPainterResource(chat.userAvatar) },
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            if (chat.isOnline) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(Color.Green, CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
                        .align(Alignment.BottomEnd)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = chat.username,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (chat.isUnread) FontWeight.Bold else FontWeight.Normal
                )
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = chat.lastMessage,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (chat.isUnread) MaterialTheme.colorScheme.onSurface else Color.Gray,
                        fontWeight = if (chat.isUnread) FontWeight.SemiBold else FontWeight.Normal
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Text(
                    text = " • ${chat.timestamp}",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
            }
        }

        if (chat.isUnread) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            )
        }
    }
}
