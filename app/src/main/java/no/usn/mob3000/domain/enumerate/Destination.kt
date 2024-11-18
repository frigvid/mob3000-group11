package no.usn.mob3000.domain.enumerate

import androidx.annotation.StringRes
import no.usn.mob3000.R
import no.usn.mob3000.domain.model.base.Icon

/**
 * This enum class defines the destination of a given route, and whether or not it has an icon.
 *
 * A destination definition, is quite simple. You need to add a title within a `values/strings.xml`
 * file, and whichever localizations there are. And you need to define an icon, if applicable.
 *
 * Here is an example of a root destination, and a regular route destination.
 * ```
 * SOME_ROOT_DESTINATION(title = R.string.some_route_destination, icon = Icon.DrawableResourceIcon(R.drawable.navbar_some_root_destination)),
 * SOME_DESTINATION(title = R.string.some_destination)
 * ```
 *
 * The destinations chosen for the [BottomNavbar], are controlled via a list called `rootEntries`
 * inside the [Viewport] function.
 *
 * @param title The title for the destination. For example, this could be used in the [TopNavbar].
 * @param icon The icon for the destination. Primarily used in the [BottomNavbar].
 * @author frigvid
 * @created 2024-09-24
 */
enum class Destination(
    @StringRes val title: Int,
    val icon: Icon? = null
) {
    INFO(title = R.string.info_title, icon = Icon.DrawableResourceIcon(R.drawable.navbar_info)),
    DOCUMENTATION(title = R.string.documentation_title),
    DOCUMENTATION_CREATE(title = R.string.documentation_create_title),
    DOCUMENTATION_DETAILS(title = R.string.documentation_details_title),
    DOCUMENTATION_UPDATE(title = R.string.documentation_update_title),
    FAQ(title = R.string.faq_title),
    FAQ_CREATE(title = R.string.faq_create_title),
    FAQ_DETAILS(title = R.string.faq_details_title),
    FAQ_UPDATE(title = R.string.faq_update_title),
    ABOUT_US(title = R.string.about_us_title),
    NEWS(title = R.string.news_title, icon = Icon.DrawableResourceIcon(R.drawable.navbar_news)),
    NEWS_DETAILS(title = R.string.news_title),
    NEWS_CREATE(title = R.string.news_create_title),
    NEWS_UPDATE(title = R.string.news_update_title),
    HOME(title = R.string.home_title, icon = Icon.DrawableResourceIcon(R.drawable.navbar_home)),
    PROFILE(title = R.string.profile_title, icon = Icon.DrawableResourceIcon(R.drawable.navbar_profile)),
    PROFILE_EDIT_PROFILE(title = R.string.profile_edit_profile_title),
    PROFILE_ADD_FRIENDS(title = R.string.profile_add_friends_title),
    PROFILE_FRIEND_REQUESTS(title = R.string.profile_pending_friend_requests_title),
    SETTINGS(title = R.string.settings_title, icon = Icon.DrawableResourceIcon(R.drawable.navbar_settings)),
    OPENINGS(title = R.string.openings_title),
    OPENINGS_CREATE(title = R.string.openings_create_title),
    OPENINGS_UPDATE(title = R.string.opening_update_title),
    OPENING_DETAILS(title = R.string.opening_details),
    GROUPS(title = R.string.groups_title),
    GROUPS_CREATE(title = R.string.groups_create_title),
    GROUPS_UPDATE(title = R.string.group_update_title),
    PLAY(title = R.string.home_play_title),
    HISTORY(title = R.string.home_history_title),
    AUTH_LOGIN(title = R.string.auth_login_title),
    AUTH_CREATE(title = R.string.auth_create_user_title),
    AUTH_FORGOT(title = R.string.auth_forgot_password_title),
    AUTH_RESET(title = R.string.auth_reset_password_title),
    AUTH_PASSWORD_RESET_VIA_EMAIL(title = R.string.auth_reset_password_via_email_title),
    AUTH_EMAIL_CHANGE(title = R.string.auth_email_title),
    ADMIN_DASHBOARD(title = R.string.admin_dashboard_title)
}
