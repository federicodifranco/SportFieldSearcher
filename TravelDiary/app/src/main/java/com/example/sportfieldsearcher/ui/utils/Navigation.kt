package com.example.sportfieldsearcher.ui.utils

import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.sportfieldsearcher.data.database.Field
import com.example.sportfieldsearcher.ui.FieldsViewModel
import com.example.sportfieldsearcher.ui.screens.addfield.AddFieldScreen
import com.example.sportfieldsearcher.ui.screens.addfield.AddFieldViewModel
import com.example.sportfieldsearcher.ui.screens.home.HomeScreen
import com.example.sportfieldsearcher.ui.screens.fielddetails.FieldDetailsScreen
import com.example.sportfieldsearcher.ui.screens.settings.SettingState
import com.example.sportfieldsearcher.ui.screens.settings.SettingsScreen
import com.example.sportfieldsearcher.ui.screens.settings.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

sealed class SportFieldSearcherRoute(
    val route: String,
    val title: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    data object Home : SportFieldSearcherRoute("fields", "SportFieldSearcher")
    data object FieldDetails : SportFieldSearcherRoute(
        "fields/{fieldId}",
        "Field Details",
        listOf(navArgument("fieldId") { type = NavType.StringType })
    ) {
        fun buildRoute(fieldId: String) = "fields/$fieldId"
    }
    data object AddField : SportFieldSearcherRoute("fields/add", "Add Field")
    data object Settings : SportFieldSearcherRoute("settings", "Settings")

    companion object {
        val routes = setOf(Home, FieldDetails, AddField, Settings)
    }
}

@Composable
fun SportFieldSearcherNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val fieldsVM = koinViewModel<FieldsViewModel>()
    val fieldsState by fieldsVM.state.collectAsStateWithLifecycle()
    val settingsviewModel = koinViewModel<SettingsViewModel>()
    val SettingState by settingsviewModel.state.collectAsStateWithLifecycle()

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
    }
}
