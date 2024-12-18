package com.schibsted.nde.testUtils

import com.schibsted.nde.data.MealsRepository
import com.schibsted.nde.model.MealResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeMealsRepository : MealsRepository {

    private var fakeMeals: List<MealResponse> = listOf()

    override suspend fun getMeals(): List<MealResponse> {
        return fakeMeals
    }

    override val allMealsFlow: Flow<List<MealResponse>>
        get() = flowOf(fakeMeals)

    fun setMeals(list: List<MealResponse>) {
        fakeMeals = list
    }
}
