package com.schibsted.nde.feature.meals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schibsted.nde.data.MealsRepository
import com.schibsted.nde.database.AppDatabase
import com.schibsted.nde.database.MealEntity
import com.schibsted.nde.model.MealResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealsViewModel @Inject constructor(
    private val mealsRepository: MealsRepository,
    private val appDatabase: AppDatabase
) : ViewModel() {
    private val _state = MutableStateFlow(MealsViewState(isLoading = true))

    val state: StateFlow<MealsViewState>
        get() = _state

    val allMeals: Flow<List<MealResponse>> = mealsRepository.allMealsFlow

    init {
        loadMeals()
    }

    fun loadMeals() {
        viewModelScope.launch {
            _state.emit(_state.value.copy(isLoading = true))
            val meals = mealsRepository.getMeals()
            _state.emit(_state.value.copy(meals = meals, filteredMeals = meals))
            _state.emit(_state.value.copy(isLoading = false))
        }
    }

    fun submitQuery(query: String?) {
        viewModelScope.launch {
            val filteredMeals = if (query?.isNotBlank() == true) {
                _state.value.meals
            } else {
                _state.value.meals.filter {
                    it.strMeal.lowercase().contains(query?.lowercase() ?: "")
                }
            }
            _state.emit(_state.value.copy(query = query, filteredMeals = filteredMeals))
        }
    }

    fun onMealChosen(meal: MealResponse) = viewModelScope.launch {
        _state.emit(_state.value.copy(selectedMeal = meal))
    }

    fun addMealsToDatabase(meals: List<MealResponse>) = viewModelScope.launch {
        val entities = meals.map { MealEntity(it.idMeal, it.strCategory, it.strMeal, it.strMealThumb, it.strInstructions) }
        appDatabase.mealDao().insertAll(entities)
    }
}