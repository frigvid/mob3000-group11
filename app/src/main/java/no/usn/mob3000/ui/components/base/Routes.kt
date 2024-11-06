package no.usn.mob3000.ui.components.base

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import no.usn.mob3000.domain.enumerate.Destination
import no.usn.mob3000.domain.viewmodel.CBViewModel
import no.usn.mob3000.domain.viewmodel.auth.DeleteAccountViewModel
import no.usn.mob3000.domain.viewmodel.auth.LoginViewModel
import no.usn.mob3000.domain.viewmodel.auth.LogoutViewModel
import no.usn.mob3000.domain.viewmodel.auth.RegistrationViewModel
import no.usn.mob3000.ui.screens.AdministratorDashboardScreen
import no.usn.mob3000.ui.screens.HomeScreen
import no.usn.mob3000.ui.screens.SettingsScreen
import no.usn.mob3000.ui.screens.auth.CreateUserScreen
import no.usn.mob3000.ui.screens.auth.ForgotPasswordScreen
import no.usn.mob3000.ui.screens.auth.LoginScreen
import no.usn.mob3000.ui.screens.auth.ResetPasswordScreen
import no.usn.mob3000.ui.screens.chess.HistoryScreen
import no.usn.mob3000.ui.screens.chess.PlayScreen
import no.usn.mob3000.ui.screens.chess.train.group.CreateGroupScreen
import no.usn.mob3000.ui.screens.chess.train.group.GroupsScreen
import no.usn.mob3000.ui.screens.chess.train.opening.CreateOpeningScreen
import no.usn.mob3000.ui.screens.chess.train.opening.OpeningDetailsScreen
import no.usn.mob3000.ui.screens.chess.train.opening.OpeningsScreen
import no.usn.mob3000.ui.screens.info.AboutUsScreen
import no.usn.mob3000.ui.screens.info.InfoScreen
import no.usn.mob3000.ui.screens.info.docs.CreateDocumentationScreen
import no.usn.mob3000.ui.screens.info.docs.DocumentationDetailsScreen
import no.usn.mob3000.ui.screens.info.docs.DocumentationScreen
import no.usn.mob3000.ui.screens.info.faq.CreateFAQScreen
import no.usn.mob3000.ui.screens.info.faq.FAQScreen
import no.usn.mob3000.ui.screens.info.news.CreateNewsScreen
import no.usn.mob3000.ui.screens.info.news.NewsDetailsScreen
import no.usn.mob3000.ui.screens.info.news.NewsScreen
import no.usn.mob3000.ui.screens.user.ProfileAddFriendsScreen
import no.usn.mob3000.ui.screens.user.ProfileEditScreen
import no.usn.mob3000.ui.screens.user.ProfileFriendRequestsScreen
import no.usn.mob3000.ui.screens.user.ProfileScreen

/**
 * Object that organizes routes into categories based on sub-objects.
 *
 * @author frigvid
 * @created 2024-11-06
 */
object Routes {
    /**
     * Routes for information related screens. E.g. documentation, news, FAQ, etc.
     *
     * @author frigvid
     * @created 2024-11-06
     */
    object Information {
        /**
         * The invokable operator function for returning the composable routes.
         *
         * @param navGraphBuilder The navigation graph builder.
         * @param navController The navigation controller.
         * @param cbViewModel The generic application ViewModel.
         * @author frigvid
         * @created 2024-11-06
         */
        operator fun invoke(
            navGraphBuilder: NavGraphBuilder,
            navController: NavController,
            cbViewModel: CBViewModel
        ): Information {
            navGraphBuilder.composable(route = Destination.INFO.name) {
                InfoScreen(
                    onAboutUsClick = { navController.navigate(Destination.ABOUT_US.name) },
                    onDocumentationClick = { navController.navigate(Destination.DOCUMENTATION.name) },
                    onFAQClick = { navController.navigate(Destination.FAQ.name) }
                )
            }

            navGraphBuilder.documentationRoutes(navController, cbViewModel)
            navGraphBuilder.faqRoutes(navController, cbViewModel)
            navGraphBuilder.newsRoutes(navController, cbViewModel)

            navGraphBuilder.composable(route = Destination.ABOUT_US.name) { AboutUsScreen() }

            return this
        }

        /**
         * The documentation routes.
         *
         * @param navController The navigation controller.
         * @param cbViewModel The generic application ViewModel.
         * @author frigvid
         * @created 2024-11-06
         */
        private fun NavGraphBuilder.documentationRoutes(
            navController: NavController,
            cbViewModel: CBViewModel
        ) {
            composable(route = Destination.DOCUMENTATION.name) {
                DocumentationScreen(
                    documentations = cbViewModel.documentations.value,
                    onDocumentationClick = { navController.navigate(Destination.DOCUMENTATION_DETAILS.name) },
                    onCreateDocumentationClick = { navController.navigate(Destination.DOCUMENTATION_CREATE.name) },
                    setDocumentationList = cbViewModel::setDocumentations,
                    setSelectedDocumentation = cbViewModel::setSelectedDocumentation,
                    clearSelectedDocumentation = cbViewModel::clearSelectedDocumentation
                )
            }

            composable(route = Destination.DOCUMENTATION_DETAILS.name) {
                DocumentationDetailsScreen(
                    selectedDocumentation = cbViewModel.selectedDocumentation.value,
                    onEditClick = { navController.navigate(Destination.DOCUMENTATION_CREATE.name) }
                )
            }

            composable(route = Destination.DOCUMENTATION_CREATE.name) {
                CreateDocumentationScreen(
                    selectedDocumentation = cbViewModel.selectedDocumentation.value,
                    onSaveDocumentationClick = { navController.navigate(Destination.DOCUMENTATION.name) }
                )
            }
        }

        /**
         * The FAQ routes.
         *
         * @param navController The navigation controller.
         * @param cbViewModel The generic application ViewModel.
         * @author frigvid
         * @created 2024-11-06
         */
        private fun NavGraphBuilder.faqRoutes(
            navController: NavController,
            cbViewModel: CBViewModel
        ) {
            composable(route = Destination.FAQ.name) {
                FAQScreen(
                    faqList = cbViewModel.faqs.value,
                    onFAQClick = { navController.navigate(Destination.FAQ_CREATE.name) },
                    onCreateFAQClick = { navController.navigate(Destination.FAQ_CREATE.name) },
                    setFAQList = cbViewModel::setFAQs,
                    setSelectedFAQ = cbViewModel::setSelectedFAQ,
                    clearSelectedFAQ = cbViewModel::clearSelectedFAQ
                )
            }

            composable(route = Destination.FAQ_CREATE.name) {
                CreateFAQScreen(
                    selectedFAQ = cbViewModel.selectedFAQ.value,
                    onSaveFAQClick = { navController.navigate(Destination.FAQ.name) },
                    onDeleteFAQClick = { navController.navigate(Destination.FAQ.name) }
                )
            }
        }

        /**
         * The news routes.
         *
         * @param navController The navigation controller.
         * @param cbViewModel The generic application ViewModel.
         * @author frigvid
         * @created 2024-11-06
         */
        private fun NavGraphBuilder.newsRoutes(
            navController: NavController,
            cbViewModel: CBViewModel
        ) {
            composable(route = Destination.NEWS.name) {
                NewsScreen(
                    news = cbViewModel.news.value,
                    onNewsClick = { navController.navigate(Destination.NEWS_DETAILS.name) },
                    onCreateNewsClick = { navController.navigate(Destination.NEWS_CREATE.name) },
                    setNewsList = cbViewModel::setNews,
                    setSelectedNews = cbViewModel::setSelectedNews,
                    clearSelectedNews = cbViewModel::clearSelectedNews
                )
            }

            composable(route = Destination.NEWS_DETAILS.name) {
                NewsDetailsScreen(
                    selectedNews = cbViewModel.selectedNews.value,
                    onEditClick = { navController.navigate(Destination.NEWS_CREATE.name) }
                )
            }

            composable(route = Destination.NEWS_CREATE.name) {
                CreateNewsScreen(
                    selectedNews = cbViewModel.selectedNews.value,
                    onSaveNewsClick = { navController.navigateUp() }
                )
            }
        }
    }

    /**
     * Routes for game-related Screens.
     *
     * @author frigvid
     * @created 2024-11-06
     */
    object Game {
        /**
         * The invokable operator function for returning the composable routes.
         *
         * @param navGraphBuilder The navigation graph builder.
         * @param navController The navigation controller.
         * @param cbViewModel The generic application ViewModel.
         * @author frigvid
         * @created 2024-11-06
         */
        operator fun invoke(
            navGraphBuilder: NavGraphBuilder,
            navController: NavController,
            cbViewModel: CBViewModel
        ): Game {
            navGraphBuilder.openingRoutes(navController, cbViewModel)
            navGraphBuilder.repositoryRoutes(navController, cbViewModel)
            navGraphBuilder.gameRoutes(navController, cbViewModel)

            return this
        }

        /**
         * The chess openings routes.
         *
         * @param navController The navigation controller.
         * @param cbViewModel The generic application ViewModel.
         * @author frigvid
         * @created 2024-11-06
         */
        private fun NavGraphBuilder.openingRoutes(
            navController: NavController,
            cbViewModel: CBViewModel
        ) {
            composable(route = Destination.OPENINGS.name) {
                OpeningsScreen(
                    onGroupsClick = { navController.navigate(Destination.GROUPS.name) },
                    onCreateOpeningClick = { navController.navigate(Destination.OPENINGS_CREATE.name) },
                    onOpeningClick = { navController.navigate(Destination.OPENING_DETAILS.name) },
                    setOpenings = cbViewModel::setOpenings,
                    setSelectedOpening = cbViewModel::setSelectedOpening
                )
            }

            composable(route = Destination.OPENINGS_CREATE.name) { CreateOpeningScreen() }

            composable(route = Destination.OPENING_DETAILS.name) {
                OpeningDetailsScreen(
                    opening = cbViewModel.selectedOpening.value,
                    onPracticeClick = { navController.navigate(Destination.PLAY.name) }
                )
            }
        }

        /**
         * The chess repository/groups routes.
         *
         * @param navController The navigation controller.
         * @param cbViewModel The generic application ViewModel.
         * @author frigvid
         * @created 2024-11-06
         */
        private fun NavGraphBuilder.repositoryRoutes(
            navController: NavController,
            cbViewModel: CBViewModel
        ) {
            composable(route = Destination.GROUPS.name) {
                GroupsScreen(
                    onCreateGroupClick = { navController.navigate(Destination.GROUPS_CREATE.name) },
                    onReturnToOpeningClick = { navController.navigate(Destination.OPENINGS.name) }
                )
            }

            composable(route = Destination.GROUPS_CREATE.name) {
                CreateGroupScreen(
                    availableOpenings = cbViewModel.openings.value
                )
            }
        }

        /**
         * The chess game and history routes.
         *
         * @param navController The navigation controller.
         * @param cbViewModel The generic application ViewModel.
         * @author frigvid
         * @created 2024-11-06
         */
        private fun NavGraphBuilder.gameRoutes(
            navController: NavController,
            cbViewModel: CBViewModel
        ) {
            composable(route = Destination.PLAY.name) { PlayScreen() }

            composable(route = Destination.HISTORY.name) {
                HistoryScreen(
                    setSelectedOpening = cbViewModel::setSelectedOpening,
                    getOpeningById = cbViewModel::getOpeningById,
                    onOpeningDetailsClick = { navController.navigate(Destination.OPENING_DETAILS.name) }
                )
            }
        }
    }

    /**
     * Routes for user profile related Screens.
     *
     * @author frigvid
     * @created 2024-11-06
     */
    object UserProfile {
        /**
         * The invokable operator function for returning the composable routes.
         *
         * @param navGraphBuilder The navigation graph builder.
         * @param navController The navigation controller.
         * @author frigvid
         * @created 2024-11-06
         */
        operator fun invoke(
            navGraphBuilder: NavGraphBuilder,
            navController: NavController
        ): UserProfile {
            navGraphBuilder.profile(navController)
            navGraphBuilder.profileFriends()

            return this
        }

        /**
         * The chess openings routes.
         *
         * @param navController The navigation controller.
         * @author frigvid
         * @created 2024-11-06
         */
        private fun NavGraphBuilder.profile(
            navController: NavController
        ) {
            composable(route = Destination.PROFILE.name) {
                ProfileScreen(
                    onProfileEditClick = { navController.navigate(Destination.PROFILE_EDIT_PROFILE.name) },
                    onProfileAddFriendsClick = { navController.navigate(Destination.PROFILE_ADD_FRIENDS.name) },
                    onProfileFriendRequestsClick = { navController.navigate(Destination.PROFILE_FRIEND_REQUESTS.name) }
                )
            }

            composable(route = Destination.PROFILE_EDIT_PROFILE.name) {
                ProfileEditScreen(
                    onSaveProfileClick = { navController.navigate(Destination.PROFILE.name) }
                )
            }
        }

        /**
         * The chess repository/groups routes.
         *
         * @author frigvid
         * @created 2024-11-06
         */
        private fun NavGraphBuilder.profileFriends() {
            composable(route = Destination.PROFILE_ADD_FRIENDS.name) { ProfileAddFriendsScreen() }
            composable(route = Destination.PROFILE_FRIEND_REQUESTS.name) { ProfileFriendRequestsScreen() }
        }
    }

    /**
     * Routes for the user/app settings Screen.
     *
     * @author frigvid
     * @created 2024-11-06
     */
    object Settings {
        /**
         * The invokable operator function that returns the composable route definition.
         *
         * @param navGraphBuilder The navigation graph builder.
         * @param navController The navigation controller.
         * @param cbViewModel The generic application ViewModel.
         * @param logoutViewModel The account logout ViewModel.
         * @param deleteAccountViewModel The account deletion ViewModel.
         * @author frigvid
         * @created 2024-11-06
         */
        operator fun invoke(
            navGraphBuilder: NavGraphBuilder,
            navController: NavController,
            cbViewModel: CBViewModel,
            logoutViewModel: LogoutViewModel,
            deleteAccountViewModel: DeleteAccountViewModel
        ): Settings {
            navGraphBuilder.composable(route = Destination.SETTINGS.name) {
                SettingsScreen(
                    logoutState = logoutViewModel.logoutState,
                    logoutStateReset = logoutViewModel::resetState,
                    onLogoutClick = logoutViewModel::logout,
                    onLoginClick = { navController.navigate(Destination.AUTH_LOGIN.name) },
                    accountDeleteState = deleteAccountViewModel.deleteAccountState,
                    accountDeleteStateReset = deleteAccountViewModel::resetState,
                    onDeleteAccountClick = deleteAccountViewModel::deleteAccount,
                    onAdminDashboardClick = { navController.navigate(Destination.ADMIN_DASHBOARD.name) },
                    selectedTheme = cbViewModel.selectedTheme.value,
                    selectedLanguage = cbViewModel.selectedLanguage.value,
                    onThemeChange = cbViewModel::setSelectedTheme,
                    onLanguageChange = cbViewModel::setSelectedLanguage
                )
            }

            return this
        }
    }

    /**
     * Routes for user authentication related Screens.
     *
     * @author frigvid
     * @created 2024-11-06
     */
    object Authentication {
        /**
         * The invokable operator function for returning the composable routes.
         *
         * @param navGraphBuilder The navigation graph builder.
         * @param navController The navigation controller.
         * @param loginViewModel The login ViewModel.
         * @param registrationViewModel The registration ViewModel.
         * @author frigvid
         * @created 2024-11-06
         */
        operator fun invoke(
            navGraphBuilder: NavGraphBuilder,
            navController: NavController,
            loginViewModel: LoginViewModel,
            registrationViewModel: RegistrationViewModel
        ): Authentication {
            navGraphBuilder.composable(route = Destination.AUTH_LOGIN.name) {
                LoginScreen(
                    loginState = loginViewModel.loginState,
                    loginStateReset = loginViewModel::resetState,
                    navigateHome = { navController.navigate(Destination.HOME.name) },
                    onLoginClick = loginViewModel::login,
                    onCreateUserClick = { navController.navigate(Destination.AUTH_CREATE.name)},
                    onForgotPasswordClick = { navController.navigate(Destination.AUTH_FORGOT.name) }
                )
            }

            navGraphBuilder.composable(route = Destination.AUTH_CREATE.name) {
                CreateUserScreen(
                    registrationState = registrationViewModel.registrationState,
                    registrationStateUpdate = registrationViewModel::updateState,
                    registrationStateReset = registrationViewModel::resetState,
                    onSignUpClick = registrationViewModel::register,
                    onReturnToLoginClick = { navController.navigate(Destination. AUTH_LOGIN.name) }
                )
            }

            navGraphBuilder.composable(route = Destination.AUTH_FORGOT.name) {
                ForgotPasswordScreen(
                    onResetPasswordClick = { navController.navigate(Destination.AUTH_RESET.name) }
                )
            }

            navGraphBuilder.composable(route = Destination.AUTH_RESET.name) {
                ResetPasswordScreen(
                    onResetPasswordClick = { navController.navigate(Destination.HOME.name) },
                )
            }

            return this
        }
    }

    /**
     * Routes for the admin Screen.
     *
     * @author frigvid
     * @created 2024-11-06
     */
    object Administrator {
        /**
         * The invokable operator function that returns the composable route definition.
         *
         * @param navGraphBuilder The navigation graph builder.
         * @author frigvid
         * @created 2024-11-06
         */
        operator fun invoke(
            navGraphBuilder: NavGraphBuilder
        ): Administrator {
            navGraphBuilder.composable(route = Destination.ADMIN_DASHBOARD.name) { AdministratorDashboardScreen() }

            return this
        }
    }

    /**
     * Routes for the home Screen.
     *
     * @author frigvid
     * @created 2024-11-06
     */
    object Home {
        /**
         * The invokable operator function that returns the composable route definition.
         *
         * @param navGraphBuilder The navigation graph builder.
         * @param navController The navigation controller.
         * @author frigvid
         * @created 2024-11-06
         */
        operator fun invoke(
            navGraphBuilder: NavGraphBuilder,
            navController: NavController
        ): Home {
            navGraphBuilder.composable(route = Destination.HOME.name) {
                HomeScreen(
                    onTrainClick = { navController.navigate(Destination.OPENINGS.name) },
                    onPlayClick =  { navController.navigate(Destination.PLAY.name) },
                    onHistoryClick =  { navController.navigate(Destination.HISTORY.name) }
                )
            }

            return this
        }
    }
}
