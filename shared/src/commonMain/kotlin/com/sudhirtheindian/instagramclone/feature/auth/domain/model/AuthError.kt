package com.sudhirtheindian.instagramclone.feature.auth.domain.model

sealed class AuthError : Exception() {
    object InvalidCredentials : AuthError()
    object EmailAlreadyInUse : AuthError()
    object WeakPassword : AuthError()
    object UserNotFound : AuthError()
    object NetworkError : AuthError()
    data class Unknown(val msg: String?) : AuthError()
}
