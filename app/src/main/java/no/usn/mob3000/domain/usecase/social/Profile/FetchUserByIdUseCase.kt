package no.usn.mob3000.domain.usecase.social.Profile

import no.usn.mob3000.data.repository.social.UserRepository
import no.usn.mob3000.domain.model.auth.UserProfile

class FetchUserByIdUseCase(
    private val userRepository: UserRepository = UserRepository()
) {
    suspend operator fun invoke(userId: String): Result<UserProfile?> {
        return userRepository.fetchUserById(userId)
    }
}
