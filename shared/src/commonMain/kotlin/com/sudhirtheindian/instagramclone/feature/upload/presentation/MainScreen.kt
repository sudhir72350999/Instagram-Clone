package com.sudhirtheindian.instagramclone.feature.upload.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.sudhirtheindian.instagramclone.feature.home.presentation.home.HomeScreen
import com.sudhirtheindian.instagramclone.feature.profile.presentation.ProfileScreen
import com.sudhirtheindian.instagramclone.feature.reels.presentation.ReelsScreen
import com.sudhirtheindian.instagramclone.feature.search.presentation.SearchScreen

class MainScreen : Screen {
    @Composable
    override fun Content() {
        var selectedTab by remember { mutableIntStateOf(0) }
        val navigator = LocalNavigator.currentOrThrow

        val homeScreen = remember { HomeScreen() }
        val searchScreen = remember { SearchScreen() }
        val reelsScreen = remember { ReelsScreen() }
        val profileScreen = remember { ProfileScreen() }

        Scaffold(
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = { Icon(if (selectedTab == 0) Icons.Filled.Home else Icons.Outlined.Home, contentDescription = "Home") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(if (selectedTab == 1) Icons.Filled.Search else Icons.Outlined.Search, contentDescription = "Search") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { 
                            navigator.push(CreatePostScreen())
                        },
                        icon = { Icon(Icons.Outlined.AddBox, contentDescription = "Create") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        icon = { Icon(if (selectedTab == 3) Icons.Filled.VideoLibrary else Icons.Outlined.VideoLibrary, contentDescription = "Reels") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 4,
                        onClick = { selectedTab = 4 },
                        icon = { Icon(if (selectedTab == 4) Icons.Filled.Person else Icons.Outlined.Person, contentDescription = "Profile") }
                    )
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                when (selectedTab) {
                    0 -> homeScreen.Content()
                    1 -> searchScreen.Content()
                    3 -> reelsScreen.Content()
                    4 -> profileScreen.Content()
                }
            }
        }
    }
}
