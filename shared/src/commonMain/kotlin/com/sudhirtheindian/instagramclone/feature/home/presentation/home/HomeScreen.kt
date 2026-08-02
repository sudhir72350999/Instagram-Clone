package com.sudhirtheindian.instagramclone.feature.home.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.sudhirtheindian.instagramclone.core.ui.navigation.InstagramScreen
import com.sudhirtheindian.instagramclone.feature.home.presentation.components.PostItem
import com.sudhirtheindian.instagramclone.feature.home.presentation.components.StoryItem
import com.sudhirtheindian.instagramclone.feature.home.presentation.story.StoryDetailScreen
import com.sudhirtheindian.instagramclone.feature.notification.presentation.NotificationScreen
import com.sudhirtheindian.instagramclone.feature.chat.presentation.ChatListScreen
import com.sudhirtheindian.instagramclone.feature.camera.presentation.CameraScreen
import com.sudhirtheindian.instagramclone.feature.publicprofile.presentation.PublicProfileScreen

class HomeScreen : InstagramScreen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<HomeViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val listState = rememberLazyListState()

        val shouldLoadMore = remember {
            derivedStateOf {
                val totalItemsCount = listState.layoutInfo.totalItemsCount
                val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                lastVisibleItemIndex >= totalItemsCount - 5
            }
        }

        LaunchedEffect(shouldLoadMore.value) {
            if (shouldLoadMore.value) {
                viewModel.onEvent(HomeUiEvent.LoadMore)
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Instagram",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                        )
                    },
                    actions = {
                        IconButton(onClick = { navigator.push(NotificationScreen()) }) {
                            Icon(Icons.Default.FavoriteBorder, contentDescription = "Notifications")
                        }
                        IconButton(onClick = { navigator.push(ChatListScreen()) }) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Messages")
                        }
                    }
                )
            }
        ) { paddingValues ->
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize().padding(paddingValues)
            ) {
                // Stories Row
                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        itemsIndexed(uiState.stories) { index, story ->
                            StoryItem(
                                story = story,
                                onClick = {
                                    navigator.push(StoryDetailScreen(uiState.stories, index))
                                }
                            )
                        }
                    }
                    HorizontalDivider()
                }

                // Feed Posts
                items(uiState.posts, key = { it.id }) { post ->
                    PostItem(
                        post = post,
                        onLikeClick = { viewModel.onEvent(HomeUiEvent.LikePost(post.id)) },
                        onSaveClick = { viewModel.onEvent(HomeUiEvent.SavePost(post.id)) },
                        onUserClick = { userId -> navigator.push(PublicProfileScreen(userId)) }
                    )
                }

                if (uiState.isLoading) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}
