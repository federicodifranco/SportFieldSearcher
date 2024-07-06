package com.example.sportfieldsearcher.ui.composables

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

enum class Size {
    VerySmall,
    Small,
    Large
}

@Composable
fun ImageWithPlaceholder(uri: Uri?, size: Size) {
    if (uri != null && uri.path?.isNotEmpty() == true) {
        AsyncImage(
            model = uri,
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(
                    when (size) {
                        Size.VerySmall -> 36.dp
                        Size.Small -> 72.dp
                        Size.Large -> 128.dp
                    }
                )
                .clip(CircleShape)
        )
    } else {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Default Profile Icon",
            tint = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier
                .size(
                    when (size) {
                        Size.VerySmall -> 36.dp
                        Size.Small -> 72.dp
                        Size.Large -> 128.dp
                    }
                )
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary)
        )
    }
}