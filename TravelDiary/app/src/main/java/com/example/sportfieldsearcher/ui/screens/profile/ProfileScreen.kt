package com.example.sportfieldsearcher.ui.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.example.sportfieldsearcher.data.database.entities.Field
import com.example.sportfieldsearcher.data.database.entities.User
import com.example.sportfieldsearcher.ui.composables.FieldSize
import com.example.sportfieldsearcher.ui.composables.ImageForField
import com.example.sportfieldsearcher.ui.composables.ImageWithPlaceholder
import com.example.sportfieldsearcher.ui.utils.SportFieldSearcherRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: User,
    fields: List<Field>,
    navController: NavHostController
) {
    Scaffold { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Spacer(Modifier.size(10.dp))
                ImageWithPlaceholder(uri = user.profilePicture?.toUri(), size = com.example.sportfieldsearcher.ui.composables.Size.Large)
                Text(text = user.username, color = MaterialTheme.colorScheme.onPrimaryContainer, style = MaterialTheme.typography.headlineMedium)
                HorizontalDivider(Modifier.padding(5.dp))
                Spacer(Modifier.size(5.dp))
            }
            item {
                AllFields(fields = fields, user = user, navController = navController)
            }
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
        .padding(5.dp)
    ) {
        Text(
            text = "Your Fields",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(Modifier.size(10.dp))
        if (fields.isNotEmpty()) {
            Column (
                modifier = Modifier
                .padding(5.dp)
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
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.headlineSmall,
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
        leadingContent = {
            ImageForField(
                uri = field.fieldPicture?.toUri(),
                size = FieldSize.Small
            )
        },
        supportingContent = {
            Column {
                Text(text = "City: " + field.city)
                Text(text = "Category: " + field.category)
            }
        },
    )
}
