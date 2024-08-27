package com.example.bancodigital.domain.profile

import com.example.bancodigital.data.model.User
import com.example.bancodigital.data.repository.profile.ProfileDataSourceImpl
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val profileDataSourceImpl: ProfileDataSourceImpl
) {

    suspend operator fun invoke(user: User): User {
        return profileDataSourceImpl.updateProfile(user)
    }

}