package com.sudhirtheindian.instagramclone.feature.upload.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CreatePostUiState(
    val selectedImageUri: String? = null,
    val isUploading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

class CreatePostViewModel : ScreenModel {
    private val _uiState = MutableStateFlow(CreatePostUiState())
    val uiState: StateFlow<CreatePostUiState> = _uiState.asStateFlow()

    fun onImageSelected(uri: String) {
        _uiState.value = _uiState.value.copy(selectedImageUri = uri, isSuccess = false)
    }

    fun uploadPost(caption: String) {
        val imageUri = _uiState.value.selectedImageUri ?: return
        
        screenModelScope.launch {
            _uiState.value = _uiState.value.copy(isUploading = true)
            
            // Mock upload delay
            delay(2000)
            
            // In a real app, we would:
            // 1. Upload image to Firebase Storage
            // 2. Save post metadata to Firestore/Database
            
            _uiState.value = _uiState.value.copy(
                isUploading = false,
                isSuccess = true
            )
        }
    }
}
