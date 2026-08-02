package com.sudhirtheindian.instagramclone.feature.auth.data.repository

import com.sudhirtheindian.instagramclone.feature.auth.domain.model.User
import com.sudhirtheindian.instagramclone.feature.auth.domain.repository.AuthRepository
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override fun getCurrentUser(): Flow<User?> {
        return firebaseAuth.authStateChanged.map { firebaseUser ->
            firebaseUser?.let {
                User(
                    id = it.uid,
                    email = it.email,
                    username = null,
                    fullName = it.displayName,
                    profileImageUrl = it.photoURL,
                    bio = null,
                    website = null
                )
            }
        }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password)
            val firebaseUser = authResult.user ?: throw Exception("Login failed")
            Result.success(User(
                id = firebaseUser.uid,
                email = firebaseUser.email,
                username = null,
                fullName = firebaseUser.displayName,
                profileImageUrl = firebaseUser.photoURL,
                bio = null,
                website = null
            ))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, password: String, username: String, fullName: String): Result<User> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password)
            val firebaseUser = authResult.user ?: throw Exception("Registration failed")
            
            // Store additional user data in Firestore
            val userMap = mapOf(
                "id" to firebaseUser.uid,
                "username" to username,
                "fullName" to fullName,
                "email" to email,
                "createdAt" to dev.gitlive.firebase.firestore.Timestamp.now()
            )
            firestore.collection("users").document(firebaseUser.uid).set(userMap)

            Result.success(User(
                id = firebaseUser.uid,
                email = firebaseUser.email,
                username = username,
                fullName = fullName,
                profileImageUrl = null,
                bio = null,
                website = null
            ))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
