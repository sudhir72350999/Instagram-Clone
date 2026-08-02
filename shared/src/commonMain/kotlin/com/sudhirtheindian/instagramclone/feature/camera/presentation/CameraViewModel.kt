package com.sudhirtheindian.instagramclone.feature.camera.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class FlashMode {
    OFF, ON, AUTO
}

data class CameraUiState(
    val flashMode: FlashMode = FlashMode.OFF,
    val isFrontCamera: Boolean = false,
    val selectedMode: String = "STORY"
)

class CameraViewModel : ScreenModel {
    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    fun toggleFlash() {
        val nextMode = when (_uiState.value.flashMode) {
            FlashMode.OFF -> FlashMode.ON
            FlashMode.ON -> FlashMode.AUTO
            FlashMode.AUTO -> FlashMode.OFF
        }
        _uiState.value = _uiState.value.copy(flashMode = nextMode)
    }

    fun switchCamera() {
        _uiState.value = _uiState.value.copy(isFrontCamera = !_uiState.value.isFrontCamera)
    }

    fun setMode(mode: String) {
        _uiState.value = _uiState.value.copy(selectedMode = mode)
    }
}
