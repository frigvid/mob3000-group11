package no.usn.mob3000.data.auth

/**
 * The user.
 *
 * TODO: Consider extracting `visibility` and `visibilityFriends`. Might be useful for logic
 *       operations needing to know if the profile is available or not.
 *
 * @param id The user's UUID.
 * @param email The user's email.
 * @param isAdmin Whether the user is an admin or not.
 * @param accountCreatedAt A timestamp for when the account was created.
 * @param accountUpdatedAt A timestamp for when the account was updated.
 * @param accountBannedUntil A timestamp telling for how long a user is banned.
 * @param emailConfirmedAt A timestamp for when the account's email was confirmed.
 * @param emailConfirmationSentAt A timestamp for when the account's confirmation email was sent.
 * @param recoveryEmailSentAt A timestamp for when the password recovery email was sent.
 * @param lastSignInAt A timestamp for when the user last signed in.
 * @param profile The user's [UserProfile] object, if any.
 * @param socialData The user's [UserSocial] object, if any.
 * @author frigvid
 * @created 2024-10-21
 */
data class User(
    val id: String,
    val email: String,
    val isAdmin: Boolean,
    val accountCreatedAt: String?,
    val accountUpdatedAt: String?,
    val accountBannedUntil: String? = null,
    val emailConfirmedAt: String?,
    val emailConfirmationSentAt: String?,
    val recoveryEmailSentAt: String?,
    val lastSignInAt: String?,
    val profile: UserProfile?,
    val socialData: UserSocial?
)

/**
 * The user's profile.
 *
 * @param displayName The user's arbitrarily chosen display name.
 * @param avatar The user's avatar URL.
 * @param eloRank The user's ELO rank.
 * @param aboutMe The user's descriptive extract on their profile.
 * @param nationality The user's nationality via their country code. E.g. `NO`.
 * @param visibility The user's entire profile visibility. If false, then people can't access it.
 * @param visibilityFriends The user's profile friend list visibility. If false, then people can
 *                          access their profile, but not see their friends.
 * @author frigvid
 * @created 2024-10-21
 */
data class UserProfile(
    val displayName: String?,
    val avatar: String?, /* TODO: See Avatar. */
    val eloRank: Int?,
    val aboutMe: String?,
    val nationality: String?,
    val visibility: Boolean,
    val visibilityFriends: Boolean,
    val updatedAt: String?
)

/**
 * The user's current friends, and their data,
 * as well as friend requests and their data.
 *
 * @param profileFriendList A list of [User] objects.
 * @param profileFriendRequests A list of [User] objects.
 * @author frigvid
 * @created 2024-10-21
 */
data class UserSocial(
    val profileFriendList: List<User>?,
    val profileFriendRequests: List<User>?
)

/* TODO: Investigate potential for using actual images without actually having to change the
 *       database tables in any way, shape or form. Below is one such temporary component of
 *       this implementation.
 *       <p/>
 *       Given how buckets work, perhaps what we'd do is simply update the cell value with a
 *       static, pre-defined value, that lets the app know to look for avatars in a bucket
 *       instead? Pretty hacky, but may be something to consider. That'd require some kind
 *       of id<-->image key-value to target images that are the user's. Could potentially be
 *       used with news too.
 *
 * See: https://supabase.com/docs/reference/kotlin/storage-createbucket
 */
//sealed class Avatar {
//    data class AvatarURL(val uri: Uri) : Avatar()
//    data class AvatarImage(val bitmap: Bitmap) : Avatar()
//}
