package com.sudhirtheindian.instagramclone.core.di

import com.sudhirtheindian.instagramclone.feature.auth.data.repository.AuthRepositoryImpl
import com.sudhirtheindian.instagramclone.feature.auth.domain.repository.AuthRepository
import com.sudhirtheindian.instagramclone.feature.auth.domain.usecase.CheckAuthStateUseCase
import com.sudhirtheindian.instagramclone.feature.auth.domain.usecase.LoginUseCase
import com.sudhirtheindian.instagramclone.feature.auth.domain.usecase.RegisterUseCase
import com.sudhirtheindian.instagramclone.feature.auth.presentation.login.LoginViewModel
import com.sudhirtheindian.instagramclone.feature.auth.presentation.register.RegisterViewModel
import com.sudhirtheindian.instagramclone.feature.camera.presentation.CameraViewModel
import com.sudhirtheindian.instagramclone.feature.home.data.repository.HomeRepositoryImpl
import com.sudhirtheindian.instagramclone.feature.home.domain.repository.HomeRepository
import com.sudhirtheindian.instagramclone.feature.home.domain.usecase.GetFeedUseCase
import com.sudhirtheindian.instagramclone.feature.home.presentation.home.HomeViewModel
import com.sudhirtheindian.instagramclone.feature.chat.presentation.ChatListViewModel
import com.sudhirtheindian.instagramclone.feature.chat.presentation.ChatViewModel
import com.sudhirtheindian.instagramclone.feature.reels.presentation.ReelsViewModel
import com.sudhirtheindian.instagramclone.feature.search.presentation.SearchViewModel
import com.sudhirtheindian.instagramclone.feature.notification.presentation.NotificationViewModel
import com.sudhirtheindian.instagramclone.feature.profile.presentation.ProfileViewModel
import com.sudhirtheindian.instagramclone.feature.profile.followers.presentation.FollowersViewModel
import com.sudhirtheindian.instagramclone.feature.profile.editprofile.presentation.EditProfileViewModel
import com.sudhirtheindian.instagramclone.feature.publicprofile.presentation.PublicProfileViewModel
import com.sudhirtheindian.instagramclone.feature.settings.presentation.SettingsViewModel
import com.sudhirtheindian.instagramclone.feature.splash.presentation.SplashViewModel
import com.sudhirtheindian.instagramclone.feature.upload.presentation.CreatePostViewModel
import com.sudhirtheindian.instagramclone.db.InstagramDatabase
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            commonModule,
            networkModule,
            databaseModule,
            repositoryModule,
            useCaseModule,
            viewModelModule,
            platformModule,
        )
    }

// For iOS
fun initKoin() = initKoin {}

val commonModule = module {
    // Core utilities, DataStore, etc.
}

val networkModule = module {
    single { Firebase.auth }
    single { Firebase.firestore }
}

val databaseModule = module {
    single { InstagramDatabase(get()) }
}

val repositoryModule = module {
    factoryOf(::AuthRepositoryImpl) bind AuthRepository::class
    factoryOf(::HomeRepositoryImpl) bind HomeRepository::class
}

val useCaseModule = module {
    factoryOf(::CheckAuthStateUseCase)
    factoryOf(::LoginUseCase)
    factoryOf(::RegisterUseCase)
    factoryOf(::GetFeedUseCase)
}

val viewModelModule = module {
    factoryOf(::SplashViewModel)
    factoryOf(::LoginViewModel)
    factoryOf(::RegisterViewModel)
    factoryOf(::HomeViewModel)
    factoryOf(::CameraViewModel)
    factoryOf(::ChatListViewModel)
    factoryOf(::ChatViewModel)
    factoryOf(::CreatePostViewModel)
    factoryOf(::ReelsViewModel)
    factoryOf(::SearchViewModel)
    factoryOf(::NotificationViewModel)
    factoryOf(::ProfileViewModel)
    factoryOf(::FollowersViewModel)
    factoryOf(::EditProfileViewModel)
    factoryOf(::PublicProfileViewModel)
    factoryOf(::SettingsViewModel)
}
