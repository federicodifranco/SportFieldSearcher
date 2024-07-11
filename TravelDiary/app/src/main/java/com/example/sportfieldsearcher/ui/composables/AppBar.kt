package com.example.sportfieldsearcher.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.sportfieldsearcher.ui.controllers.AppViewModel
import com.example.sportfieldsearcher.ui.utils.SportFieldSearcherRoute
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    navController: NavHostController,
    currentRoute: SportFieldSearcherRoute
) {
    val routesBlackList = listOf(
        SportFieldSearcherRoute.Home,
        SportFieldSearcherRoute.Search,
        SportFieldSearcherRoute.AddField,
        SportFieldSearcherRoute.Profile,
        SportFieldSearcherRoute.Settings
    )

    val appViewModel = koinViewModel<AppViewModel>()
    val appState by appViewModel.state.collectAsStateWithLifecycle()

    fun checkRoute(route: SportFieldSearcherRoute): Boolean =
        routesBlackList.map { it.route }.contains(route.route)

    CenterAlignedTopAppBar(
        title = {
            Text(
                currentRoute.title,
                fontWeight = FontWeight.Medium,
            )
        },
        navigationIcon = {
            if (navController.previousBackStackEntry != null && !checkRoute(currentRoute)) {
                if (currentRoute == SportFieldSearcherRoute.Profile &&
                    navController.currentBackStackEntry?.arguments?.getInt("userId") == appState.userId) {
                    return@CenterAlignedTopAppBar
                }
                IconButton(onClick = {
                    if (currentRoute == SportFieldSearcherRoute.Settings) {
                        navController.popBackStack()
                    } else {
                        navController.navigateUp()
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Back button"
                    )
                }
            }
        },
        actions = {
            if (currentRoute.route == SportFieldSearcherRoute.Profile.route &&
                navController.currentBackStackEntry?.arguments?.getInt("userId") == appState.userId) {
                IconButton(onClick = { navController.navigate(SportFieldSearcherRoute.Settings.route) }) {
                    Icon(Icons.Outlined.Settings, "Settings")
                }
            }
            if (currentRoute.route == SportFieldSearcherRoute.Settings.route) {
                IconButton(onClick = {
                    appViewModel.changeUserId(null).invokeOnCompletion {
                        if (it == null) {
                            SportFieldSearcherRoute.routes.forEach { route: SportFieldSearcherRoute -> navController.popBackStack(route.route, true) }
                            navController.navigate(SportFieldSearcherRoute.Login.route)
                        }
                    }
                }) {
                    Icon(Icons.Outlined.Logout, "Logout")
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}

