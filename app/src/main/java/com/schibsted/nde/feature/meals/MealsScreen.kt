//noinspection UsingMaterialAndMaterial3Libraries
package com.schibsted.nde.feature.meals

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.schibsted.nde.R
import com.schibsted.nde.feature.common.MealImage
import com.schibsted.nde.model.MealDetails
import com.schibsted.nde.model.MealResponse
import com.schibsted.nde.model.filterByName
import com.schibsted.nde.ui.typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MealsScreen(
    viewModel: MealsViewModel = hiltViewModel(),
    navigateToDetails: (MealDetails) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    ModalBottomSheetLayout(
        sheetContent = { ModalBottomSheetContent(
            state = modalBottomSheetState,
            onSearch = { query -> viewModel.submitQuery(query) },
            coroutineScope = coroutineScope
        ) },
        sheetState = modalBottomSheetState
    ) {
        Scaffold(topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = {
                        coroutineScope.launch { modalBottomSheetState.show() }
                    }) {
                        Icon(Icons.Filled.Search, "search")
                    }
                }
            )
        }
        ) { MealsScreenContent(navigateToDetails) }
    }
}

@Composable
fun ModalBottomSheetContent(
    state: ModalBottomSheetState,
    onSearch: (String?) -> Unit,
    coroutineScope: CoroutineScope
) {
    val focusManager = LocalFocusManager.current
    if (state.currentValue != ModalBottomSheetValue.Hidden) {
        DisposableEffect(Unit) {
            onDispose {
                focusManager.clearFocus()
            }
        }
    }
    Column(Modifier.padding(16.dp)) {
        var query by rememberSaveable { mutableStateOf("") }
        TextField(
            value = query,
            onValueChange = { query = it },
            label = { Text(stringResource(R.string.query)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                onSearch(query)
                coroutineScope.launch { state.hide() }
            }),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Row(Modifier.align(Alignment.End)) {
            OutlinedButton(onClick = {
                onSearch(null)
                query = ""
                coroutineScope.launch { state.hide() }
            }) {
                Text(stringResource(R.string.clear))
            }
            Spacer(Modifier.width(16.dp))
            Button(onClick = {
                onSearch(query)
                coroutineScope.launch { state.hide() }
            }) {
                Text(stringResource(R.string.search))
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MealsScreenContent(
    navigateToDetails: (MealDetails) -> Unit,
    viewModel: MealsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val allMeals by viewModel.allMeals.collectAsState(initial = state.filteredMeals)
    val coroutineScope = rememberCoroutineScope()

    Box(Modifier
        .fillMaxSize()
        .pullRefresh(
            state = rememberPullRefreshState(
                refreshing = state.isLoading,
                onRefresh = { viewModel.loadMeals() }
            )
        )
    ) {
        if (state.isLoading) return
        val selected = state.selectedMeal
        when {
            state.filteredMeals.isEmpty() -> {
                Box(Modifier.align(Alignment.Center)) {
                    Text(
                        modifier = Modifier.testTag("empty_state_text"),
                        text = stringResource(R.string.no_meals_found),
                        style = MaterialTheme.typography.h6,
                        fontSize = 28.sp
                    )
                }
            }
            selected != null -> {
                LaunchedEffect(Unit) {
                    val details = MealDetails(
                        image = selected.strMealThumb,
                        name = selected.strMeal,
                        instructions = selected.strInstructions
                    )
                    navigateToDetails(details)
                }
            }
            else -> Column {
                if (allMeals.isEmpty()) {
                    MealGridContent(state.filteredMeals)
                    LaunchedEffect(Unit) {
                        coroutineScope.launch {
                            viewModel.addMealsToDatabase(state.meals)
                        }
                    }
                } else {
                    MealGridContent(
                        viewModel.state.value.query?.let { query ->
                            allMeals.filterByName(query)
                        } ?: allMeals
                    )
                }
            }
        }
    }
}

@Composable
fun MealGridContent(meals: List<MealResponse>) {
    val orientation = LocalConfiguration.current.orientation
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(meals) { meal ->
                Divider(thickness = 8.dp)
                MealRowComposable(meal)
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.background(
                colors.onSurface.copy(
                    alpha = 0.12f
                )
            )
        ) {
            itemsIndexed(meals) { index, meal ->
                val isFirstColumn = index % 2 == 0
                Column(
                    Modifier
                        .padding(horizontal = if (isFirstColumn) 8.dp else 4.dp)
                        .padding(top = 8.dp, bottom = 0.dp)
                ) {
                    MealRowComposable(meal)
                }
            }
        }
    }
}

@Composable
fun MealRowComposable(meal: MealResponse, viewModel: MealsViewModel = hiltViewModel()) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(colors.surface)
            .clip(RoundedCornerShape(4.dp))
            .padding(16.dp)
            .clickable { viewModel.onMealChosen(meal) }
            .testTag("meal_row")
    ) {
        MealImage(meal.strMealThumb, Modifier.size(64.dp))
        Column(
            Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    meal.strCategory,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.weight(1f)
                )
            }
            Text(text = meal.strMeal,
                fontWeight = FontWeight.Bold,
                style = typography.h6
            )
        }
    }
}