package com.sudhirtheindian.instagramclone.feature.profile.followers.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
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
import com.sudhirtheindian.instagramclone.feature.profile.domain.model.FollowUser
import com.sudhirtheindian.instagramclone.feature.publicprofile.presentation.PublicProfileScreen
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

data class FollowersScreen(
    val username: String,
    val isFollowers: Boolean = true
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<FollowersViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.current

        LaunchedEffect(isFollowers) {
            viewModel.loadUsers(isFollowers)
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = username,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator?.pop() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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

                // Users List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(uiState.users, key = { it.id }) { user ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(tween(500)) + slideInVertically(tween(500)),
                            exit = fadeOut(tween(300)) + slideOutVertically(tween(300))
                        ) {
                            UserItem(
                                user = user,
                                isFollowers = isFollowers,
                                onFollowToggle = { viewModel.toggleFollow(user.id) },
                                onRemove = { viewModel.removeFollower(user.id) },
                                onUserClick = { navigator?.push(PublicProfileScreen(user.id)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(
    user: FollowUser,
    isFollowers: Boolean,
    onFollowToggle: () -> Unit,
    onRemove: () -> Unit,
    onUserClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onUserClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        KamelImage(
            resource = { asyncPainterResource(user.profileImageUrl) },
            contentDescription = null,
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.username,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = user.fullName,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }

        if (isFollowers) {
            Button(
                onClick = onRemove,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                contentPadding = PaddingValues(horizontal = 12.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text("Remove", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        } else {
            Button(
                onClick = onFollowToggle,
                shape = RoundedCornerShape(8.dp),
                colors = if (user.isFollowing) {
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                } else {
                    ButtonDefaults.buttonColors()
                },
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text(
                    text = if (user.isFollowing) "Following" else "Follow",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
