package com.schibsted.nde.data

import com.schibsted.nde.model.MealResponse
import kotlinx.coroutines.flow.Flow

interface MealsRepository {

    suspend fun getMeals(): List<MealResponse>

    val allMealsFlow: Flow<List<MealResponse>>

}