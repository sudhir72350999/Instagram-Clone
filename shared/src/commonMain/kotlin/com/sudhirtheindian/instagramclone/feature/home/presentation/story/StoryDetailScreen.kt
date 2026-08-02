package com.sudhirtheindian.instagramclone.feature.home.presentation.story

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import kotlinx.coroutines.launch

data class StoryDetailScreen(
    val stories: List<Story>,
    val initialStoryIndex: Int
) : InstagramScreen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val pagerState = rememberPagerState(initialPage = initialStoryIndex) { stories.size }
        val scope = rememberCoroutineScope()
        var isPaused by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = !isPaused
            ) { page ->
                val story = stories[page]
                StoryPage(
                    story = story,
                    isActive = pagerState.currentPage == page,
                    isPaused = isPaused,
                    onNext = {
                        if (pagerState.currentPage < stories.size - 1) {
                            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                        } else {
                            navigator.pop()
                        }
                    },
                    onPrevious = {
                        if (pagerState.currentPage > 0) {
                            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                        } else {
                            navigator.pop()
                        }
                    },
                    onTogglePause = { isPaused = it },
                    onClose = { navigator.pop() },
                    totalStories = stories.size,
                    currentPagerIndex = pagerState.currentPage
                )
            }
        }
    }
}

@Composable
fun StoryPage(
    story: Story,
    isActive: Boolean,
    isPaused: Boolean,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onTogglePause: (Boolean) -> Unit,
    onClose: () -> Unit,
    totalStories: Int,
    currentPagerIndex: Int
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(isActive, isPaused) {
        if (isActive) {
            if (isPaused) {
                progress.stop()
            } else {
                progress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = (5000 * (1f - progress.value)).toInt(),
                        easing = LinearEasing
                    )
                )
                onNext()
            }
        } else {
            progress.snapTo(0f)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        onTogglePause(true)
                        tryAwaitRelease()
                        onTogglePause(false)
                    },
                    onTap = { offset ->
                        if (offset.x < size.width / 3) {
                            onPrevious()
                        } else {
                            onNext()
                        }
                    }
                )
            }
    ) {
        KamelImage(
            resource = asyncPainterResource(story.imageUrl),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Overlay UI
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
                repeat(totalStories) { index ->
                    val barProgress = when {
                        index < currentPagerIndex -> 1f
                        index == currentPagerIndex -> progress.value
                        else -> 0f
                    }
                    LinearProgressIndicator(
                        progress = { barProgress },
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
                    resource = asyncPainterResource(story.userProfileImageUrl ?: ""),
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = story.username,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onClose) {
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
