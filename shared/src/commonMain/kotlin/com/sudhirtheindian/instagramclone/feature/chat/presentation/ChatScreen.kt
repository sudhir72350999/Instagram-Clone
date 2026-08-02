package com.sudhirtheindian.instagramclone.feature.chat.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.sudhirtheindian.instagramclone.feature.chat.domain.model.Message
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

data class ChatScreen(
    val partnerName: String,
    val partnerAvatar: String
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<ChatViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.current
        val listState = rememberLazyListState()

        LaunchedEffect(partnerName) {
            viewModel.initChat(partnerName, partnerAvatar)
        }

        // Scroll to bottom when new message arrives
        LaunchedEffect(uiState.messages.size) {
            if (uiState.messages.isNotEmpty()) {
                listState.animateScrollToItem(uiState.messages.size - 1)
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            KamelImage(
                                resource = { asyncPainterResource(partnerAvatar) },
                                contentDescription = null,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    partnerName,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                )
                                Text("Active now", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator?.pop() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            },
            bottomBar = {
                ChatInput(
                    text = uiState.inputText,
                    onTextChange = { viewModel.onInputChange(it) },
                    onSend = { viewModel.sendMessage() },
                    onImageClick = { viewModel.sendImage("https://picsum.photos/400/300?random=${(100..999).random()}") }
                )
            }
        ) { padding ->
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.messages) { message ->
                    MessageBubble(message)
                }
                
                if (uiState.isTyping) {
                    item {
                        Text(
                            "$partnerName is typing...",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(start = 8.dp),
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message) {
    val alignment = if (message.isMe) Alignment.CenterEnd else Alignment.CenterStart
    val bgColor = if (message.isMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (message.isMe) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    val shape = if (message.isMe) {
        RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp)
    } else {
        RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp)
    }

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = alignment) {
        Column(horizontalAlignment = if (message.isMe) Alignment.End else Alignment.Start) {
            Surface(
                color = bgColor,
                shape = shape,
                modifier = Modifier.widthIn(max = 280.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                    if (message.imageUrl != null) {
                        KamelImage(
                            resource = { asyncPainterResource(message.imageUrl) },
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                        if (message.text != null) Spacer(modifier = Modifier.height(8.dp))
                    }
                    if (message.text != null) {
                        Text(text = message.text, color = textColor, fontSize = 15.sp)
                    }
                }
            }
            Text(
                text = message.timestamp,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp, start = 4.dp, end = 4.dp)
            )
        }
    }
}

@Composable
fun ChatInput(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    onImageClick: () -> Unit
) {
    Surface(
        tonalElevation = 2.dp,
        modifier = Modifier.imePadding()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(36.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = "Camera", tint = Color.White, modifier = Modifier.size(20.dp))
            }
            
            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (text.isEmpty()) {
                            Text("Message...", color = Color.Gray, fontSize = 15.sp)
                        }
                        BasicTextField(
                            value = text,
                            onValueChange = onTextChange,
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
                        )
                    }
                    
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.SentimentSatisfiedAlt, contentDescription = "Emoji")
                    }
                }
            }

            if (text.isNotEmpty()) {
                TextButton(onClick = onSend) {
                    Text("Send", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            } else {
                Row {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Mic, contentDescription = "Voice")
                    }
                    IconButton(onClick = onImageClick) {
                        Icon(Icons.Default.Image, contentDescription = "Gallery")
                    }
                }
            }
        }
    }
}
