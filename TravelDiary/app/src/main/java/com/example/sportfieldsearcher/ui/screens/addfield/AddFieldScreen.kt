package com.example.sportfieldsearcher.ui.screens.addfield

import android.app.DatePickerDialog
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.MyLocation
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
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults.contentWindowInsets
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.sportfieldsearcher.data.database.entities.CategoryType
import com.example.sportfieldsearcher.data.database.entities.PrivacyType
import com.example.sportfieldsearcher.ui.composables.FieldSize
import com.example.sportfieldsearcher.ui.composables.ImageForField
import com.example.sportfieldsearcher.ui.utils.rememberGalleryLauncher
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

    val galleryLauncher =
        rememberGalleryLauncher { imageUri -> actions.setFieldPicture(imageUri) }

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
        },
        contentWindowInsets = contentWindowInsets.exclude(NavigationBarDefaults.windowInsets)
    ) { contentPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(contentPadding)
                .padding(5.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = actions::setName,
                label = { Text("Name", color = MaterialTheme.colorScheme.onPrimaryContainer) },
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onPrimaryContainer),
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = state.description,
                onValueChange = actions::setDescription,
                label = { Text("Description", color = MaterialTheme.colorScheme.onPrimaryContainer) },
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onPrimaryContainer),
                modifier = Modifier.fillMaxWidth(),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = dateState.value,
                    onValueChange = { dateState.value = it },
                    label = { Text("Date", color = MaterialTheme.colorScheme.onPrimaryContainer) },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onPrimaryContainer),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    trailingIcon = {
                        IconButton(onClick = { datePickerDialog.show() }) {
                            Icon(Icons.Outlined.Event, contentDescription = "Pick date", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }
                )

                OutlinedTextField(
                    value = state.city,
                    onValueChange = actions::setCity,
                    label = { Text("City", color = MaterialTheme.colorScheme.onPrimaryContainer) },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onPrimaryContainer),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    trailingIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(Icons.Outlined.MyLocation, contentDescription = "Current location", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }
                )
            }
            ExposedDropdownMenuBox(
                expanded = expandedCategory.value,
                onExpandedChange = { expandedCategory.value = !expandedCategory.value }
            ) {
                OutlinedTextField(
                    value = state.category.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category", color = MaterialTheme.colorScheme.onPrimaryContainer) },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onPrimaryContainer),
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
                            text = { Text(category.name, color = MaterialTheme.colorScheme.onPrimaryContainer) },
                            onClick = {
                                actions.setCategory(category)
                                expandedCategory.value = false
                            }
                        )
                    }
                }
            }
            ExposedDropdownMenuBox(
                expanded = expandedPrivacy.value,
                onExpandedChange = { expandedPrivacy.value = !expandedPrivacy.value }
            ) {
                OutlinedTextField(
                    value = state.privacyType.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Privacy", color = MaterialTheme.colorScheme.onPrimaryContainer) },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onPrimaryContainer),
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
                            text = { Text(privacy.name, color = MaterialTheme.colorScheme.onPrimaryContainer) },
                            onClick = {
                                actions.setPrivacyType(privacy)
                                expandedPrivacy.value = false
                            }
                        )
                    }
                }
            }
            Button(
                onClick = { galleryLauncher.selectImage() },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            ) {
                Icon(Icons.Filled.Image, contentDescription = "Gallery Icon", modifier = Modifier.size(24.dp))
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Select Image")
            }

            Spacer(Modifier.size(5.dp))

            if (state.fieldPicture == Uri.EMPTY) {
                Image(
                    Icons.Outlined.Image,
                    contentDescription = "Field picture",
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary),
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(5.dp)
                )
            } else {
                ImageForField(
                    uri = state.fieldPicture,
                    size = FieldSize.Large
                )
            }
        }
    }
}
