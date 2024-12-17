package com.schibsted.nde.feature.details

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DetailsScreen(id: String?) {
    Surface(Modifier.fillMaxSize()) {
        Text(
            text = "id: $id",
            style = MaterialTheme.typography.h5
        )
    }
}