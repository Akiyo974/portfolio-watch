package com.christendijoux.watch.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Text
import com.christendijoux.watch.data.Portfolio
import com.christendijoux.watch.data.Project
import com.christendijoux.watch.ui.components.Label
import com.christendijoux.watch.ui.components.ScreenScaffold
import com.christendijoux.watch.ui.theme.Ink
import com.christendijoux.watch.ui.theme.Line
import com.christendijoux.watch.ui.theme.Stage
import com.christendijoux.watch.ui.theme.Type

/** Liste des projets : l'élément au centre est mis en avant par la ScalingLazyColumn. */
@Composable
fun ProjectsScreen(onOpenProject: (String) -> Unit) {
    val listState = rememberScalingLazyListState()
    ScreenScaffold(listState = listState) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().background(Ink),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            item { Label("Projets · %02d".format(Portfolio.projects.size)) }
            itemsIndexed(Portfolio.projects) { index, project ->
                ProjectCard(index = index, project = project, onClick = { onOpenProject(project.id) })
            }
        }
    }
}

@Composable
private fun ProjectCard(index: Int, project: Project, onClick: () -> Unit) {
    val shape = RoundedCornerShape(24.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Stage, shape)
            .border(1.dp, Line, shape)
            .clickable(role = Role.Button, onClickLabel = project.name, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        Label("%02d · %s · %s".format(index + 1, project.year, project.status.label))
        Text(
            text = project.name.uppercase(),
            style = Type.display(26),
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}
