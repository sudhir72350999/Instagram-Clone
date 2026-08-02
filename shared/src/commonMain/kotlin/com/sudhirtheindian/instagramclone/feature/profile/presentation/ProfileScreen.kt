package com.sudhirtheindian.instagramclone.feature.profile.presentation

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.sudhirtheindian.instagramclone.feature.profile.editprofile.presentation.EditProfileScreen
import com.sudhirtheindian.instagramclone.feature.profile.followers.presentation.FollowersScreen
import com.sudhirtheindian.instagramclone.feature.settings.presentation.SettingsScreen
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

class ProfileScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<ProfileViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            uiState.user?.username ?: "",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    actions = {
                        val navigator = LocalNavigator.current
                        IconButton(onClick = { navigator?.push(SettingsScreen()) }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                ProfileHeader(uiState)

                ProfileBio(uiState)

                ProfileActions(uiState.user)

                HighlightsSection(uiState.highlights)

                ProfileTabs()

                PostsGrid(uiState.posts)
            }
        }
    }
}

@Composable
fun ProfileHeader(uiState: ProfileUiState) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Animated Avatar
        Box(
            modifier = Modifier
                .size(90.dp)
                .scale(scale)
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFF58529), Color(0xFFDD2A7B), Color(0xFF8134AF), Color(0xFF515BD4))
                    ),
                    shape = CircleShape
                )
                .padding(4.dp)
        ) {
            KamelImage(
                resource = { asyncPainterResource(uiState.user?.profileImageUrl ?: "") },
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val navigator = LocalNavigator.current
            StatItem(count = uiState.postCount.toString(), label = "Posts")
            StatItem(
                count = uiState.followerCount.toString(),
                label = "Followers",
                onClick = { navigator?.push(FollowersScreen(uiState.user?.username ?: "", true)) }
            )
            StatItem(
                count = uiState.followingCount.toString(),
                label = "Following",
                onClick = { navigator?.push(FollowersScreen(uiState.user?.username ?: "", false)) }
            )
        }
    }
}

@Composable
fun StatItem(count: String, label: String, onClick: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(text = count, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun ProfileBio(uiState: ProfileUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = uiState.user?.fullName ?: "", fontWeight = FontWeight.Bold)
        Text(text = uiState.user?.bio ?: "", fontSize = 14.sp)
        uiState.user?.website?.let {
            Text(text = it, fontSize = 14.sp, color = Color(0xFF00376B))
        }
    }
}

@Composable
fun ProfileActions(user: com.sudhirtheindian.instagramclone.feature.auth.domain.model.User?) {
    val navigator = LocalNavigator.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = { navigator?.push(EditProfileScreen(user)) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer)
        ) {
            Text("Edit Profile", fontWeight = FontWeight.Bold)
        }
        Button(
            onClick = {},
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer)
        ) {
            Text("Share Profile", fontWeight = FontWeight.Bold)
        }
        IconButton(
            onClick = {},
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(8.dp))
                .size(40.dp)
        ) {
            Icon(Icons.Default.PersonAdd, contentDescription = "Discover People")
        }
    }
}

@Composable
fun HighlightsSection(highlights: List<Highlight>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(vertical = 12.dp)
    ) {
        items(highlights) { highlight ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .border(1.dp, Color.LightGray, CircleShape)
                        .padding(3.dp)
                ) {
                    KamelImage(
                        resource = { asyncPainterResource(highlight.coverUrl) },
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = highlight.title, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun ProfileTabs() {
    var selectedTab by remember { mutableStateOf(0) }
    TabRow(
        selectedTabIndex = selectedTab,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurface,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    ) {
        Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
            Icon(Icons.Default.GridOn, contentDescription = "Posts", modifier = Modifier.padding(12.dp))
        }
        Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
            Icon(Icons.Default.VideoLibrary, contentDescription = "Reels", modifier = Modifier.padding(12.dp))
        }
    }
}

@Composable
fun PostsGrid(posts: List<ProfilePost>) {
    val rows = posts.chunked(3)
    Column(modifier = Modifier.fillMaxWidth().padding(top = 2.dp)) {
        rows.forEach { rowPosts ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                rowPosts.forEach { post ->
                    Box(modifier = Modifier.weight(1f).aspectRatio(1f)) {
                        KamelImage(
                            resource = { asyncPainterResource(post.imageUrl) },
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                if (rowPosts.size < 3) {
                    repeat(3 - rowPosts.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}