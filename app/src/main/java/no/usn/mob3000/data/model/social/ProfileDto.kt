package no.usn.mob3000.data.model.social

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Serializable data transferable object representing the `public.profiles` table.
 *
 * @param userId A UUID reference to a matching user in `auth.users`.
 * @param updatedAt A timestamp representing when the profile was updated.
 * @param displayName The user's arbitrary display name.
 * @param eloRank The user's calculated ELO rank.
 * @param avatarUrl The user's avatar URL.
 * @param aboutMe The user's about me section.
 * @param nationality A unicode country code.
 * @param profileVisibility Whether the profile is visible to other users.
 * @param friendsVisibility Whether the profile's friend list is visible to other users.
 * @author frigvid
 * @created 2024-10-25
 */
@Serializable
data class ProfileDto(
    @SerialName("id")
    val userId: String,
    @SerialName("updated_at")
    val updatedAt: Instant,
    @SerialName("display_name")
    val displayName: String? = "",
    @SerialName("elo_rank")
    val eloRank: Int? = 0,
    @SerialName("avatar_url")
    val avatarUrl: String? = "",
    @SerialName("about_me")
    val aboutMe: String? = "",
    @SerialName("nationality")
    val nationality: String? = null,
    @SerialName("visibility")
    val profileVisibility: Boolean = false,
    @SerialName("visibility_friends")
    val friendsVisibility: Boolean = false
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
