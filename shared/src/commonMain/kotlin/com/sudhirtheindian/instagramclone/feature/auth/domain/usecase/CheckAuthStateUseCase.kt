package com.sudhirtheindian.instagramclone.feature.auth.domain.usecase

import com.sudhirtheindian.instagramclone.feature.auth.domain.repository.AuthRepository

class CheckAuthStateUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Boolean {
        return repository.isUserLoggedIn()
    }
}
