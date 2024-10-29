package no.usn.mob3000.data.model.auth

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * The user.
 *
 * @param id The user's UUID.
 * @param email The user's email.
 * @param emailConfirmedAt A timestamp for when the account's email was confirmed.
 * @param invitedAt A timestamp for when, and if, the user was invited to the platform.
 * @param emailConfirmationSentAt A timestamp for when the account's confirmation email was sent.
 * @param recoveryEmailSentAt A timestamp for when the password recovery email was sent.
 * @param emailChangeSentAt A timestamp for when the e-mail change was sent.
 * @param lastSignInAt A timestamp for when the user last signed in.
 * @param rawUserMetadata Json object containing potential arbitrary key-value pairs.
 * @param isAdmin Whether the user is an admin or not.
 * @param accountCreatedAt A timestamp for when the account was created.
 * @param accountUpdatedAt A timestamp for when the account was updated.
 * @param accountBannedUntil A timestamp telling for how long a user is banned.
 * @author frigvid
 * @created 2024-10-21
 */
@Serializable
data class UserDto(
    @SerialName("id")
    val id: String,
    @SerialName("email")
    val email: String? = null,
    @SerialName("email_confirmed_at")
    val emailConfirmedAt: Instant? = null,
    @SerialName("invited_at")
    val invitedAt: Instant? = null,
    @SerialName("confirmation_sent_at")
    val emailConfirmationSentAt: Instant? = null,
    @SerialName("recovery_sent_at")
    val recoveryEmailSentAt: Instant? = null,
    @SerialName("email_change_sent_at")
    val emailChangeSentAt: Instant? = null,
    @SerialName("last_sign_in_at")
    val lastSignInAt: Instant? = null,
    @SerialName("raw_user_meta_data")
    val rawUserMetadata: JsonObject? = null,
    @SerialName("is_super_admin")
    val isAdmin: Boolean? = false,
    @SerialName("created_at")
    val accountCreatedAt: Instant? = null,
    @SerialName("updated_at")
    val accountUpdatedAt: Instant? = null,
    @SerialName("banned_until")
    val accountBannedUntil: Instant? = null,
)
