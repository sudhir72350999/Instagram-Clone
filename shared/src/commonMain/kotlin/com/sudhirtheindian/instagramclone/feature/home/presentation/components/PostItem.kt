package com.sudhirtheindian.instagramclone.feature.home.presentation.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sudhirtheindian.instagramclone.feature.home.domain.model.Post
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PostItem(
    post: Post,
    modifier: Modifier = Modifier,
    onLikeClick: () -> Unit = {},
    onCommentClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    onUserClick: (String) -> Unit = {}
) {
    var showHeartAnimation by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            KamelImage(
                resource = asyncPainterResource(post.userProfileImageUrl ?: ""),
                contentDescription = null,
                modifier = Modifier.size(32.dp).clip(CircleShape).clickable { onUserClick(post.userId) },
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.clickable { onUserClick(post.userId) }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = post.username,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    if (post.isVerified) {
                        Spacer(modifier = Modifier.width(4.dp))
                        VerifiedBadge()
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "• 2h",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {}) {
                Icon(Icons.Default.MoreVert, contentDescription = null, modifier = Modifier.size(20.dp))
            }
        }

        // Content
        Box(contentAlignment = Alignment.Center) {
            KamelImage(
                resource = asyncPainterResource(post.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        if (!post.isLiked) {
                            onLikeClick()
                        }
                        showHeartAnimation = true
                    },
                contentScale = ContentScale.Crop
            )

            if (showHeartAnimation) {
                AnimatedHeart(onEnd = { showHeartAnimation = false })
            }
        }

        // Actions
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedLikeButton(
                isLiked = post.isLiked,
                onClick = onLikeClick
            )

            IconButton(onClick = onCommentClick) {
                Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = "Comment", modifier = Modifier.size(26.dp))
            }

            IconButton(onClick = onShareClick) {
                Icon(Icons.Default.Cached, contentDescription = "Repost", modifier = Modifier.size(26.dp))
            }

            IconButton(onClick = onShareClick) {
                Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = "Send", modifier = Modifier.size(26.dp))
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = onSaveClick) {
                Icon(
                    imageVector = if (post.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Save",
                    modifier = Modifier.size(26.dp),
                    tint = if (post.isSaved) Color.Black else LocalContentColor.current
                )
            }
        }

        // Likes summary
        val likedByText = buildAnnotatedString {
            append("Liked by ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(post.lastLikedBy ?: "sairakshith28")
            }
            append(" and ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("others")
            }
        }
        Text(
            text = likedByText,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        // Caption
        if (!post.caption.isNullOrBlank()) {
            val captionText = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(post.username)
                }
                if (post.isVerified) {
                    append(" ")
                    // We can't easily put a Composable inside buildAnnotatedString without InlineTextContent
                    // For now just skipping the badge in caption or adding a symbol
                }
                append(" ")
                append(post.caption)
            }
            Text(
                text = captionText,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }

        // Comment count
        if (post.commentCount > 0) {
            Text(
                text = "View all ${post.commentCount} comments",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp).clickable { onCommentClick() }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun VerifiedBadge(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Default.CheckCircle,
        contentDescription = "Verified",
        modifier = modifier.size(14.dp),
        tint = Color(0xFF0095F6) // Instagram Blue
    )
}

@Composable
fun AnimatedLikeButton(
    isLiked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = updateTransition(targetState = isLiked, label = "LikeTransition")

    val scale by transition.animateFloat(
        transitionSpec = {
            if (false isTransitioningTo true) {
                spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
            } else {
                spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium)
            }
        },
        label = "Scale"
    ) { state ->
        if (state) 1.2f else 1f
    }

    IconButton(
        onClick = onClick,
        modifier = modifier.size(48.dp)
    ) {
        Icon(
            imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Like",
            modifier = Modifier.size((24 * scale).dp),
            tint = if (isLiked) Color.Red else LocalContentColor.current
        )
    }
}

@Composable
fun AnimatedHeart(onEnd: () -> Unit) {
    val scale = remember { androidx.compose.animation.core.Animatable(0f) }
    val alpha = remember { androidx.compose.animation.core.Animatable(0f) }

    LaunchedEffect(Unit) {
        launch {
            scale.animateTo(
                targetValue = 1.2f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(100)
            )
            delay(500L)
            scale.animateTo(0f, tween(200))
        }
        launch {
            alpha.animateTo(1f, tween(200))
            delay(600L)
            alpha.animateTo(0f, tween(200))
            onEnd()
        }
    }

    Icon(
        imageVector = Icons.Default.Favorite,
        contentDescription = null,
        modifier = Modifier.size(100.dp).graphicsLayer(scaleX = scale.value, scaleY = scale.value, alpha = alpha.value),
        tint = Color.White.copy(alpha = 0.9f)
    )
}
