package com.sudhirtheindian.instagramclone.feature.reels.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Send
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
import com.sudhirtheindian.instagramclone.feature.reels.domain.model.Reel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

class ReelsScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<ReelsViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val pagerState = rememberPagerState { uiState.reels.size }

        Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
            VerticalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                ReelItem(
                    reel = uiState.reels[page],
                    onLikeClick = { viewModel.toggleLike(uiState.reels[page].id) }
                )
            }
            
            // Top Bar
            Text(
                text = "Reels",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp).align(Alignment.TopStart)
            )
            
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(16.dp).align(Alignment.TopEnd)
            )
        }
    }
}

@Composable
fun ReelItem(
    reel: Reel,
    onLikeClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Placeholder for Video - Using an image for now in KMM
        KamelImage(
            resource = asyncPainterResource("https://picsum.photos/1080/1920"), // Mock video background
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Overlay for dark gradient at bottom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                        startY = 500f
                    )
                )
        )

        // Right Side Floating Icons
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 8.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ReelActionIcon(
                icon = if (reel.isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                label = reel.likeCount.toString(),
                tint = if (reel.isLiked) Color.Red else Color.White,
                onClick = onLikeClick
            )
            ReelActionIcon(icon = Icons.Outlined.ChatBubbleOutline, label = reel.commentCount.toString())
            ReelActionIcon(icon = Icons.Outlined.Send, label = "")
            IconButton(onClick = {}) {
                Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.White)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Music Disk Animation placeholder
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color.DarkGray)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.MusicNote, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }

        // Bottom Content (Username, Caption, Music)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 16.dp, end = 80.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                KamelImage(
                    resource = asyncPainterResource(reel.userProfileImageUrl ?: ""),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = reel.username, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(
                    onClick = {},
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White),
                    modifier = Modifier.height(28.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                ) {
                    Text("Follow", fontSize = 12.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(text = reel.caption, color = Color.White, fontSize = 14.sp, maxLines = 2)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.MusicNote, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = reel.musicName, color = Color.White, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun ReelActionIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: Color = Color.White,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        IconButton(onClick = onClick) {
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(28.dp))
        }
        if (label.isNotEmpty()) {
            Text(text = label, color = Color.White, fontSize = 12.sp)
        }
    }
}
