package com.example.sportfieldsearcher.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.example.sportfieldsearcher.data.database.User
import com.example.sportfieldsearcher.ui.composables.ImageWithPlaceholder

@Composable
fun ProfileScreen(
    user: User,
    navController: NavHostController
) {

    Column (
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.size(10.dp))
        ImageWithPlaceholder(uri = user.profilePicture?.toUri(), size = com.example.sportfieldsearcher.ui.composables.Size.Large)
        Text(text = user.username)
        Spacer(Modifier.size(10.dp))
        HorizontalDivider()
    }
}