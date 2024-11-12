import no.usn.mob3000.domain.model.auth.UserProfile
import no.usn.mob3000.data.repository.social.UserRepository

class FetchUserProfileUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(userId: String): Result<UserProfile?> {
        return userRepository.getUserProfile(userId)
    }
}
