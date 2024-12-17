package com.schibsted.nde

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.schibsted.nde.feature.details.DetailsScreen
import com.schibsted.nde.feature.meals.MealsScreen
import com.schibsted.nde.ui.AppTheme
import dagger.hilt.android.AndroidEntryPoint

private const val DETAILS = "details/{id}"
private const val MEALS = "meals"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            AppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    NavGraph(navController)
                }
            }
        }
    }
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = MEALS) {
        composable(MEALS) { MealsScreen(navigateToDetails = { id ->
            navController.navigate(route = "details/$id")
        }) }
        composable(
            route = DETAILS,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                }
            )
        ) { entry ->
            DetailsScreen(entry.arguments?.getString("id"))
        }
    }
}