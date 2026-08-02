package com.sudhirtheindian.instagramclone.feature.profile.editprofile.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
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
import com.sudhirtheindian.instagramclone.feature.auth.domain.model.User
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

data class EditProfileScreen(val user: User?) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<EditProfileViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.current

        LaunchedEffect(user) {
            viewModel.init(user)
        }
        
        if (uiState.isSuccess) {
            LaunchedEffect(Unit) {
                navigator?.pop()
            }
        }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Edit Profile", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)) },
                    navigationIcon = {
                        IconButton(onClick = { navigator?.pop() }) {
                            Icon(Icons.Default.Close, contentDescription = "Cancel")
                        }
                    },
                    actions = {
                        IconButton(onClick = { viewModel.saveProfile() }) {
                            Icon(Icons.Default.Done, contentDescription = "Done", tint = MaterialTheme.colorScheme.primary)
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
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Photo Section
                Box(contentAlignment = Alignment.Center) {
                    KamelImage(
                        resource = { asyncPainterResource(uiState.profileImageUrl) },
                        contentDescription = "Profile Photo",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                
                TextButton(onClick = { /* Open Image Picker */ }) {
                    Text("Edit picture or avatar", fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Form Fields
                EditProfileTextField(
                    label = "Name",
                    value = uiState.fullName,
                    onValueChange = { viewModel.onFullNameChange(it) }
                )

                EditProfileTextField(
                    label = "Username",
                    value = uiState.username,
                    onValueChange = { viewModel.onUsernameChange(it) }
                )

                EditProfileTextField(
                    label = "Website",
                    value = uiState.website,
                    onValueChange = { viewModel.onWebsiteChange(it) }
                )

                EditProfileTextField(
                    label = "Bio",
                    value = uiState.bio,
                    onValueChange = { viewModel.onBioChange(it) },
                    singleLine = false,
                    minLines = 3
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                
                TextButton(
                    onClick = { /* Switch to professional account */ },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                        Text("Switch to Professional Account", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                    }
                }

                TextButton(
                    onClick = { /* Personal information settings */ },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                        Text("Personal Information Settings", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
fun EditProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
            ),
            singleLine = singleLine,
            minLines = minLines
        )
    }
}
