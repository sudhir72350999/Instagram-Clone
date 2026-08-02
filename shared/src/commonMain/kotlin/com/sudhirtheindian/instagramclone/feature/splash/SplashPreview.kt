package com.sudhirtheindian.instagramclone.feature.splash

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.sudhirtheindian.instagramclone.core.ui.theme.InstagramTheme
import com.sudhirtheindian.instagramclone.feature.splash.components.SplashLogo

@Preview
@Composable
fun SplashPreview() {
    InstagramTheme {
        SplashLogo()
    }
}
