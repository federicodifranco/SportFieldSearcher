package com.example.sportfieldsearcher.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.sportfieldsearcher.data.database.Field
import com.example.sportfieldsearcher.data.database.User
import com.example.sportfieldsearcher.ui.controllers.AppViewModel
import com.example.sportfieldsearcher.ui.controllers.FieldsViewModel
import com.example.sportfieldsearcher.ui.controllers.UsersViewModel
import com.example.sportfieldsearcher.ui.screens.addfield.AddFieldScreen
import com.example.sportfieldsearcher.ui.screens.addfield.AddFieldViewModel
import com.example.sportfieldsearcher.ui.screens.home.HomeScreen
import com.example.sportfieldsearcher.ui.screens.fielddetails.FieldDetailsScreen
import com.example.sportfieldsearcher.ui.screens.profile.ProfileScreen
import com.example.sportfieldsearcher.ui.screens.register.RegistrationScreen
import com.example.sportfieldsearcher.ui.screens.register.RegistrationViewModel
import com.example.sportfieldsearcher.ui.screens.settings.SettingsScreen
import com.example.sportfieldsearcher.ui.screens.settings.SettingsViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.koinViewModel

sealed class SportFieldSearcherRoute(
    val route: String,
    val title: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {

    data object FieldDetails : SportFieldSearcherRoute(
        "fields/{fieldId}",
        "Field Details",
        listOf(navArgument("fieldId") { type = NavType.StringType })
    ) {
        fun buildRoute(fieldId: String) = "fields/$fieldId"
    }

    data object Profile : SportFieldSearcherRoute(
        "profile",
        "Profile",
        //listOf(navArgument("userId") { type = NavType.IntType })
    ) {
        //fun buildRoute(userId: Int) = "profile/$userId"
    }

    data object Home : SportFieldSearcherRoute("fields", "SportFieldSearcher")
    data object AddField : SportFieldSearcherRoute("fields/add", "Add Field")
    data object Settings : SportFieldSearcherRoute("settings", "Settings")
    data object Registration : SportFieldSearcherRoute("registration", "Registration")

    companion object {
        val routes = setOf(Home, FieldDetails, AddField, Settings, Profile, Registration)
    }
}

@Composable
fun SportFieldSearcherNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val usersViewModel = koinViewModel<UsersViewModel>()
    val usersState by usersViewModel.state.collectAsStateWithLifecycle()
    val fieldsVM = koinViewModel<FieldsViewModel>()
    val fieldsState by fieldsVM.state.collectAsStateWithLifecycle()
    val settingsviewModel = koinViewModel<SettingsViewModel>()
    val SettingState by settingsviewModel.state.collectAsStateWithLifecycle()
    val appViewModel = koinViewModel<AppViewModel>()
    val appState by appViewModel.state.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = SportFieldSearcherRoute.Home.route,
        modifier = modifier
    ) {
        with(SportFieldSearcherRoute.Home) {
            composable(route) {
                HomeScreen(fieldsState, navController)
            }
        }
        with(SportFieldSearcherRoute.FieldDetails) {
            composable(route, arguments) { backStackEntry ->
                val field = requireNotNull(fieldsState.fields.find { field ->
                    field.id == backStackEntry.arguments?.getString("fieldId")?.toInt()
                })
                FieldDetailsScreen(field)
            }
        }
        with(SportFieldSearcherRoute.AddField) {
            composable(route) {
                val addFieldVm = koinViewModel<AddFieldViewModel>()
                val state by addFieldVm.state.collectAsStateWithLifecycle()
                AddFieldScreen(
                    state,
                    addFieldVm.actions,
                    onCreate = {
                        fieldsVM.addField(Field(
                            name = state.location,
                            date = state.date,
                            description = state.description
                        ))
                    },
                    navController = navController
                )
            }
        }
        with(SportFieldSearcherRoute.Settings) {
            composable(route) {
                SettingsScreen(
                    state = SettingState,
                    navController = navController,
                    changeTheme = settingsviewModel::changeTheme
                )
            }
        }
        with(SportFieldSearcherRoute.Profile) {
            composable(route, arguments) { backStackEntry: NavBackStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId")
                // Create a temporary user
                val tempUser = User(
                    userId = userId ?: 0,
                    username = "Temporary User",
                    email = "",
                    password = "",
                    profilePicture = null
                )
                ProfileScreen(user = tempUser, navController = navController)
            }
        }
        with(SportFieldSearcherRoute.Registration) {
            composable(route) {
                val registrationViewModel = koinViewModel<RegistrationViewModel>()
                val state by registrationViewModel.state.collectAsStateWithLifecycle()
                RegistrationScreen(
                    usersState = usersState,
                    state = state,
                    actions = registrationViewModel.actions,
                    onSubmit = {
                        usersViewModel.addUser(state.createUser()).invokeOnCompletion {
                            onQueryComplete(
                                usersViewModel.getUserOnLogin(
                                    email = state.email,
                                    password = state.password
                                ),
                                onComplete = { result: Any ->
                                    appViewModel.changeUserId((result as User).userId)
                                        .invokeOnCompletion {
                                            if (it == null)
                                                navigateAndClearBackstack(route, SportFieldSearcherRoute.Home.route, navController)
                                        }
                                },
                                checkResult = { result: Any? ->
                                    result != null && result is User
                                }
                            )
                        }
                    },
                    navController = navController
                )
            }
        }
    }
}
fun navigateAndClearBackstack(
    currentRoute: String,
    destination: String,
    navController: NavHostController
) {
    navController.popBackStack(
        route = currentRoute,
        inclusive = true
    )
    navController.navigate(destination)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun onQueryComplete(
    result: Deferred<Any?>,
    onComplete: (Any) -> Unit,
    checkResult: (Any?) -> Boolean
) {
    result.invokeOnCompletion {
        if (it == null) {
            if (checkResult(result.getCompleted()))
                onComplete(result.getCompleted()!!)
        }
    }
}
