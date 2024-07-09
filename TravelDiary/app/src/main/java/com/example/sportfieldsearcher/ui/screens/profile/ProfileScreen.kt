package com.example.sportfieldsearcher.ui.screens.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.example.sportfieldsearcher.data.database.entities.Field
import com.example.sportfieldsearcher.data.database.entities.PrivacyType
import com.example.sportfieldsearcher.data.database.entities.User
import com.example.sportfieldsearcher.ui.composables.ImageWithPlaceholder
import com.example.sportfieldsearcher.ui.utils.SportFieldSearcherRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: User,
    fields: List<Field>,
    navController: NavHostController
) {
    Scaffold {

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(it)
        ) {
            Spacer(Modifier.size(10.dp))
            ImageWithPlaceholder(uri = user.profilePicture?.toUri(), size = com.example.sportfieldsearcher.ui.composables.Size.Large)
            Text(text = user.username, style = MaterialTheme.typography.headlineMedium)
            HorizontalDivider(Modifier.padding(5.dp))
            Text(
                text = "All Fields",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.size(10.dp))
            AllFields(fields = fields.take(20).filter { it.privacyType == PrivacyType.PUBLIC }, user = user, navController = navController)
        }
    }
}

@Composable
fun AllFields(
    fields: List<Field>,
    user: User,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .padding(bottom = 10.dp)
    ) {
        if (fields.isNotEmpty()) {
            Column(
                modifier = Modifier.border(width = 2.dp, color = Color.Gray)
            ) {
                fields.forEach { field ->
                    FieldItem(
                        field = field,
                        onClick = { navController.navigate(SportFieldSearcherRoute.FieldDetails.buildRoute(field.fieldId)) }
                    )
                }
            }
        } else {
            Text(
                text = "No fields posted",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}

@Composable
fun FieldItem(field: Field, onClick: () -> Unit) {
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        headlineContent = { Text(text = field.name) },
        supportingContent = {
            Text(text = field.date)
        },
    )
    HorizontalDivider()
}
