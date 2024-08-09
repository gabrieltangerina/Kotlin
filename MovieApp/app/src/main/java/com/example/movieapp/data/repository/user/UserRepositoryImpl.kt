package com.example.movieapp.data.repository.user

import com.example.movieapp.domain.api.repository.user.UserRepository
import com.example.movieapp.domain.model.user.User
import com.example.movieapp.util.FirebaseHelper
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class UserRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : UserRepository {

    private val profileRef = firebaseDatabase.reference.child("profile")

    override suspend fun update(user: User) {
        return suspendCoroutine { continuation ->
            profileRef
                .child(FirebaseHelper.getUserId())
                .setValue(user)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resumeWith(Result.success(Unit))
                    } else {
                        task.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }
    }

    override suspend fun getUser(): User {
        return suspendCoroutine { continuation ->
            profileRef
                .child(FirebaseHelper.getUserId())
                .get()
                .addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        val user = task.result.getValue(User::class.java)
                        if(user != null){
                            continuation.resumeWith(Result.success(user))
                        }else{
                            continuation.resumeWith(
                                Result.failure(
                                    Exception("User not found")
                                )
                            )
                        }
                    }else{
                        task.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }
    }


}