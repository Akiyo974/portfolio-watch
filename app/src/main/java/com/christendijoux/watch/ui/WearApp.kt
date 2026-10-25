package com.christendijoux.watch.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.christendijoux.watch.ui.screens.ContactScreen
import com.christendijoux.watch.ui.screens.HomeScreen
import com.christendijoux.watch.ui.screens.JourneyScreen
import com.christendijoux.watch.ui.screens.MenuScreen
import com.christendijoux.watch.ui.screens.ProjectScreen
import com.christendijoux.watch.ui.screens.ProjectsScreen
import com.christendijoux.watch.ui.screens.StarGameScreen
import com.christendijoux.watch.ui.theme.PortfolioTheme

object Routes {
    const val HOME = "home"
    const val MENU = "menu"
    const val PROJECTS = "projects"
    const val PROJECT = "project/{id}"
    const val JOURNEY = "journey"
    const val CONTACT = "contact"
    const val GAME = "game"

    fun project(id: String) = "project/$id"
}

/** Racine de l'app : chaque écran se ferme en glissant vers la droite ou avec le bouton retour. */
@Composable
fun WearApp() {
    PortfolioTheme {
        val nav = rememberSwipeDismissableNavController()
        // Pendant une partie, un glissement vers la droite ne doit pas fermer le jeu
        var gameRunning by remember { mutableStateOf(false) }
        SwipeDismissableNavHost(
            navController = nav,
            startDestination = Routes.HOME,
            userSwipeEnabled = !gameRunning,
        ) {
            composable(Routes.HOME) {
                HomeScreen(onOpenMenu = { nav.navigate(Routes.MENU) })
            }
            composable(Routes.MENU) {
                MenuScreen(onNavigate = { route -> nav.navigate(route) })
            }
            composable(Routes.PROJECTS) {
                ProjectsScreen(onOpenProject = { id -> nav.navigate(Routes.project(id)) })
            }
            composable(Routes.PROJECT) { entry ->
                ProjectScreen(projectId = entry.arguments?.getString("id"))
            }
            composable(Routes.JOURNEY) { JourneyScreen() }
            composable(Routes.CONTACT) { ContactScreen() }
            composable(Routes.GAME) { StarGameScreen(onRunningChange = { gameRunning = it }) }
        }
    }
}
