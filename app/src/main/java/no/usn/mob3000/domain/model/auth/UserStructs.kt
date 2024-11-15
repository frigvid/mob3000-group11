/**
 * These are user-related data objects that are used by the UI layer. These are similar
 * in structure to the serializable user DTO structs, but this one can be, and should be
 * considered as the "final" implementation.
 *
 * Any changes to these **MUST NOT** modify the structs in such a way that the UI layer
 * needs refactoring to access previously accessible values. Appending new values is fine,
 * but should be done cautiously, is all.
 *
 * @author frigvid
 * @created 2024-10-22
 */

package no.usn.mob3000.domain.model.auth

import kotlinx.datetime.Instant

/**
 * The user struct.
 *
 * @author frigvid
 * @created 2024-10-22
 */
data class User(
    val id: String,
    val email: String?,
    val isAdmin: Boolean,
    val stats: UserGameStats?,
    val profile: UserProfile?,
    val socials: UserSocial?,
    val metadata: UserMetadata
)

/**
 * The user's game stats struct.
 *
 * @author frigvid
 * @created 2024-10-22
 */
data class UserGameStats(
    val wins: Int,
    val losses: Int,
    val draws: Int
)

/**
 * The user's profile struct.
 *
 * @author frigvid
 * @created 2024-10-22
 */
data class UserProfile(
    val userId: String,
    val displayName: String,
    val avatarUrl: String,
    val eloRank: Int,
    val aboutMe: String,
    val nationality: String,
    val visibility: Boolean,
    val visibilityFriends: Boolean
)

/**
 * The user's socials struct.
 *
 * @author frigvid
 * @created 2024-10-22
 */
data class UserSocial(
    val friends: List<Friend>,
    val pendingRequests: List<FriendRequest>
)

/**
 * The user's account metadata struct.
 *
 * @author frigvid
 * @created 2024-10-22
 */
data class UserMetadata(
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
    val emailConfirmedAt: Instant? = null,
    val lastSignInAt: Instant? = null
    // TODO: val updatedProfileAt: Instant
)

/**
 * The user's friend struct.
 *
 * @author frigvid
 * @created 2024-10-22
 */
data class Friend(
    val friendshipId: String,
    val userId: String,
    val displayName: String,
    val eloRank: Int,
    val avatarUrl: String,
    val nationality: String
)

/**
 * The user's friend requests struct.
 *
 * @author frigvid
 * @created 2024-10-22
 */
data class FriendRequest(
    val friendRequestId: String,
    val createdAt: Instant,
    val fromUserId: String,
    val toUserId: String
)
