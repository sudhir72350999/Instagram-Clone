package com.sudhirtheindian.instagramclone.feature.profile.editprofile.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import com.sudhirtheindian.instagramclone.feature.auth.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class EditProfileUiState(
    val fullName: String = "",
    val username: String = "",
    val website: String = "",
    val bio: String = "",
    val profileImageUrl: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false
)

class EditProfileViewModel : ScreenModel {
    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    fun init(user: User?) {
        user?.let {
            _uiState.value = _uiState.value.copy(
                fullName = it.fullName ?: "",
                username = it.username ?: "",
                website = it.website ?: "",
                bio = it.bio ?: "",
                profileImageUrl = it.profileImageUrl ?: ""
            )
        }
    }

    fun onFullNameChange(name: String) {
        _uiState.value = _uiState.value.copy(fullName = name)
    }

    fun onUsernameChange(username: String) {
        _uiState.value = _uiState.value.copy(username = username)
    }

    fun onWebsiteChange(website: String) {
        _uiState.value = _uiState.value.copy(website = website)
    }

    fun onBioChange(bio: String) {
        _uiState.value = _uiState.value.copy(bio = bio)
    }
    
    fun onProfileImageChange(url: String) {
        _uiState.value = _uiState.value.copy(profileImageUrl = url)
    }

    fun saveProfile() {
        // Logic to save profile
        _uiState.value = _uiState.value.copy(isLoading = true)
        // Simulate network call
        _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
    }
}
