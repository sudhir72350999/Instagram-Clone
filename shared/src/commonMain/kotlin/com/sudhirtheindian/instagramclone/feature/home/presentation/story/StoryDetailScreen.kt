package com.sudhirtheindian.instagramclone.feature.home.presentation.story

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.sudhirtheindian.instagramclone.core.ui.navigation.InstagramScreen
import com.sudhirtheindian.instagramclone.feature.home.domain.model.Story
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.delay

data class StoryDetailScreen(
    val stories: List<Story>,
    val initialStoryIndex: Int
) : InstagramScreen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var currentIndex by remember { mutableStateOf(initialStoryIndex) }
        val currentStory = stories[currentIndex]
        
        var progress by remember(currentIndex) { mutableStateOf(0f) }

        LaunchedEffect(currentIndex) {
            while (progress < 1f) {
                delay(50) // Adjust for speed
                progress += 0.01f
            }
            if (currentIndex < stories.size - 1) {
                currentIndex++
            } else {
                navigator.pop()
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            if (offset.x < size.width / 3) {
                                if (currentIndex > 0) currentIndex--
                                else navigator.pop()
                            } else {
                                if (currentIndex < stories.size - 1) currentIndex++
                                else navigator.pop()
                            }
                        }
                    )
                }
        ) {
            // Story Image
            KamelImage(
                resource = asyncPainterResource(currentStory.imageUrl),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Top Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 8.dp, end = 8.dp)
            ) {
                // Progress Bars
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    stories.forEachIndexed { index, _ ->
                        LinearProgressIndicator(
                            progress = { 
                                when {
                                    index < currentIndex -> 1f
                                    index == currentIndex -> progress
                                    else -> 0f
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(2.dp)
                                .clip(CircleShape),
                            color = Color.White,
                            trackColor = Color.White.copy(alpha = 0.3f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // User Info
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    KamelImage(
                        resource = asyncPainterResource(currentStory.userProfileImageUrl ?: ""),
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = currentStory.username,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { navigator.pop() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}
