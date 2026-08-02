package com.sudhirtheindian.instagramclone

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.sudhirtheindian.instagramclone.core.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "InstagramClone",
        ) {
            App()
        }
    }
}
