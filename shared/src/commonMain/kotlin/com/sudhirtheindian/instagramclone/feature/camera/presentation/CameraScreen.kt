package com.sudhirtheindian.instagramclone.feature.camera.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator

class CameraScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<CameraViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.current
        
        val modes = listOf("POST", "STORY", "REEL", "LIVE")

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Placeholder for Viewfinder
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 140.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.DarkGray.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text("Camera Viewfinder Placeholder", color = Color.Gray)
            }

            // Top Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navigator?.pop() }) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }

                IconButton(onClick = { viewModel.toggleFlash() }) {
                    val flashIcon = when (uiState.flashMode) {
                        FlashMode.OFF -> Icons.Default.FlashOff
                        FlashMode.ON -> Icons.Default.FlashOn
                        FlashMode.AUTO -> Icons.Default.FlashAuto
                    }
                    Icon(flashIcon, contentDescription = "Flash", tint = Color.White)
                }

                IconButton(onClick = {}) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
                }
            }

            // Bottom Controls
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Mode Selector
                LazyRow(
                    modifier = Modifier.padding(bottom = 24.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(modes) { mode ->
                        Text(
                            text = mode,
                            color = if (uiState.selectedMode == mode) Color.White else Color.Gray,
                            fontWeight = if (uiState.selectedMode == mode) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 14.sp,
                            modifier = Modifier.clickable { viewModel.setMode(mode) }
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Gallery Shortcut
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Gray.copy(alpha = 0.5f))
                            .border(1.dp, Color.White, RoundedCornerShape(8.dp))
                    )

                    // Capture Button
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .border(5.dp, Color.White, CircleShape)
                            .padding(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color.White)
                                .clickable { /* Capture Logic */ }
                        )
                    }

                    // Camera Switch
                    IconButton(
                        onClick = { viewModel.switchCamera() },
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color.Gray.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(Icons.Default.FlipCameraIos, contentDescription = "Switch Camera", tint = Color.White)
                    }
                }
            }
        }
    }
}
