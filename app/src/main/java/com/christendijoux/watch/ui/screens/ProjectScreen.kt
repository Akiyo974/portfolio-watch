package com.christendijoux.watch.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Text
import com.christendijoux.watch.data.Portfolio
import com.christendijoux.watch.ui.components.Label
import com.christendijoux.watch.ui.components.PillButton
import com.christendijoux.watch.ui.components.PortfolioIcons
import com.christendijoux.watch.ui.components.ScreenScaffold
import com.christendijoux.watch.ui.components.Tag
import com.christendijoux.watch.ui.theme.Ink
import com.christendijoux.watch.ui.theme.Type
import com.christendijoux.watch.util.openOnPhone

/** Étude de cas résumée ; la version complète s'ouvre sur le téléphone. */
@Composable
fun ProjectScreen(projectId: String?) {
    val context = LocalContext.current
    val project = Portfolio.project(projectId)
    val index = Portfolio.projects.indexOf(project)
    val listState = rememberScalingLazyListState()

    ScreenScaffold(listState = listState) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().background(Ink),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (project == null) {
                item { Text(text = "Projet introuvable", style = Type.body, textAlign = TextAlign.Center) }
                return@ScalingLazyColumn
            }
            item {
                Label("%02d / %02d · %s".format(index + 1, Portfolio.projects.size, project.year))
            }
            item {
                Text(text = project.name.uppercase(), style = Type.display(36), textAlign = TextAlign.Center)
            }
            item {
                Label(project.status.label)
            }
            item {
                Text(
                    text = project.summary,
                    style = Type.body,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 18.dp),
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                    project.tags.take(3).forEach { Tag(it) }
                }
            }
            item {
                PillButton(
                    text = "Ouvrir sur le tél.",
                    icon = PortfolioIcons.ArrowUpRight,
                    onClick = { context.openOnPhone(project.url) },
                )
            }
        }
    }
}
