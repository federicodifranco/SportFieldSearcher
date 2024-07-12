package com.example.sportfieldsearcher.ui.composables

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

enum class FieldSize {
    VerySmall,
    Small,
    Large
}

@Composable
fun ImageForField(uri: Uri?, size: FieldSize) {
    if (uri != null && uri.path?.isNotEmpty() == true) {
        AsyncImage(
            model = uri,
            contentDescription = "Field Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(
                    when (size) {
                        FieldSize.VerySmall -> 36.dp
                        FieldSize.Small -> 72.dp
                        FieldSize.Large -> 200.dp
                    }
                )
                .clip(CircleShape)
        )
    } else {
        Icon(
            imageVector = Icons.Default.Image,
            contentDescription = "Default Field Icon",
            tint = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier
                .size(
                    when (size) {
                        FieldSize.VerySmall -> 36.dp
                        FieldSize.Small -> 72.dp
                        FieldSize.Large -> 200.dp
                    }
                )
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary)
        )
    }
}