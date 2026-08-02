package com.sudhirtheindian.instagramclone.core.di

import com.sudhirtheindian.instagramclone.feature.auth.data.repository.AuthRepositoryImpl
import com.sudhirtheindian.instagramclone.feature.auth.domain.repository.AuthRepository
import com.sudhirtheindian.instagramclone.feature.auth.domain.usecase.CheckAuthStateUseCase
import com.sudhirtheindian.instagramclone.feature.auth.domain.usecase.LoginUseCase
import com.sudhirtheindian.instagramclone.feature.auth.domain.usecase.RegisterUseCase
import com.sudhirtheindian.instagramclone.feature.auth.presentation.login.LoginViewModel
import com.sudhirtheindian.instagramclone.feature.auth.presentation.register.RegisterViewModel
import com.sudhirtheindian.instagramclone.feature.home.data.repository.HomeRepositoryImpl
import com.sudhirtheindian.instagramclone.feature.home.domain.repository.HomeRepository
import com.sudhirtheindian.instagramclone.feature.home.domain.usecase.GetFeedUseCase
import com.sudhirtheindian.instagramclone.feature.home.presentation.home.HomeViewModel
import com.sudhirtheindian.instagramclone.feature.splash.presentation.SplashViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            commonModule,
            networkModule,
            databaseModule,
            repositoryModule,
            useCaseModule,
            viewModelModule
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
    // SQLDelight database (Need driver)
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
}
