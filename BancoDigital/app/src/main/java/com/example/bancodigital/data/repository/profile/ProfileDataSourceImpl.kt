package com.example.bancodigital.data.repository.profile

import android.net.Uri
import android.util.Log
import com.example.bancodigital.data.model.User
import com.example.bancodigital.util.FirebaseHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class ProfileDataSourceImpl @Inject constructor(
    firebaseDatabase: FirebaseDatabase,
    storage: FirebaseStorage
) : ProfileDataSource {

    private val profileDatabaseReference = firebaseDatabase.reference
        .child("profile")

    private val profileStorageReference = storage.reference
        .child("images")
        .child("profiles")
        .child("${FirebaseHelper.getUserId()}.jpeg")

    override suspend fun saveProfile(user: User) {
        return suspendCoroutine { continuation ->
            profileDatabaseReference
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

    override suspend fun getProfile(id: String): User {
        return suspendCoroutine { continuation ->
            profileDatabaseReference
                .child(id)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val profile = snapshot.getValue(User::class.java)

                        profile?.let {
                            continuation.resumeWith(Result.success(it))
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        continuation.resumeWith(Result.failure(error.toException()))
                    }

                })
        }
    }

    override suspend fun updateProfile(user: User): User {
        return suspendCoroutine { continuation ->
            profileDatabaseReference
                .child(FirebaseHelper.getUserId())
                .setValue(user)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resumeWith(Result.success(user))
                    } else {
                        task.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }
    }

    override suspend fun getProfiles(): List<User> {
        return suspendCoroutine { continuation ->
            profileDatabaseReference
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val userList: MutableList<User> = mutableListOf()

                        for (ds in snapshot.children) {
                            val user = ds.getValue(User::class.java)
                            user?.let {
                                userList.add(it)
                            }
                        }

                        val idUserLoggedIn = FirebaseHelper.getUserId()
                        val index = userList.indexOfFirst { it.id == idUserLoggedIn }
                        if (index != -1) {
                            userList.removeAt(index)
                        }

                        continuation.resumeWith(Result.success(userList))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        continuation.resumeWith(Result.failure(error.toException()))
                    }

                })
        }
    }

    override suspend fun saveImageProfile(imageProfile: String): String {
        return suspendCoroutine { continuation ->
            val uploadTask = profileStorageReference.putFile(Uri.parse(imageProfile))

            uploadTask.addOnSuccessListener {

                profileStorageReference.downloadUrl.addOnCompleteListener { task ->
                    continuation.resumeWith(Result.success(task.result.toString()))
                }

            }.addOnFailureListener {
                continuation.resumeWith(Result.failure(it))
            }
        }
    }

}