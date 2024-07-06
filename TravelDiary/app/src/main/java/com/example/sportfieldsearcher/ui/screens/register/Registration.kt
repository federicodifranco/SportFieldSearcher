package com.example.sportfieldsearcher.ui.screens.register

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sportfieldsearcher.ui.controllers.UsersState
import com.example.sportfieldsearcher.ui.composables.ImageWithPlaceholder
import com.example.sportfieldsearcher.ui.composables.Size

val PADDING = 10.dp

@Composable
fun RegistrationScreen(
    usersState: UsersState,
    state: RegistrationState,
    actions: RegistrationActions,
    onSubmit: () -> Unit,
    navController: NavHostController
) {
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    // Camera
    /*val cameraLauncher = rememberCameraLauncher { imageUri -> actions.setProfilePicture(imageUri) }
    val cameraPermission = rememberPermission(Manifest.permission.CAMERA) { status ->
        if (status.isGranted)
            cameraLauncher.captureImage()
        else
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
    }

    fun takePicture() {
        if (cameraPermission.status.isGranted)
            cameraLauncher.captureImage()
        else
            cameraPermission.launchPermissionRequest()
    }

    val galleryLauncher =
        rememberGalleryLauncher { imageUri -> actions.setProfilePicture(imageUri) }
    */
    // UI
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(NavigationBarDefaults.windowInsets)
    ) { contentPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(
                    top = 0.dp,
                    bottom = 0.dp,
                    start = contentPadding.calculateStartPadding(LayoutDirection.Ltr),
                    end = contentPadding.calculateEndPadding(LayoutDirection.Rtl)
                )
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Registration",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
            OutlinedTextField(
                value = state.username,
                onValueChange = actions::setUsername,
                label = { Text("Username") },
                modifier = Modifier,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            OutlinedTextField(
                value = state.email,
                onValueChange = actions::setEmail,
                label = { Text("Email") },
                modifier = Modifier,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            OutlinedTextField(
                value = state.password,
                onValueChange = actions::setPassword,
                label = { Text("Password") },
                modifier = Modifier,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = actions::setConfirmPassword,
                label = { Text("Confirm Password") },
                modifier = Modifier,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
            Row {
                Button(
                    onClick = { },
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                ) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = "Camera", modifier = Modifier.size(24.dp))
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Take a picture")
                }
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Button(
                    onClick = {},
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                ) {
                    Icon(Icons.Filled.Image, contentDescription = "Gallery Icon", modifier = Modifier.size(24.dp))
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Select Image")
                }
            }
            if (state.profilePicture != Uri.EMPTY) {
                Spacer(Modifier.size(PADDING))
                ImageWithPlaceholder(uri = state.profilePicture, size = Size.Large)
            }
            TextButton(onClick = {
                navController.popBackStack()
                //navController.navigate(SportFieldSearcherRoute.Login.route)
            }) {
                Text("Already have an account? Log in")
            }
            HorizontalDivider()
            Button(
                modifier = Modifier.padding(bottom = PADDING),
                onClick = {
                    if (!state.canSubmit || usersState.users.any { it.email == state.email }) {
                        when {
                            state.username.isBlank() -> {
                                snackbarMessage = "Username is required"
                                showSnackbar = true
                            }

                            state.email.isBlank() -> {
                                snackbarMessage = "Email is required"
                                showSnackbar = true
                            }

                            state.email.contains("@").not() -> {
                                snackbarMessage = "Invalid email"
                                showSnackbar = true
                            }

                            usersState.users.any { it.email == state.email } -> {
                                snackbarMessage = "Email already in use"
                                showSnackbar = true
                            }

                            state.password.isBlank() -> {
                                snackbarMessage = "Password is required"
                                showSnackbar = true
                            }

                            state.confirmPassword.isBlank() -> {
                                snackbarMessage = "Confirm your password"
                                showSnackbar = true
                            }

                            state.confirmPassword != state.password -> {
                                snackbarMessage = "Passwords do not match"
                                showSnackbar = true
                            }
                        }
                        return@Button
                    }
                    onSubmit()
                }
            ) {
                Text(text = "Submit")
            }
        }
    }
    if (showSnackbar) {
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(
                message = snackbarMessage,
                actionLabel = "Dismiss",
                duration = SnackbarDuration.Short
            )
            showSnackbar = false
        }
    }
}