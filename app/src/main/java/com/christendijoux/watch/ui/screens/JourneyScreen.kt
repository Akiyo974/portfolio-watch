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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Text
import com.christendijoux.watch.data.Experience
import com.christendijoux.watch.data.Portfolio
import com.christendijoux.watch.ui.components.Label
import com.christendijoux.watch.ui.components.ScreenScaffold
import com.christendijoux.watch.ui.theme.Ink
import com.christendijoux.watch.ui.theme.Line
import com.christendijoux.watch.ui.theme.Paper
import com.christendijoux.watch.ui.theme.Type
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// Arc sur la moitié basse (l'heure occupe le haut) : départ à gauche, arrivée à droite
private const val ARC_START = 160f
private const val ARC_SWEEP = -140f

/** Parcours : La Réunion → Saguenay, tracé le long de la lunette. */
@Composable
fun JourneyScreen() {
    val listState = rememberScalingLazyListState()
    ScreenScaffold(listState = listState) {
        Box(Modifier.fillMaxSize().background(Ink)) {
            ScalingLazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                item { Label("Parcours") }
                item {
                    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = formatThousands(Portfolio.JOURNEY_KM), style = Type.display(52))
                        Label("Km", modifier = Modifier.padding(bottom = 8.dp))
                    }
                }
                item { Label("La Réunion → Saguenay") }
                items(Portfolio.experiences) { ExperienceRow(it) }
            }
            JourneyArc()
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
        Label(experience.role, modifier = Modifier.fillMaxWidth())
    }
}

/** Arc du trajet, du point de départ (cercle vide) à l'arrivée (point plein). */
@Composable
private fun JourneyArc() {
    Canvas(Modifier.fillMaxSize()) {
        val inset = 5.dp.toPx()
        val radius = size.minDimension / 2 - inset
        val center = Offset(size.width / 2, size.height / 2)
        val stroke = 1.5.dp.toPx()

        drawArc(
            color = Paper,
            startAngle = ARC_START,
            sweepAngle = ARC_SWEEP,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = stroke),
        )

        fun pointAt(degrees: Float): Offset {
            val rad = degrees * PI / 180
            return Offset(center.x + radius * cos(rad).toFloat(), center.y + radius * sin(rad).toFloat())
        }
        val dot = 3.5.dp.toPx()
        drawCircle(color = Ink, radius = dot, center = pointAt(ARC_START))
        drawCircle(color = Paper, radius = dot, center = pointAt(ARC_START), style = Stroke(width = stroke))
        drawCircle(color = Paper, radius = dot, center = pointAt(ARC_START + ARC_SWEEP))
    }
}
