package com.sudhirtheindian.instagramclone.feature.upload.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
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
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.sudhirtheindian.instagramclone.core.ui.navigation.InstagramScreen
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

class CreatePostScreen : InstagramScreen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<CreatePostViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        
        var caption by remember { mutableStateOf("") }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("New Post", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        TextButton(
                            onClick = { viewModel.uploadPost(caption) },
                            enabled = uiState.selectedImageUri != null && !uiState.isUploading
                        ) {
                            Text(
                                if (uiState.isUploading) "Sharing..." else "Share",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                if (uiState.isUploading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

                // Image Preview / Placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(Color.LightGray.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.selectedImageUri != null) {
                        KamelImage(
                            resource = asyncPainterResource(uiState.selectedImageUri!!),
                            contentDescription = "Selected Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.PhotoLibrary,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Select an image to share", color = Color.Gray)
                        }
                    }
                }

                // Media Options
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MediaOption(
                        icon = Icons.Default.PhotoCamera,
                        label = "Camera",
                        onClick = { /* TODO: Platform specific camera */ }
                    )
                    MediaOption(
                        icon = Icons.Default.PhotoLibrary,
                        label = "Gallery",
                        onClick = { viewModel.onImageSelected("https://picsum.photos/800/800") } // Mocking selection
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                // Caption Field
                TextField(
                    value = caption,
                    onValueChange = { caption = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                    placeholder = { Text("Write a caption...") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                
                // Success message
                if (uiState.isSuccess) {
                    LaunchedEffect(Unit) {
                        navigator.pop()
                    }
                }
            }
        }
    }
}

@Composable
fun MediaOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(28.dp))
        Text(label, fontSize = 12.sp)
    }
}
