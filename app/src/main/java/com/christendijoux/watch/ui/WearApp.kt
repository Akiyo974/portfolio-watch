package com.christendijoux.watch.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.christendijoux.watch.data.Portfolio
import com.christendijoux.watch.ui.screens.AmbientScreen
import com.christendijoux.watch.ui.screens.ContactScreen
import com.christendijoux.watch.ui.screens.HomeScreen
import com.christendijoux.watch.ui.screens.JourneyScreen
import com.christendijoux.watch.ui.screens.MenuScreen
import com.christendijoux.watch.ui.screens.ProjectScreen
import com.christendijoux.watch.ui.screens.ProjectsScreen
import com.christendijoux.watch.ui.screens.QrScreen
import com.christendijoux.watch.ui.screens.StarGameScreen
import com.christendijoux.watch.ui.theme.PortfolioTheme
import com.christendijoux.watch.util.call
import com.christendijoux.watch.util.openOnPhone

object Routes {
    const val HOME = "home"
    const val MENU = "menu"
    const val PROJECTS = "projects"
    const val PROJECT = "project/{id}"
    const val JOURNEY = "journey"
    const val CONTACT = "contact"
    const val GAME = "game"
    const val QR = "qr"

    fun project(id: String) = "project/$id"
}

/** Raccourcis reçus de la tuile, dans l'extra [EXTRA] de l'intent qui ouvre l'app. */
object Shortcuts {
    const val EXTRA = "shortcut"
    const val QR = "qr"
    const val CALL = "call"
    const val EMAIL = "email"
}

/**
 * Racine de l'app : chaque écran se ferme en glissant vers la droite ou avec le bouton retour.
 * [shortcut] ouvre directement un écran ou une action de contact ; [onShortcutHandled] le consomme.
 * En mode veille ([ambient]), le QR reste affiché tel quel pour pouvoir être scanné ;
 * les autres écrans laissent place à une heure sobre, presque entièrement noire.
 */
@Composable
fun WearApp(shortcut: String? = null, onShortcutHandled: () -> Unit = {}, ambient: Boolean = false) {
    PortfolioTheme {
        val nav = rememberSwipeDismissableNavController()
        val route = nav.currentBackStackEntryFlow.collectAsState(initial = null).value?.destination?.route
        val context = LocalContext.current
        val currentOnShortcutHandled by rememberUpdatedState(onShortcutHandled)
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
            composable(Routes.CONTACT) { ContactScreen(onShowQr = { nav.navigate(Routes.QR) }) }
            composable(Routes.QR) { QrScreen() }
            composable(Routes.GAME) { StarGameScreen(onRunningChange = { gameRunning = it }) }
        }

        if (ambient && route != Routes.QR) AmbientScreen()

        // Depuis la tuile : on repart de l'accueil puis Contact, pour que le retour reste naturel
        LaunchedEffect(shortcut) {
            if (shortcut == null) return@LaunchedEffect
            nav.navigate(Routes.CONTACT) { popUpTo(Routes.HOME) }
            when (shortcut) {
                Shortcuts.QR -> nav.navigate(Routes.QR)
                Shortcuts.CALL -> context.call(Portfolio.PHONE)
                Shortcuts.EMAIL -> context.openOnPhone("mailto:${Portfolio.EMAIL}")
            }
            currentOnShortcutHandled()
        }
    }
}
