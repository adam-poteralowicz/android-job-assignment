package com.schibsted.nde

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasImeAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.schibsted.nde.feature.meals.MealsViewModel
import com.schibsted.nde.testUtils.FakeAppDatabase
import com.schibsted.nde.testUtils.FakeMealsRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
@HiltAndroidTest
class MainActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: TestNavHostController
    private lateinit var viewModel: MealsViewModel
    private lateinit var mealsRepository: FakeMealsRepository
    private lateinit var appDatabase: FakeAppDatabase

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            NavGraph(navController)
        }
        mealsRepository = FakeMealsRepository()
        appDatabase = FakeAppDatabase()
        initViewModel()
    }

    @Test
    fun verify_nav_host_start_destination() = with(composeTestRule) {
        waitUntil {
            onAllNodesWithText("Food App").fetchSemanticsNodes().size == 1
        }
    }

    @Test
    fun verify_meal_items_visible() = with(composeTestRule) {
        waitUntilAtLeastOneExists(hasText("Chicken"))
    }

    @Test
    fun verify_search_bottom_sheet_visible() = with(composeTestRule) {
        verify_meal_items_visible()
        onNodeWithContentDescription("search").performClick()
        waitUntil {
            onAllNodesWithText("Query").fetchSemanticsNodes().size == 1
                    && onAllNodes(hasImeAction(ImeAction.Search)).fetchSemanticsNodes().size == 1
        }
    }

    @Test
    fun verify_search_results_visible() {
        verify_search_bottom_sheet_visible()
        composeTestRule.onNodeWithText("Query").performTextInput("Casserole")
        composeTestRule.onNodeWithText("Search").performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasText("Casserole"))
    }

    @Test
    fun verify_empty_state_visible() {
        verify_search_results_visible()
        composeTestRule.onNodeWithText("Query").performTextInput("unavailable")
        composeTestRule.onNodeWithText("Search").performClick()
        composeTestRule.onNodeWithText("No meals found").assertExists()
    }

    @Test
    fun verify_details_visible() {
        verify_meal_items_visible()
        composeTestRule.onNodeWithText("Brown Stew Chicken").performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasContentDescription("back"))
        composeTestRule.waitUntilAtLeastOneExists(hasText("Brown Stew Chicken"))
    }

    @Test
    fun verify_meals_on_back_from_details() {
        verify_details_visible()
        composeTestRule.onNodeWithContentDescription("back").performClick()
        verify_nav_host_start_destination()
        verify_meal_items_visible()
    }

    private fun initViewModel() {
        viewModel = MealsViewModel(mealsRepository, appDatabase)
    }
}