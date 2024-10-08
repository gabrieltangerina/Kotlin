package com.example.bancodigital.data.repository.profile

import com.example.bancodigital.data.model.User

interface ProfileDataSource {

    suspend fun saveProfile(user: User)

    suspend fun getProfile(id: String): User

    suspend fun updateProfile(user: User): User

    suspend fun getProfiles(): List<User>

    suspend fun saveImageProfile(imageProfile: String): String

}