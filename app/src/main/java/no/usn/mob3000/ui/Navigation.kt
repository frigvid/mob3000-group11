package no.usn.mob3000.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
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
import no.usn.mob3000.ui.components.base.Routes

/**
 * The LocalNavController was borne out of necessity, to allow for a more dynamic UI where
 * individual screens can define parts of the viewport. Normally, one wouldn't pass around the
 * NavController directly, and that's why we're it's done using local composition, to avoid
 * callback hell.
 *
 * As an aside, this still shouldn't be used to pass around to individual screens unless absolutely
 * necessary. In most cases, you merely need to use "navController.navigate()".
 *
 * @author frigvid
 * @created 2024-10-02
 */
val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController found!") }

/**
 * The App function serves as the main navigation point for the application. This is where you
 * define an actual route, its underlying destinations, and whatever else is necessary.
 *
 * To define a route, three things are necessary.
 * 1. You need to create a file called <something>Screen.kt. This is somewhere under ui/screens/.
 * 2. You need to add a destination definition to the enum class [Destination].
 * 3. You need to define a composable route definition within the NavHost.
 *
 * When creating a new [Composable] screen, it's important to remember to use the [Viewport] as
 * the root of the screen. If you don't use it, you're going to have a bad time. The reason you'd
 * want to use it, is to allow for individual screens to replace the topBars or bottomBars, or
 * just add certain parts to the topBar without fully replacing it. Below is a very, very basic
 * example of a given Jetpack Compose screen.
 * ```
 * @Composable
 * fun CreateOpeningScreen() {
 *     Viewport { innerPadding ->
 *         Box(
 *             modifier = Modifier.fillMaxSize()
 *                                .padding(innerPadding),
 *             contentAlignment = Alignment.Center
 *         ) {
 *             Text("Create opening screen. Stub.")
 *         }
 *     }
 * }
 * ```
 *
 * For step 2, you'll need to register the destination definition in the enum class [Destination].
 *
 * Finally, to define the actual navigation logic, you'll need to create a composable route
 * definition. This consists of 2 things, at the most basic level. A parameter where you give the
 * destination definition from [Destination], and a body, where you call for the function you made in
 * step 1. Below is an example of this:
 * ```
 * composable(route = Screen.DOCUMENTATION.name) { DocumentationScreen() }
 * ```
 *
 * As you can see, the parameter is "route", and it uses the the string value of the
 * [Destination.DOCUMENTATION]. This is what decides where it goes, and if it has an icon.
 * Within the body, you call the function for the screen you made in step 1.
 *
 * @param viewModel The generic application ViewModel.
 * @param loginViewModel The login state ViewModel.
 * @param logoutViewModel The logout state ViewModel.
 * @param registrationViewModel The registration state ViewModel.
 * @param deleteAccountViewModel The account deletion state ViewModel.
 * @param navController The navigation controller.
 * @see Destination
 * @see Viewport
 * @see ScreenIcon
 * @see Icon
 * @author frigvid
 * @contributors Routes: Anarox1111, Markus
 * @created 2024-09-24
 */
@Composable
fun Navigation(
    viewModel: CBViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel(),
    logoutViewModel: LogoutViewModel = viewModel(),
    changeEmailViewModel: ChangeEmailViewModel = viewModel(),
    changePasswordViewModel: ChangePasswordViewModel = viewModel(),
    registrationViewModel: RegistrationViewModel = viewModel(),
    forgotPasswordViewModel: ForgotPasswordViewModel = viewModel(),
    deleteAccountViewModel: DeleteAccountViewModel = viewModel(),
    authenticationViewModel: AuthenticationViewModel = viewModel(),
    documentationViewModel: DocumentationViewModel = viewModel(),
    newsViewModel: NewsViewModel = viewModel(),
    faqViewModel: FAQViewModel = viewModel(),
    openingsViewModel: OpeningsViewModel = viewModel(),
    groupsViewModel: GroupsViewModel = viewModel(),
    chessBoardViewModel: ChessBoardViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController = navController,
            startDestination = Destination.HOME.name,
            modifier = Modifier.fillMaxSize()
        ) {
            Routes.Home(
                this,
                navController,
                openingsViewModel,
                groupsViewModel
            )

            Routes.Information(
                this,
                navController,
                authenticationViewModel,
                documentationViewModel,
                faqViewModel,
                newsViewModel
            )

            Routes.Game(
                this,
                navController,
                openingsViewModel,
                groupsViewModel,
                chessBoardViewModel,
                authenticationViewModel
            )

            Routes.UserProfile(
                this,
                navController
            )

            Routes.Settings(
                this,
                navController,
                viewModel,
                logoutViewModel,
                deleteAccountViewModel,
                changeEmailViewModel,
                changePasswordViewModel,
                authenticationViewModel
            )

            Routes.Authentication(
                this,
                navController,
                loginViewModel,
                registrationViewModel,
                changeEmailViewModel,
                changePasswordViewModel,
                forgotPasswordViewModel,
                authenticationViewModel
            )

            Routes.Administrator(this)
        }
    }
}
