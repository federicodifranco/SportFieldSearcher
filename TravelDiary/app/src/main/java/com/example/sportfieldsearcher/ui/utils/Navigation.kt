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
import com.example.sportfieldsearcher.data.database.entities.CategoryType
import com.example.sportfieldsearcher.data.database.entities.Field
import com.example.sportfieldsearcher.data.database.entities.FieldWithUsers
import com.example.sportfieldsearcher.data.database.entities.PrivacyType
import com.example.sportfieldsearcher.data.database.entities.User
import com.example.sportfieldsearcher.data.database.entities.UserWithFields
import com.example.sportfieldsearcher.ui.controllers.AppViewModel
import com.example.sportfieldsearcher.ui.controllers.FieldsViewModel
import com.example.sportfieldsearcher.ui.controllers.UsersViewModel
import com.example.sportfieldsearcher.ui.screens.addfield.AddFieldScreen
import com.example.sportfieldsearcher.ui.screens.addfield.AddFieldViewModel
import com.example.sportfieldsearcher.ui.screens.fielddetails.FieldDetailsScreen
import com.example.sportfieldsearcher.ui.screens.fieldmap.FieldMapScreen
import com.example.sportfieldsearcher.ui.screens.home.HomeScreen
import com.example.sportfieldsearcher.ui.screens.login.LoginScreen
import com.example.sportfieldsearcher.ui.screens.login.LoginViewModel
import com.example.sportfieldsearcher.ui.screens.profile.ProfileScreen
import com.example.sportfieldsearcher.ui.screens.register.RegistrationScreen
import com.example.sportfieldsearcher.ui.screens.register.RegistrationViewModel
import com.example.sportfieldsearcher.ui.screens.search.SearchScreen
import com.example.sportfieldsearcher.ui.screens.settings.SettingsScreen
import com.example.sportfieldsearcher.ui.screens.statistics.StatisticsScreen
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
        listOf(navArgument("fieldId") { type = NavType.IntType })
    ) {
        fun buildRoute(fieldId: Int) = "fields/$fieldId"
    }

    data object Profile : SportFieldSearcherRoute(
        "profile/{userId}",
        "Profile",
        listOf(navArgument("userId") { type = NavType.IntType })
    ) {
        fun buildRoute(userId: Int) = "profile/$userId"
    }

    data object FieldsMap: SportFieldSearcherRoute("map", "Fields Map")
    data object Login : SportFieldSearcherRoute("login", "Login")
    data object Home : SportFieldSearcherRoute("fields", "Fields")
    data object AddField : SportFieldSearcherRoute("add", "Add Field")
    data object Settings : SportFieldSearcherRoute("settings", "Settings")
    data object Registration : SportFieldSearcherRoute("registration", "Registration")
    data object Search : SportFieldSearcherRoute("search", "Search")
    data object Statistics: SportFieldSearcherRoute("statistics", "Statistics")

    companion object {
        val routes = setOf(Home, FieldDetails, FieldsMap, AddField, Settings, Profile, Registration, Login, Search, Statistics)
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
    val appViewModel = koinViewModel<AppViewModel>()
    val appState by appViewModel.state.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = SportFieldSearcherRoute.Login.route,
        modifier = modifier
    ) {
        with(SportFieldSearcherRoute.Home) {
            composable(route) {
                if (appState.userId == null) {
                    navigateAndClearBackstack(route, SportFieldSearcherRoute.Login.route, navController)
                    return@composable
                }
                val privateFields =
                    fieldsState.fieldsWithUsers.filter { it.field.privacyType == PrivacyType.PRIVATE }
                val publicFields =
                    fieldsState.fieldsWithUsers.filter { it.field.privacyType == PrivacyType.PUBLIC }
                HomeScreen(
                    publicFields = publicFields,
                    privateFields = privateFields,
                    navController = navController,
                )
            }
        }
        with(SportFieldSearcherRoute.FieldDetails) {
            composable(route, arguments) { backStackEntry ->
                if (appState.userId == null) {
                    navigateAndClearBackstack(route, SportFieldSearcherRoute.Login.route, navController)
                    return@composable
                }
                var fieldWithUsers by remember {
                    mutableStateOf(
                        FieldWithUsers(
                            field = Field(
                                fieldId = -1,
                                name = "",
                                description = "",
                                date = "",
                                category = CategoryType.NONE,
                                privacyType = PrivacyType.NONE,
                                fieldAddedId = -1,
                                city = "",
                                fieldPicture = null
                            ),
                            connection = emptyList()
                        )
                    )
                }
                var user by remember {
                    mutableStateOf(
                        User(
                            userId = -1,
                            username = "",
                            email = "",
                            password = "",
                            profilePicture = null
                        )
                    )
                }
                var isFieldCoroutineFinished by remember { mutableStateOf(false) }
                var isUserCoroutineFinished by remember { mutableStateOf(false) }
                onQueryComplete(
                    result = fieldsVM.getFieldWithUsersById(
                        backStackEntry.arguments?.getInt("fieldId") ?: -1
                    ),
                    onComplete = { result: Any ->
                        fieldWithUsers = result as FieldWithUsers
                        isFieldCoroutineFinished = true
                    },
                    checkResult = { result: Any? ->
                        result != null && result is FieldWithUsers
                    }
                )
                onQueryComplete(
                    result = usersViewModel.getUserInfo(fieldWithUsers.field.fieldAddedId),
                    onComplete = { result: Any ->
                        user = result as User
                        isUserCoroutineFinished = true
                    },
                    checkResult = { result: Any? ->
                        result != null && result is User
                    }
                )
                if (isFieldCoroutineFinished && isUserCoroutineFinished) {
                    FieldDetailsScreen(
                        fieldWithUsers,
                        fieldAdded = user,
                        navController = navController,
                        loggedUserId = appState.userId!!,
                        onDelete = {
                            fieldsVM.deleteField(fieldWithUsers.field)
                            navController.navigateUp()
                        }
                    )
                }
            }
        }
        with(SportFieldSearcherRoute.Statistics) {
            composable(route) {
                if (appState.userId == null) {
                    navigateAndClearBackstack(route, SportFieldSearcherRoute.Login.route, navController)
                    return@composable
                }
                StatisticsScreen(fields = fieldsState.fieldsWithUsers)
            }
        }
        with(SportFieldSearcherRoute.AddField) {
            composable(route) {
                if (appState.userId == null) {
                    navigateAndClearBackstack(route, SportFieldSearcherRoute.Login.route, navController)
                    return@composable
                }
                val addFieldVm = koinViewModel<AddFieldViewModel>()
                val state by addFieldVm.state.collectAsStateWithLifecycle()
                AddFieldScreen(
                    state = state,
                    actions = addFieldVm.actions,
                    onSubmit = {
                        fieldsVM.addField(state.toField(appState.userId!!))
                        navController.navigateUp()
                    }
                )
            }
        }
        with(SportFieldSearcherRoute.Search) {
            composable(route) {
                if (appState.userId == null) {
                    navigateAndClearBackstack(route, SportFieldSearcherRoute.Login.route, navController)
                    return@composable
                }
                SearchScreen(
                    fieldsState = fieldsState,
                    usersState = usersState,
                    navController = navController
                )
            }
        }
        with(SportFieldSearcherRoute.Settings) {
            composable(route) {
                if (appState.userId == null) {
                    navigateAndClearBackstack(route, SportFieldSearcherRoute.Login.route, navController)
                    return@composable
                }
                SettingsScreen(
                    state = appState,
                    changeTheme = appViewModel::changeTheme
                )
            }
        }
        with(SportFieldSearcherRoute.Profile) {
            composable(route, arguments) { backStackEntry: NavBackStackEntry ->
                if (appState.userId == null) {
                    navigateAndClearBackstack(route, SportFieldSearcherRoute.Login.route, navController)
                    return@composable
                }
                var userWithFields by remember {
                    mutableStateOf(
                        UserWithFields(
                            user = User(
                                userId = -1,
                                username = "",
                                email = "",
                                password = "",
                                profilePicture = null
                            ),
                            fields = emptyList(),
                            createdFields = emptyList()
                        )
                    )
                }
                var isCoroutineFinished by remember { mutableStateOf(false) }

                onQueryComplete(
                    result = usersViewModel.getUserWithFieldsById(
                        backStackEntry.arguments?.getInt(
                            "userId"
                        ) ?: -1
                    ),
                    onComplete = { result: Any ->
                        userWithFields = result as UserWithFields
                        isCoroutineFinished = true
                    },
                    checkResult = { result: Any? ->
                        result != null && result is UserWithFields
                    }
                )
                if (isCoroutineFinished) {
                    ProfileScreen(
                        user = userWithFields.user,
                        fields = userWithFields.createdFields,
                        navController = navController
                    )
                }
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
        with(SportFieldSearcherRoute.Login) {
            composable(route) {
                val loginViewModel = koinViewModel<LoginViewModel>()
                val state by loginViewModel.state.collectAsStateWithLifecycle()
                if (appState.userId != null) {
                    navController.popBackStack()
                    navController.navigate(SportFieldSearcherRoute.Home.route)
                } else {
                    LoginScreen(
                        state = state,
                        actions = loginViewModel.actions,
                        onLogin = { email: String, password: String, errorMessage: () -> Unit ->
                            onQueryComplete(
                                usersViewModel.getUserOnLogin(email = email, password = password),
                                onComplete = { result: Any ->
                                    appViewModel.changeUserId((result as User).userId)
                                        .invokeOnCompletion {
                                            if (it == null)
                                                navigateAndClearBackstack(route, SportFieldSearcherRoute.Home.route, navController)
                                        }
                                },
                                checkResult = { result: Any? ->
                                    val check = result != null && result is User
                                    if (!check) {
                                        errorMessage()
                                    }
                                    return@onQueryComplete check
                                }
                            )
                        },
                        navController = navController
                    )
                }
            }
        }
        with(SportFieldSearcherRoute.FieldsMap) {
            composable(route) {
                if (appState.userId == null) {
                    navigateAndClearBackstack(route, SportFieldSearcherRoute.Login.route, navController)
                    return@composable
                }
                FieldMapScreen(
                    fieldsState = fieldsState,
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
