package no.usn.mob3000.data.auth

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
@Serializable
data class User(
    @SerialName("id")
    val id: String,
    @SerialName("email")
    val email: String? = null,
    @SerialName("is_super_admin")
    val isAdmin: Boolean? = false,
    @SerialName("created_at")
    val accountCreatedAt: Instant? = null,
    @SerialName("updated_at")
    val accountUpdatedAt: Instant? = null,
    @SerialName("banned_until")
    val accountBannedUntil: Instant? = null,
    @SerialName("email_confirmed_at")
    val emailConfirmedAt: Instant? = null,
    @SerialName("confirmation_sent_at")
    val emailConfirmationSentAt: Instant? = null,
    @SerialName("recovery_sent_at")
    val recoveryEmailSentAt: Instant? = null,
    @SerialName("last_sign_in_at")
    val lastSignInAt: Instant? = null,
    val profile: UserProfile? = null,
    val socialData: UserSocial? = null
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
@Serializable
data class UserProfile(
    @SerialName("display_name")
    val displayName: String?,
    @SerialName("avatar_url")
    val avatar: String?, /* TODO: See Avatar. */
    @SerialName("url_rank")
    val eloRank: Int?,
    @SerialName("about_me")
    val aboutMe: String?,
    @SerialName("nationality")
    val nationality: String?,
    @SerialName("visibility")
    val visibility: Boolean,
    @SerialName("visibility_friends")
    val visibilityFriends: Boolean,
    @SerialName("updated_at")
    val updatedAt: String?
)

/**
 * The user's current friends, and their data, as well as
 * friend requests and their data.
 *
 * TODO: Investigate whether it's easier/better to simply use a serialized struct
 *       for the `friends` and `friend_requests` tables for specified users. I
 *       suspect it won't function as I expect in its current state.
 *
 * @param profileFriendList A list of [User] objects.
 * @param profileFriendRequests A list of [User] objects.
 * @author frigvid
 * @created 2024-10-21
 */
@Serializable
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
//@Serializable
//sealed class Avatar {
//    data class AvatarURL(val uri: Uri) : Avatar()
//    data class AvatarImage(val bitmap: Bitmap) : Avatar()
//}
