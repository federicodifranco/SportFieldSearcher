package com.example.sportfieldsearcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sportfieldsearcher.data.models.Theme
import com.example.sportfieldsearcher.ui.utils.SportFieldSearcherNavGraph
import com.example.sportfieldsearcher.ui.utils.SportFieldSearcherRoute
import com.example.sportfieldsearcher.ui.composables.AppBar
import com.example.sportfieldsearcher.ui.screens.settings.SettingsScreen
import com.example.sportfieldsearcher.ui.screens.settings.SettingsViewModel
import com.example.sportfieldsearcher.ui.theme.SportFieldSearcherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val settingsviewModel by viewModels<SettingsViewModel>()
            val state by settingsviewModel.state.collectAsStateWithLifecycle()

            SportFieldSearcherTheme(
                darkTheme = when (state.theme) {
                    Theme.Light -> false
                    Theme.Dark -> true
                    Theme.System -> isSystemInDarkTheme()
                }
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val navController = rememberNavController()
                        val backStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute by remember {
                            derivedStateOf {
                                SportFieldSearcherRoute.routes.find {
                                    it.route == backStackEntry?.destination?.route
                                } ?: SportFieldSearcherRoute.Home
                            }
                        }
                        Scaffold(
                            topBar = { AppBar(navController, currentRoute) }
                        ) { contentPadding ->
                            SportFieldSearcherNavGraph(
                                navController,
                                modifier = Modifier.padding(contentPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}
