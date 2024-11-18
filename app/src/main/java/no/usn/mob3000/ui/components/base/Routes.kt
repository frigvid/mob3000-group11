package no.usn.mob3000.ui.components.base

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import no.usn.mob3000.domain.enumerate.Destination
import no.usn.mob3000.domain.viewmodel.CBViewModel
import no.usn.mob3000.domain.viewmodel.auth.AuthenticationViewModel
import no.usn.mob3000.domain.viewmodel.auth.ChangeEmailViewModel
import no.usn.mob3000.domain.viewmodel.auth.ChangePasswordViewModel
import no.usn.mob3000.domain.viewmodel.auth.DeleteAccountViewModel
import no.usn.mob3000.domain.viewmodel.auth.ForgotPasswordViewModel
import no.usn.mob3000.domain.viewmodel.auth.LoginViewModel
import no.usn.mob3000.domain.viewmodel.auth.LogoutViewModel
import no.usn.mob3000.domain.viewmodel.auth.RegistrationViewModel
import no.usn.mob3000.domain.viewmodel.content.DocumentationViewModel
import no.usn.mob3000.domain.viewmodel.content.FAQViewModel
import no.usn.mob3000.domain.viewmodel.content.NewsViewModel
import no.usn.mob3000.domain.viewmodel.game.ChessBoardViewModel
import no.usn.mob3000.domain.viewmodel.game.GroupsViewModel
import no.usn.mob3000.domain.viewmodel.game.OpeningsViewModel
import no.usn.mob3000.ui.screens.AdministratorDashboardScreen
import no.usn.mob3000.ui.screens.HomeScreen
import no.usn.mob3000.ui.screens.SettingsScreen
import no.usn.mob3000.ui.screens.auth.ChangeEmailScreen
import no.usn.mob3000.ui.screens.auth.CreateUserScreen
import no.usn.mob3000.ui.screens.auth.ForgotPasswordScreen
import no.usn.mob3000.ui.screens.auth.LoginScreen
import no.usn.mob3000.ui.screens.auth.ResetPasswordScreen
import no.usn.mob3000.ui.screens.auth.ResetPasswordViaEmailScreen
import no.usn.mob3000.ui.screens.chess.HistoryScreen
import no.usn.mob3000.ui.screens.chess.PlayScreen
import no.usn.mob3000.ui.screens.chess.group.CreateGroupScreen
import no.usn.mob3000.ui.screens.chess.group.GroupsScreen
import no.usn.mob3000.ui.screens.chess.group.UpdateGroupScreen
import no.usn.mob3000.ui.screens.chess.opening.CreateOpeningScreen
import no.usn.mob3000.ui.screens.chess.opening.OpeningDetailsScreen
import no.usn.mob3000.ui.screens.chess.opening.OpeningsScreen
import no.usn.mob3000.ui.screens.chess.opening.UpdateOpeningScreen
import no.usn.mob3000.ui.screens.info.AboutUsScreen
import no.usn.mob3000.ui.screens.info.InfoScreen
import no.usn.mob3000.ui.screens.info.docs.CreateDocumentationScreen
import no.usn.mob3000.ui.screens.info.docs.DocumentationDetailsScreen
import no.usn.mob3000.ui.screens.info.docs.DocumentationScreen
import no.usn.mob3000.ui.screens.info.docs.UpdateDocumentationScreen
import no.usn.mob3000.ui.screens.info.faq.CreateFAQScreen
import no.usn.mob3000.ui.screens.info.faq.FAQDetailsScreen
import no.usn.mob3000.ui.screens.info.faq.FAQScreen
import no.usn.mob3000.ui.screens.info.faq.UpdateFAQScreen
import no.usn.mob3000.ui.screens.info.news.CreateNewsScreen
import no.usn.mob3000.ui.screens.info.news.NewsDetailsScreen
import no.usn.mob3000.ui.screens.info.news.NewsScreen
import no.usn.mob3000.ui.screens.info.news.UpdateNewsScreen
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
         * @param authenticationViewModel The authentication status ViewModel.
         * @param documentationViewModel The documentation ViewModel.
         * @param faqViewModel The FAQ ViewModel.
         * @param newsViewModel The news ViewModel.
         * @author frigvid
         * @created 2024-11-06
         */
        operator fun invoke(
            navGraphBuilder: NavGraphBuilder,
            navController: NavController,
            authenticationViewModel: AuthenticationViewModel,
            documentationViewModel: DocumentationViewModel,
            faqViewModel: FAQViewModel,
            newsViewModel: NewsViewModel
        ): Information {
            navGraphBuilder.composable(route = Destination.INFO.name) {
                InfoScreen(
                    onAboutUsClick = { navController.navigate(Destination.ABOUT_US.name) },
                    onDocumentationClick = { navController.navigate(Destination.DOCUMENTATION.name) },
                    onFAQClick = { navController.navigate(Destination.FAQ.name) }
                )
            }

            navGraphBuilder.documentationRoutes(navController, authenticationViewModel, documentationViewModel)
            navGraphBuilder.faqRoutes(navController, authenticationViewModel, faqViewModel)
            navGraphBuilder.newsRoutes(navController, authenticationViewModel, newsViewModel)

            navGraphBuilder.composable(route = Destination.ABOUT_US.name) { AboutUsScreen() }

            return this
        }

        /**
         * The documentation routes.
         *
         * @param navController The navigation controller.
         * @param authenticationViewModel The authentication status view model.
         * @param documentationViewModel The documentation view model.
         * @author frigvid
         * @created 2024-11-06
         */
        private fun NavGraphBuilder.documentationRoutes(
            navController: NavController,
            authenticationViewModel: AuthenticationViewModel,
            documentationViewModel: DocumentationViewModel
        ) {
            composable(route = Destination.DOCUMENTATION.name) {
                DocumentationScreen(
                    docsState = documentationViewModel.documentations,
                    fetchDocs = documentationViewModel::fetchDocumentations,
                    onDocsClick = { docsItem ->
                        documentationViewModel.setSelectedDocumentation(docsItem)
                        navController.navigate(Destination.DOCUMENTATION_DETAILS.name)
                    },
                    onCreateDocumentationClick = { navController.navigate(Destination.DOCUMENTATION_CREATE.name) },
                    setSelectedDocumentation = documentationViewModel::setSelectedDocumentation,
                    clearSelectedDocumentation = documentationViewModel::clearSelectedDocumentation,
                    authenticationState = authenticationViewModel.authState,
                    authenticationStateUpdate = authenticationViewModel::updateAuthState
                )
            }

            composable(route = Destination.DOCUMENTATION_DETAILS.name) {
                DocumentationDetailsScreen(
                    setSelectedDoc = documentationViewModel::setSelectedDocumentation,
                    deleteDocItem = documentationViewModel::deleteDocs,
                    selectedDoc = documentationViewModel.selectedDocumentation.value,
                    navigateToDocumentationUpdate = { navController.navigate(Destination.DOCUMENTATION_UPDATE.name) },
                    popNavigationBackStack = navController::popBackStack,
                    authenticationState = authenticationViewModel.authState,
                    authenticationStateUpdate = authenticationViewModel::updateAuthState
                )
            }

            composable(route = Destination.DOCUMENTATION_CREATE.name) {
                CreateDocumentationScreen(
                    insertDocumentation = documentationViewModel::insertDocs,
                    navControllerNavigateUp = { navController.navigateUp() }
                )
            }

            composable(route = Destination.DOCUMENTATION_UPDATE.name) {
                UpdateDocumentationScreen(
                    selectedDoc = documentationViewModel.selectedDocumentation.value,
                    saveDocChanges = documentationViewModel::saveDocumentationChanges,
                    navigateToDocs = { navController.navigate(Destination.DOCUMENTATION.name) }
                )
            }
        }

        /**
         * The FAQ routes.
         *
         * @param navController The navigation controller.
         * @param authenticationViewModel The authentication status view model.
         * @param faqViewModel The FAQ view model.
         * @author frigvid
         * @created 2024-11-06
         */
        private fun NavGraphBuilder.faqRoutes(
            navController: NavController,
            authenticationViewModel: AuthenticationViewModel,
            faqViewModel: FAQViewModel
        ) {
            composable(route = Destination.FAQ.name) {
                FAQScreen(
                    faqState = faqViewModel.faq,
                    fetchFaq = faqViewModel::fetchFAQ,
                    onFaqClick = { faqItem ->
                        faqViewModel.setSelectedFAQ(faqItem)
                        navController.navigate(Destination.FAQ_DETAILS.name) },
                    onCreateFAQClick = { navController.navigate(Destination.FAQ_CREATE.name) },
                    setSelectedFAQ = faqViewModel::setSelectedFAQ,
                    clearSelectedFAQ = faqViewModel::clearSelectedFAQ,
                    authenticationState = authenticationViewModel.authState,
                    authenticationStateUpdate = authenticationViewModel::updateAuthState
                )
            }

            composable(route = Destination.FAQ_CREATE.name) {
                CreateFAQScreen(
                    insertFaq = faqViewModel::insertFAQ,
                    navControllerNavigateUp = { navController.navigateUp() }
                )
            }

            composable(route = Destination.FAQ_DETAILS.name) {
                FAQDetailsScreen(
                    setSelectedFaq = faqViewModel::setSelectedFAQ,
                    deleteFaqItem = faqViewModel::deleteFAQ,
                    selectedFaq = faqViewModel.selectedFAQ.value,
                    navigateToFaqUpdate = { navController.navigate(Destination.FAQ_UPDATE.name) },
                    popNavigationBackStack = navController::popBackStack,
                    authenticationState = authenticationViewModel.authState,
                    authenticationStateUpdate = authenticationViewModel::updateAuthState
                )
            }

            composable(route = Destination.FAQ_UPDATE.name) {
                UpdateFAQScreen(
                    selectedFaq = faqViewModel.selectedFAQ.value,
                    saveFaqChanges = faqViewModel::saveFAQChanges,
                    navigateToFaq = { navController.navigate(Destination.FAQ.name) }
                )
            }
        }

        /**
         * The news routes.
         *
         * @param navController The navigation controller.
         * @param authenticationViewModel The authentication status ViewModel.
         * @param newsViewModel The news ViewModel.
         * @author frigvid
         * @created 2024-11-06
         */
        private fun NavGraphBuilder.newsRoutes(
            navController: NavController,
            authenticationViewModel: AuthenticationViewModel,
            newsViewModel: NewsViewModel
        ) {
            composable(route = Destination.NEWS.name) {
                NewsScreen(
                    newsState = newsViewModel.news,
                    fetchNews = { newsViewModel.fetchNews() },
                    onNewsClick = { newsItem ->
                        newsViewModel.setSelectedNews(newsItem)
                        navController.navigate(Destination.NEWS_DETAILS.name)
                    },
                    onCreateNewsClick = { navController.navigate(Destination.NEWS_CREATE.name) },
                    setSelectedNews = newsViewModel::setSelectedNews,
                    clearSelectedNews = newsViewModel::clearSelectedNews,
                    authenticationState = authenticationViewModel.authState,
                    authenticationStateUpdate = authenticationViewModel::updateAuthState
                )
            }

        composable(route = Destination.NEWS_DETAILS.name) {
                NewsDetailsScreen(
                    setSelectedNews = newsViewModel::setSelectedNews,
                    deleteNewsItem = newsViewModel::deleteNews,
                    selectedNews = newsViewModel.selectedNews.value,
                    navigateToNewsUpdate = { navController.navigate(Destination.NEWS_UPDATE.name) },
                    navControllerPopBackStack = navController::popBackStack,
                    authenticationState = authenticationViewModel.authState,
                    authenticationStateUpdate = authenticationViewModel::updateAuthState
                )
            }

            composable(route = Destination.NEWS_CREATE.name) {
                CreateNewsScreen(
                    insertNews = newsViewModel::insertNews,
                    navControllerNavigateUp = { navController.navigateUp() }
                )
            }

            composable(route = Destination.NEWS_UPDATE.name) {
                UpdateNewsScreen(
                    selectedNews = newsViewModel.selectedNews.value,
                    saveNewsChanges = newsViewModel::saveNewsChanges,
                    navigateToNews = { navController.navigate(Destination.NEWS.name) },
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
         * @param openingsViewModel The openings ViewModel.
         * @param groupsViewModel The groups ViewModel.
         * @param chessBoardViewModel The chess board ViewModel.
         * @param authenticationViewModel The authentication state ViewModel.
         * @author frigvid
         * @created 2024-11-06
         */
        operator fun invoke(
            navGraphBuilder: NavGraphBuilder,
            navController: NavController,
            openingsViewModel: OpeningsViewModel,
            groupsViewModel: GroupsViewModel,
            chessBoardViewModel: ChessBoardViewModel,
            authenticationViewModel: AuthenticationViewModel
        ): Game {
            navGraphBuilder.openingRoutes(
                navController,
                openingsViewModel,
                chessBoardViewModel,
                authenticationViewModel
            )

            navGraphBuilder.repositoryRoutes(
                navController,
                openingsViewModel,
                groupsViewModel,
                chessBoardViewModel,
                authenticationViewModel
            )

            navGraphBuilder.gameRoutes(
                navController,
                chessBoardViewModel,
                openingsViewModel
            )

            return this
        }

        /**
         * The chess openings routes.
         *
         * @param navController The navigation controller.
         * @param openingsViewModel The openings ViewModel.
         * @param chessBoardViewModel The chess board ViewModel.
         * @param authenticationViewModel The authentication state ViewModel.
         * @author frigvid
         * @created 2024-11-06
         */
        private fun NavGraphBuilder.openingRoutes(
            navController: NavController,
            openingsViewModel: OpeningsViewModel,
            chessBoardViewModel: ChessBoardViewModel,
            authenticationViewModel: AuthenticationViewModel
        ) {
            composable(route = Destination.OPENINGS.name) {
                OpeningsScreen(
                    authenticationState = authenticationViewModel.authState,
                    authenticationStateUpdate = authenticationViewModel::updateAuthState,
                    openingsStartPeriodicUpdates = openingsViewModel::startPeriodicUpdates,
                    setSelectedOpening = openingsViewModel::setSelectedOpening,
                    openings = openingsViewModel.openings.value,
                    onGroupsClick = { navController.navigate(Destination.GROUPS.name) },
                    onCreateOpeningClick = { navController.navigate(Destination.OPENINGS_CREATE.name) },
                    onOpeningClick = { navController.navigate(Destination.OPENING_DETAILS.name) }
                )
            }

            composable(route = Destination.OPENINGS_CREATE.name) {
                CreateOpeningScreen(
                    authenticationState = authenticationViewModel.authState,
                    authenticationStateUpdate = authenticationViewModel::updateAuthState,
                    openingsStartPeriodicUpdates = openingsViewModel::startPeriodicUpdates,
                    onSaveOpeningClick = openingsViewModel::createOpening,
                    popNavigationBackStack = navController::popBackStack
                )
            }

            composable(route = Destination.OPENINGS_UPDATE.name) {
                UpdateOpeningScreen(
                    authenticationState = authenticationViewModel.authState,
                    authenticationStateUpdate = authenticationViewModel::updateAuthState,
                    opening = openingsViewModel.selectedOpening.value,
                    onUpdateOpeningClick = openingsViewModel::updateOpening,
                    openingsStartPeriodicUpdates = openingsViewModel::startPeriodicUpdates,
                    popNavigationBackStack = navController::popBackStack
                )
            }

            composable(route = Destination.OPENING_DETAILS.name) {
                OpeningDetailsScreen(
                    authenticationState = authenticationViewModel.authState,
                    authenticationStateUpdate = authenticationViewModel::updateAuthState,
                    opening = openingsViewModel.selectedOpening.value,
                    navigateToPlayScreen = { navController.navigate(Destination.PLAY.name) },
                    onDeleteOpeningClick = openingsViewModel::deleteOpening,
                    setSelectedOpening = openingsViewModel::setSelectedOpening,
                    setSelectedBoardOpenings = chessBoardViewModel::setSelectedBoardOpenings,
                    navigateToOpeningEditor = { navController.navigate(Destination.OPENINGS_UPDATE.name) },
                    popNavigationBackStack = navController::popBackStack
                )
            }
        }

        /**
         * The chess repository/groups routes.
         *
         * @param navController The navigation controller.
         * @param groupsViewModel The groups ViewModel.
         * @param openingsViewModel The openings ViewModel.
         * @param chessBoardViewModel The chess board ViewModel.
         * @param authenticationViewModel The authentication state ViewModel.
         * @author frigvid
         * @created 2024-11-06
         */
        private fun NavGraphBuilder.repositoryRoutes(
            navController: NavController,
            openingsViewModel: OpeningsViewModel,
            groupsViewModel: GroupsViewModel,
            chessBoardViewModel: ChessBoardViewModel,
            authenticationViewModel: AuthenticationViewModel
        ) {
            composable(route = Destination.GROUPS.name) {
                GroupsScreen(
                    groupsList = groupsViewModel.groups.value,
                    openingsStartPeriodicUpdates = openingsViewModel::startPeriodicUpdates,
                    groupsStartPeriodicUpdates = groupsViewModel::startPeriodicUpdates,
                    onOpeningSelect = openingsViewModel::setSelectedOpening,
                    setSelectedGroup = groupsViewModel::setSelectedGroup,
                    setSelectedBoardOpenings = chessBoardViewModel::setSelectedBoardOpenings,
                    onGroupDelete = groupsViewModel::deleteGroup,
                    onNavigateToGroupCreation = { navController.navigate((Destination.GROUPS_CREATE.name)) },
                    onNavigateToGroupEditing = { navController.navigate(Destination.GROUPS_UPDATE.name) },
                    onNavigateToOpeningDetails = { navController.navigate(Destination.OPENING_DETAILS.name) }
                )
            }

            composable(route = Destination.GROUPS_CREATE.name) {
                CreateGroupScreen(
                    authenticationState = authenticationViewModel.authState,
                    authenticationStateUpdate = authenticationViewModel::updateAuthState,
                    availableOpenings = openingsViewModel.openings.value,
                    onCreateGroup = groupsViewModel::createGroup,
                    navControllerPopBackStack = navController::popBackStack
                )
            }

            composable(route = Destination.GROUPS_UPDATE.name) {
                UpdateGroupScreen(
                    group = groupsViewModel.selectedGroup.value,
                    availableOpenings = openingsViewModel.openings.value,
                    onUpdateGroupClick = groupsViewModel::updateGroup,
                    navControllerPopBackStack = navController::popBackStack
                )
            }
        }

        /**
         * The chess game and history routes.
         *
         * @param navController The navigation controller.
         * @param chessBoardViewModel The chess board ViewModel.
         * @param openingsViewModel The openings ViewModel.
         * @author frigvid
         * @created 2024-11-06
         */
        private fun NavGraphBuilder.gameRoutes(
            navController: NavController,
            chessBoardViewModel: ChessBoardViewModel,
            openingsViewModel: OpeningsViewModel
        ) {
            composable(route = Destination.PLAY.name) {
                PlayScreen(
                    openingsList = chessBoardViewModel.selectedBoardOpenings.value,
                    boardState = chessBoardViewModel.boardState,
                    gameState = chessBoardViewModel.gameState,
                    onResetBoardClick = chessBoardViewModel::resetGame,
                    onUndoMoveClick = chessBoardViewModel::undoLastMove
                )
            }

            composable(route = Destination.HISTORY.name) {
                HistoryScreen(
                    setSelectedOpening = openingsViewModel::setSelectedOpening,
                    getOpeningById = openingsViewModel::getOpeningById,
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
         * @param changeEmailViewModel The account email change ViewModel.
         * @param changePasswordViewModel The account password change ViewModel.
         * @param deleteAccountViewModel The account deletion ViewModel.
         * @author frigvid
         * @created 2024-11-06
         */
        operator fun invoke(
            navGraphBuilder: NavGraphBuilder,
            navController: NavController,
            cbViewModel: CBViewModel,
            logoutViewModel: LogoutViewModel,
            deleteAccountViewModel: DeleteAccountViewModel,
            changeEmailViewModel: ChangeEmailViewModel,
            changePasswordViewModel: ChangePasswordViewModel,
            authenticationViewModel: AuthenticationViewModel
        ): Settings {
            navGraphBuilder.composable(route = Destination.SETTINGS.name) {
                SettingsScreen(
                    logoutState = logoutViewModel.logoutState,
                    logoutStateReset = logoutViewModel::resetState,
                    changeEmailState = changeEmailViewModel.changeEmailState,
                    changePasswordState = changePasswordViewModel.changePasswordState,
                    authenticationState = authenticationViewModel.authState,
                    onLogoutClick = logoutViewModel::logout,
                    onLoginClick = { navController.navigate(Destination.AUTH_LOGIN.name) },
                    accountDeleteState = deleteAccountViewModel.deleteAccountState,
                    accountDeleteStateReset = deleteAccountViewModel::resetState,
                    authenticationStateUpdate = authenticationViewModel::updateAuthState,
                    onDeleteAccountClick = deleteAccountViewModel::deleteAccount,
                    onAdminDashboardClick = { navController.navigate(Destination.ADMIN_DASHBOARD.name) },
                    selectedTheme = cbViewModel.selectedTheme.value,
                    selectedLanguage = cbViewModel.selectedLanguage.value,
                    onThemeChange = cbViewModel::setSelectedTheme,
                    onLanguageChange = cbViewModel::setSelectedLanguage,
                    navigateToPasswordReset = { navController.navigate(Destination.AUTH_RESET.name) },
                    navigateToEmailChange = { navController.navigate(Destination.AUTH_EMAIL_CHANGE.name) }
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
         * @param changeEmailViewModel The e-mail change ViewModel.
         * @param changePasswordViewModel The password change ViewModel.
         * @param forgotPasswordViewModel The forgotten password request ViewModel.
         * @param registrationViewModel The registration ViewModel.
         * @author frigvid
         * @contributor Anarox
         * @created 2024-11-06
         */
        operator fun invoke(
            navGraphBuilder: NavGraphBuilder,
            navController: NavController,
            loginViewModel: LoginViewModel,
            registrationViewModel: RegistrationViewModel,
            changeEmailViewModel: ChangeEmailViewModel,
            changePasswordViewModel: ChangePasswordViewModel,
            forgotPasswordViewModel: ForgotPasswordViewModel,
            authenticationViewModel: AuthenticationViewModel
        ): Authentication {
            navGraphBuilder.composable(route = Destination.AUTH_LOGIN.name) {
                LoginScreen(
                    loginState = loginViewModel.loginState,
                    forgotPasswordState = forgotPasswordViewModel.forgotPasswordState,
                    loginStateReset = loginViewModel::resetState,
                    navigateHome = { navController.navigate(Destination.HOME.name) },
                    onLoginClick = loginViewModel::login,
                    onCreateUserClick = { navController.navigate(Destination.AUTH_CREATE.name)},
                    onForgotPasswordClick = { navController.navigate(Destination.AUTH_FORGOT.name) },
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
                    onForgotPasswordClick = forgotPasswordViewModel::forgotPassword,
                    forgotPasswordStateUpdate = forgotPasswordViewModel::updateState,
                    navControllerPopBackStack = navController::popBackStack
                )
            }

            navGraphBuilder.composable(route = Destination.AUTH_RESET.name) {
                ResetPasswordScreen(
                    onResetPasswordClick = { navController.navigate(Destination.HOME.name) },
                    navControllerPopBackStack = navController::popBackStack,
                    authenticationStateUpdate = authenticationViewModel::updateAuthState,
                    changePasswordStateUpdate = changePasswordViewModel::updateState
                )
            }

            /**
             * Routing with Deeplinking if the external webdomain is matching the URI. The URI holds the navArguments:
             * @see navArgument that contains a token, a type and next which identifies the specific link sent to the email
             * of the user.
             *
             * @author Anarox
             * @created 2024-11-11
             */
            navGraphBuilder.composable(
                route = Destination.AUTH_PASSWORD_RESET_VIA_EMAIL.name + "?token_hash={token_hash}&type={type}&next={next}",
                arguments = listOf(
                    navArgument("token_hash") {
                        type = NavType.StringType; defaultValue = ""
                    },
                    navArgument("type") {
                        type = NavType.StringType; defaultValue = ""
                    },
                    navArgument("next") {
                        type = NavType.StringType; defaultValue = ""
                    }
                ),
                deepLinks = listOf(
                    navDeepLink { uriPattern = "https://a2g11.vercel.app/confirm?token_hash={token_hash}&type={type}&next={next}" }
                )
            ) {
                /**
                 * backStackEntry extracts the passed parameters through the deeplink to ensure that ResetPasswordScreen
                 * recieves the correct data.
                 *
                 * If there are no arguments provided, it will use default value "" as a fallback value.
                 *
                 * @author Anarox
                 * @created 2024-11-11
                 */
                    backStackEntry ->
                val tokenHash = backStackEntry.arguments?.getString("token_hash") ?: ""
                val type = backStackEntry.arguments?.getString("type") ?: ""
                val next = backStackEntry.arguments?.getString("next") ?: ""

                Log.d("DeepLink", "tokenHash: $tokenHash, type: $type, next: $next")

                ResetPasswordViaEmailScreen(
                    tokenHash = tokenHash,
                    type = type,
                    next = next,
                    onResetPasswordClick = { navController.navigate(Destination.HOME.name) },
                    navControllerPopBackStack = navController::popBackStack,
                    authenticationStateUpdate = authenticationViewModel::updateAuthState,
                    changePasswordStateUpdate = changePasswordViewModel::updateState
                )
            }

            navGraphBuilder.composable(route = Destination.AUTH_EMAIL_CHANGE.name) {
                ChangeEmailScreen(
                    changeEmailStateUpdate = changeEmailViewModel::updateState,
                    onChangeEmailClick = changeEmailViewModel::changeEmail,
                    navControllerPopBackStack = navController::popBackStack,
                    authenticationStateUpdate = authenticationViewModel::updateAuthState
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
         * @param openingsViewModel The openings ViewModel.
         * @param groupsViewModel The groups ViewModel.
         * @author frigvid
         * @created 2024-11-06
         */
        operator fun invoke(
            navGraphBuilder: NavGraphBuilder,
            navController: NavController,
            openingsViewModel: OpeningsViewModel,
            groupsViewModel: GroupsViewModel
        ): Home {
            navGraphBuilder.composable(route = Destination.HOME.name) {
                HomeScreen(
                    onTrainClick = { navController.navigate(Destination.OPENINGS.name) },
                    onPlayClick =  { navController.navigate(Destination.PLAY.name) },
                    onHistoryClick =  { navController.navigate(Destination.HISTORY.name) },
                    openingsStartPeriodicUpdates = openingsViewModel::startPeriodicUpdates,
                    groupsStartPeriodicUpdates = groupsViewModel::startPeriodicUpdates
                )
            }

            return this
        }
    }
}
