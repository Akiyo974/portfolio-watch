package com.christendijoux.watch.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Text
import com.christendijoux.watch.R
import com.christendijoux.watch.data.Experience
import com.christendijoux.watch.data.Portfolio
import com.christendijoux.watch.ui.components.Label
import com.christendijoux.watch.ui.components.ScreenScaffold
import com.christendijoux.watch.ui.theme.Ink
import com.christendijoux.watch.ui.theme.Line
import com.christendijoux.watch.ui.theme.Paper
import com.christendijoux.watch.ui.theme.Type

/** Parcours : La Réunion → Saguenay, tracé comme un vol au-dessus de la liste des expériences. */
@Composable
fun JourneyScreen() {
    val listState = rememberScalingLazyListState()
    ScreenScaffold(listState = listState) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().background(Ink),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            item { Label(stringResource(R.string.menu_journey)) }
            item {
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = formatThousands(Portfolio.JOURNEY_KM), style = Type.display(52))
                    Label(stringResource(R.string.unit_km), modifier = Modifier.padding(bottom = 8.dp))
                }
            }
            // Le trajet défile avec la liste : il ne passe plus jamais sur le texte
            item { FlightPath() }
            item { Label(stringResource(R.string.journey_route)) }
            items(Portfolio.experiences) { ExperienceRow(it) }
        }
    }
}

@Composable
private fun ExperienceRow(experience: Experience) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(Modifier.fillMaxWidth().height(1.dp).background(Line))
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = experience.company.uppercase(), style = Type.display(20))
            Label(experience.year)
        }
        Label(stringResource(experience.role), modifier = Modifier.fillMaxWidth())
    }
}

/** Arc pointillé du départ (cercle vide) à l'arrivée (point plein), bombé comme une trajectoire de vol. */
@Composable
private fun FlightPath() {
    Canvas(Modifier.fillMaxWidth(0.6f).height(28.dp)) {
        val stroke = 1.5.dp.toPx()
        val dot = 3.5.dp.toPx()
        val start = Offset(dot + stroke, size.height - dot - stroke)
        val end = Offset(size.width - dot - stroke, start.y)

        val path = Path().apply {
            moveTo(start.x, start.y)
            quadraticTo(size.width / 2, -size.height * 0.55f, end.x, end.y)
        }
        drawPath(
            path = path,
            color = Paper,
            style = Stroke(width = stroke, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(), 3.dp.toPx()))),
        )

        drawCircle(color = Ink, radius = dot, center = start)
        drawCircle(color = Paper, radius = dot, center = start, style = Stroke(width = stroke))
        drawCircle(color = Paper, radius = dot, center = end)
    }
}
