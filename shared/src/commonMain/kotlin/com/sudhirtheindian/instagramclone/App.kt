package com.sudhirtheindian.instagramclone

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.sudhirtheindian.instagramclone.core.ui.theme.InstagramTheme
import com.sudhirtheindian.instagramclone.feature.splash.SplashScreen

@Composable
fun App() {
    InstagramTheme {
        Navigator(SplashScreen()) { navigator ->
            if (navigator.lastItem is SplashScreen) {
                CurrentScreen()
            } else {
                SlideTransition(navigator)
            }
        }
    }
}
