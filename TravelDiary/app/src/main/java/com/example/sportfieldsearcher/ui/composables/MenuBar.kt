package com.example.sportfieldsearcher.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.sportfieldsearcher.ui.controllers.AppViewModel
import com.example.sportfieldsearcher.ui.utils.SportFieldSearcherRoute
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.getValue

@Composable
fun MenuBar(navController: NavHostController) {
    val routesBlackList = listOf(SportFieldSearcherRoute.Home, SportFieldSearcherRoute.AddField, SportFieldSearcherRoute.Settings)
    fun deleteDuplicates(route: String) {
        navController.popBackStack(route, true)
        SportFieldSearcherRoute.routes.filter { !routesBlackList.contains(it) }.forEach { navController.popBackStack(it.route, true) }
    }
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Row (modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1.0f, true))

            IconButton(onClick = {
                deleteDuplicates(SportFieldSearcherRoute.Home.route)
                navController.navigate(SportFieldSearcherRoute.Home.route)
            }) {
                Icon(Icons.Filled.Home, contentDescription = "Home", modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.weight(1.0f, true))
            IconButton(onClick = {
                deleteDuplicates(SportFieldSearcherRoute.Search.route)
                navController.navigate(SportFieldSearcherRoute.Search.route)
            }) {
                Icon(Icons.Filled.Search, contentDescription = "Search", modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.weight(1.0f, true))
            IconButton(onClick = {
                deleteDuplicates(SportFieldSearcherRoute.AddField.route)
                navController.navigate(SportFieldSearcherRoute.AddField.route)
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Field", modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.weight(1.0f, true))
            val appViewModel = koinViewModel<AppViewModel>()
            val appState by appViewModel.state.collectAsStateWithLifecycle()
            IconButton(onClick = {
                deleteDuplicates(SportFieldSearcherRoute.Profile.route)
                val userId = appState.userId ?: -1
                navController.navigate(SportFieldSearcherRoute.Profile.buildRoute(userId))
            }) {
                Icon(Icons.Filled.Person, contentDescription = "Profile", modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.weight(1.0f, true))

            IconButton(onClick = {
                deleteDuplicates(SportFieldSearcherRoute.Statistics.route)
                navController.navigate(SportFieldSearcherRoute.Statistics.route)
            }) {
                Icon(Icons.Filled.BarChart, contentDescription = "Statistics", modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.weight(1.0f, true))
        }
    }
}