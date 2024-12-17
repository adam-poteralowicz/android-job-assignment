package com.schibsted.nde.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal")
data class MealEntity(
    @PrimaryKey val id: String,
    val category: String,
    val name: String,
    val image: String,
    val instructions: String,
)
