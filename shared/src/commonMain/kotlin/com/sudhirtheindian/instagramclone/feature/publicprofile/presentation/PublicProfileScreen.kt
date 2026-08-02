package com.sudhirtheindian.instagramclone.feature.publicprofile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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
import com.sudhirtheindian.instagramclone.feature.chat.presentation.ChatScreen
import com.sudhirtheindian.instagramclone.feature.profile.presentation.*
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

data class PublicProfileScreen(val userId: String) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<PublicProfileViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.current

        LaunchedEffect(userId) {
            viewModel.loadProfile(userId)
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            uiState.user?.username ?: "",
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
                            Icon(Icons.Default.MoreVert, contentDescription = "More")
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
                // Reuse components from ProfileScreen
                ProfileHeader(
                    ProfileUiState(
                        user = uiState.user,
                        postCount = uiState.postCount,
                        followerCount = uiState.followerCount,
                        followingCount = uiState.followingCount
                    )
                )

                ProfileBio(
                    ProfileUiState(user = uiState.user)
                )

                PublicProfileActions(
                    isFollowing = uiState.isFollowing,
                    onFollowClick = { viewModel.toggleFollow() },
                    onMessageClick = {
                        uiState.user?.let {
                            navigator?.push(ChatScreen(it.username ?: "", it.profileImageUrl ?: ""))
                        }
                    }
                )

                HighlightsSection(uiState.highlights)

                ProfileTabs()

                PostsGrid(uiState.posts)
            }
        }
    }
}

@Composable
fun PublicProfileActions(
    isFollowing: Boolean,
    onFollowClick: () -> Unit,
    onMessageClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onFollowClick,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp),
            colors = if (isFollowing) {
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            } else {
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            Text(if (isFollowing) "Following" else "Follow", fontWeight = FontWeight.Bold)
        }
        
        Button(
            onClick = onMessageClick,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Text("Message", fontWeight = FontWeight.Bold)
        }
        
        IconButton(
            onClick = {},
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(8.dp))
                .size(40.dp)
        ) {
            Icon(Icons.Default.PersonAdd, contentDescription = "Similar Accounts")
        }
    }
}
