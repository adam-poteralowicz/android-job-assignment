package com.schibsted.nde.feature.details

import android.content.res.Configuration
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.schibsted.nde.model.MealDetails

@Composable
fun DetailsScreen(
    details: MealDetails
) {
    val orientation = LocalConfiguration.current.orientation
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
        PortraitDetailsContent(details)
    } else {
        LandscapeDetailsContent(details)
    }
}

@Composable
fun PortraitDetailsContent(details: MealDetails) {
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
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h5
        )
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = details.instructions,
                style = MaterialTheme.typography.subtitle1,
            )
        }
    }
}

@Composable
fun LandscapeDetailsContent(details: MealDetails) {
    Row(Modifier.padding(8.dp)) {
        Column(Modifier.padding(end = 16.dp)) {
            Image(
                modifier = Modifier.padding(bottom = 8.dp),
                painter = rememberAsyncImagePainter(model = details.image),
                contentDescription = "mealImage"
            )
            Text(
                text = details.name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h5
            )
        }
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = details.instructions,
                style = MaterialTheme.typography.subtitle1,
            )
        }
    }
}