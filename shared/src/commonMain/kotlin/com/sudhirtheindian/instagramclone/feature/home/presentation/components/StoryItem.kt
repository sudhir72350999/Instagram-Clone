package com.sudhirtheindian.instagramclone.feature.home.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sudhirtheindian.instagramclone.feature.home.domain.model.Story
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun StoryItem(
    story: Story,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .width(85.dp)
            .padding(4.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp),
            contentAlignment = Alignment.Center
        ) {
            // Instagram Gradient Ring
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 2.5.dp.toPx()
                drawCircle(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFF9CE34), // Yellow
                            Color(0xFFEE2A7B), // Pink
                            Color(0xFF6228D7)  // Purple
                        )
                    ),
                    radius = size.minDimension / 2 - strokeWidth / 2,
                    style = Stroke(width = strokeWidth)
                )
            }
            
            KamelImage(
                resource = asyncPainterResource(story.userProfileImageUrl ?: ""),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(3.dp, Color.White, CircleShape), // White gap
                contentScale = ContentScale.Crop
            )
        }
        
        Spacer(modifier = Modifier.height(2.dp))
        
        Text(
            text = story.username,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp)
        )
    }
}
