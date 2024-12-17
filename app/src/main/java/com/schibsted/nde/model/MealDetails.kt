package com.schibsted.nde.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MealDetails(
    val image: String,
    val name: String,
    val instructions: String
) : Parcelable