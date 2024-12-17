package com.schibsted.nde.data

import com.schibsted.nde.api.BackendApi
import com.schibsted.nde.database.MealEntity
import com.schibsted.nde.database.MealEntityDao
import com.schibsted.nde.model.MealResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealsRepository @Inject constructor(
    private val backendApi: BackendApi,
    mealEntityDao: MealEntityDao
) {
    val allMealsFlow: Flow<List<MealResponse>> = mealEntityDao.getAll().map { it.map { entity -> entity.toMealResponse() } }

    suspend fun getMeals(): List<MealResponse> = backendApi.getMeals().meals

    private fun MealEntity.toMealResponse(): MealResponse = MealResponse(
        idMeal = id,
        strCategory = category,
        strMeal = name,
        strMealThumb = image,
        strInstructions = instructions
    )
}