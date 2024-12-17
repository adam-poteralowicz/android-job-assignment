package com.schibsted.nde.feature.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.schibsted.nde.model.MealDetails

@Composable
fun DetailsScreen(
    details: MealDetails
) {
    Column(Modifier.padding(8.dp)) {
        Row(Modifier.align(Alignment.CenterHorizontally)) {
            Image(
                modifier = Modifier.padding(bottom = 16.dp),
                painter = rememberAsyncImagePainter(model = details.image),
                contentDescription = "mealImage"
            )
        }
        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = details.name,
            style = MaterialTheme.typography.h5
        )
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())) {
            Text(
                text = details.instructions,
                style = MaterialTheme.typography.subtitle1,
            )
        }
    }
}