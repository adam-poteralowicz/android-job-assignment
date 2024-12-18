package com.schibsted.nde.model

data class MealResponse(
    val idMeal: String,
    val strMeal: String,
    val strCategory: String,
    val strMealThumb: String,
    val strInstructions: String
)

fun List<MealResponse>.filterByName(query: String) = filter {
    it.strMeal.lowercase().contains(query.lowercase())
}