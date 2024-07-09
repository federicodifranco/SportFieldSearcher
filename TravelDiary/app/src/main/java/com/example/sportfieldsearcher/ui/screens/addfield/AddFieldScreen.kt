package com.example.sportfieldsearcher.ui.screens.addfield

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sportfieldsearcher.data.database.entities.CategoryType
import com.example.sportfieldsearcher.data.database.entities.PrivacyType
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFieldScreen(
    state: AddFieldState,
    actions: AddFieldActions,
    onSubmit: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    val context = LocalContext.current
    val dateState = remember { mutableStateOf(state.date) }
    val calendar = Calendar.getInstance()
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                dateState.value = "$dayOfMonth/${month + 1}/$year"
                actions.setDate(dateState.value)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    val expandedCategory = remember { mutableStateOf(false) }
    val expandedPrivacy = remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    if (!state.canSubmit) {
                        when {
                            state.name.isBlank() -> {
                                snackbarMessage = "Name cannot be empty"
                                showSnackbar = true
                            }
                            state.date.isBlank() -> {
                                snackbarMessage = "Please select a date"
                                showSnackbar = true
                            }
                            LocalDate.parse(
                                state.date,
                                DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            ) < LocalDate.now() -> {
                                snackbarMessage = "Date cannot be in the past"
                                showSnackbar = true
                            }
                            state.privacyType == PrivacyType.NONE -> {
                                snackbarMessage = "Privacy type cannot be NONE"
                                showSnackbar = true
                            }
                            state.category == CategoryType.NONE -> {
                                snackbarMessage = "Category type cannot be NONE"
                                showSnackbar = true
                            }
                        }
                    } else {
                        onSubmit()
                    }
                }
            ) {
                Icon(Icons.Outlined.Check, contentDescription = "Add Field")
            }
        }
    ) { contentPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(contentPadding)
                .padding(12.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = actions::setName,
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = dateState.value,
                onValueChange = { dateState.value = it },
                label = { Text("Date") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(Icons.Outlined.Event, contentDescription = "Pick date")
                    }
                }
            )
            OutlinedTextField(
                value = state.description,
                onValueChange = actions::setDescription,
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.city,
                onValueChange = actions::setCity,
                label = { Text("City") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Outlined.MyLocation, contentDescription = "Current location")
                    }
                }
            )

            ExposedDropdownMenuBox(
                expanded = expandedCategory.value,
                onExpandedChange = { expandedCategory.value = !expandedCategory.value }
            ) {
                OutlinedTextField(
                    value = state.category.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory.value)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                DropdownMenu(
                    expanded = expandedCategory.value,
                    onDismissRequest = { expandedCategory.value = false }
                ) {
                    CategoryType.entries.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                actions.setCategory(category)
                                expandedCategory.value = false
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.size(4.dp))
            ExposedDropdownMenuBox(
                expanded = expandedPrivacy.value,
                onExpandedChange = { expandedPrivacy.value = !expandedPrivacy.value }
            ) {
                OutlinedTextField(
                    value = state.privacyType.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Privacy") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPrivacy.value)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                DropdownMenu(
                    expanded = expandedPrivacy.value,
                    onDismissRequest = { expandedPrivacy.value = false }
                ) {
                    PrivacyType.entries.forEach { privacy ->
                        DropdownMenuItem(
                            text = { Text(privacy.name) },
                            onClick = {
                                actions.setPrivacyType(privacy)
                                expandedPrivacy.value = false
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.size(4.dp))
            Button(
                onClick = { /*TODO*/ },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            ) {
                Icon(
                    Icons.Outlined.PhotoCamera,
                    contentDescription = "Camera icon",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Take a picture")
            }

            Spacer(Modifier.size(8.dp))

            Image(
                Icons.Outlined.Image,
                contentDescription = "Field picture",
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(18.dp)
            )
        }
    }
}
