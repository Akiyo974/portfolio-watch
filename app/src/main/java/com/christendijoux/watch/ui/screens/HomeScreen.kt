package com.christendijoux.watch.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.CurvedLayout
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.curvedText
import com.christendijoux.watch.data.Portfolio
import com.christendijoux.watch.ui.components.Label
import com.christendijoux.watch.ui.theme.Ink
import com.christendijoux.watch.ui.theme.Line
import com.christendijoux.watch.ui.theme.LineStrong
import com.christendijoux.watch.ui.theme.Mono
import com.christendijoux.watch.ui.theme.Paper
import com.christendijoux.watch.ui.theme.PaperMuted
import com.christendijoux.watch.ui.theme.Type
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
private val dateFormat = DateTimeFormatter.ofPattern("EEE d MMM", Locale.FRENCH)

/** Écran d'accueil façon cadran : un toucher ouvre le menu. */
@Composable
fun HomeScreen(onOpenMenu: () -> Unit) {
    val now by produceState(LocalDateTime.now()) {
        while (true) {
            value = LocalDateTime.now()
            delay(1_000)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Ink)
            .clickable(onClickLabel = "Ouvrir le menu", onClick = onOpenMenu),
        contentAlignment = Alignment.Center,
    ) {
        MinuteTicks()

        CurvedLayout(modifier = Modifier.padding(14.dp), anchor = 270f) {
            curvedText(
                text = "CHRISTEN DIJOUX · PORTFOLIO",
                color = PaperMuted,
                fontSize = 10.sp,
                fontFamily = Mono,
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Box(Modifier.size(5.dp).background(Paper, CircleShape))
                Label("Disponible")
            }
            Text(text = now.format(timeFormat), style = Type.display(60))
            Label("${now.format(dateFormat)} · ${Portfolio.CITY}")
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Complication(value = "%02d".format(Portfolio.projects.size), label = "Projets")
                Box(Modifier.width(1.dp).height(22.dp).background(Line))
                Complication(value = formatThousands(Portfolio.JOURNEY_KM), label = "Km")
            }
        }
    }
}

@Composable
private fun Complication(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = Type.display(20).copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Normal))
        Label(label)
    }
}

/** Graduation des 60 minutes le long du bord, plus marquée toutes les 5 minutes. */
@Composable
private fun MinuteTicks() {
    Canvas(Modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val outer = size.minDimension / 2 - 3.dp.toPx()
        for (i in 0 until 60) {
            val angle = i * 6.0 * PI / 180
            val inner = outer - if (i % 5 == 0) 7.dp.toPx() else 3.dp.toPx()
            val dir = Offset(cos(angle).toFloat(), sin(angle).toFloat())
            drawLine(
                color = LineStrong,
                start = center + dir * inner,
                end = center + dir * outer,
                strokeWidth = 1.dp.toPx(),
            )
        }
    }
}

/** 14420 → « 14 420 » (espace simple, présente dans toutes les polices). */
fun formatThousands(value: Int): String =
    value.toString().reversed().chunked(3).joinToString(" ").reversed()
