package com.christendijoux.watch.ui.screens

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.christendijoux.watch.ui.components.Label
import com.christendijoux.watch.ui.components.PillButton
import com.christendijoux.watch.ui.components.PortfolioIcons
import com.christendijoux.watch.ui.components.ScreenScaffold
import com.christendijoux.watch.ui.theme.Ink
import com.christendijoux.watch.ui.theme.Line
import com.christendijoux.watch.ui.theme.LineStrong
import com.christendijoux.watch.ui.theme.Paper
import com.christendijoux.watch.ui.theme.Type
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

private const val GAME_SECONDS = 20
private const val PREFS = "star_game"
private const val KEY_BEST = "best"

/** Position aléatoire de l'étoile, en fraction du rayon de l'écran (reste dans la zone sûre). */
private fun randomSpot(): Offset {
    val angle = Random.nextDouble(0.0, 2 * PI)
    val r = sqrt(Random.nextDouble()) * 0.55
    return Offset((r * cos(angle)).toFloat(), (r * sin(angle)).toFloat())
}

/** Le jeu caché du site : toucher un maximum d'étoiles en 20 secondes. */
@Composable
fun StarGameScreen() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences(PREFS, Context.MODE_PRIVATE) }
    val haptics = LocalHapticFeedback.current

    var running by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var left by remember { mutableIntStateOf(GAME_SECONDS) }
    var last by remember { mutableStateOf<Int?>(null) }
    var best by remember { mutableIntStateOf(prefs.getInt(KEY_BEST, 0)) }
    var star by remember { mutableStateOf(randomSpot()) }

    LaunchedEffect(running) {
        if (!running) return@LaunchedEffect
        while (left > 0) {
            delay(1_000)
            left--
        }
        running = false
        last = score
        if (score > best) {
            best = score
            prefs.edit().putInt(KEY_BEST, score).apply()
        }
    }

    if (running) {
        GameBoard(
            score = score,
            left = left,
            star = star,
            onHit = {
                score++
                star = randomSpot()
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
            },
        )
    } else {
        GameIntro(
            last = last,
            best = best,
            onStart = {
                score = 0
                left = GAME_SECONDS
                star = randomSpot()
                running = true
            },
        )
    }
}

@Composable
private fun GameBoard(score: Int, left: Int, star: Offset, onHit: () -> Unit) {
    BoxWithConstraints(Modifier.fillMaxSize().background(Ink)) {
        val radius = maxWidth / 2

        // Anneau de temps restant
        Canvas(Modifier.fillMaxSize()) {
            val inset = 4.dp.toPx()
            val stroke = Stroke(width = 3.dp.toPx())
            val arcSize = Size(size.width - inset * 2, size.height - inset * 2)
            drawArc(LineStrong.copy(alpha = 0.12f), 0f, 360f, false, Offset(inset, inset), arcSize, style = stroke)
            drawArc(Paper, -90f, 360f * left / GAME_SECONDS, false, Offset(inset, inset), arcSize, style = stroke)
        }

        Label("$left s", modifier = Modifier.align(Alignment.TopCenter).padding(top = 16.dp))

        Text(
            text = score.toString(),
            style = Type.display(96, color = Paper.copy(alpha = 0.14f)),
            modifier = Modifier.align(Alignment.Center),
        )

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = radius * star.x, y = radius * star.y)
                .size(52.dp)
                .clip(CircleShape)
                .clickable(onClickLabel = "Attraper l'étoile", onClick = onHit),
            contentAlignment = Alignment.Center,
        ) {
            Icon(imageVector = PortfolioIcons.Star, contentDescription = "Étoile", tint = Paper, modifier = Modifier.size(34.dp))
        }
    }
}

@Composable
private fun GameIntro(last: Int?, best: Int, onStart: () -> Unit) {
    val listState = rememberScalingLazyListState()
    ScreenScaffold(listState = listState) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().background(Ink),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item { Label("Étoiles") }
            item { Text(text = "20 SECONDES", style = Type.display(34), textAlign = TextAlign.Center) }
            item {
                Text(
                    text = "Touchez un maximum d'étoiles avant la fin du tour.",
                    style = Type.body,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 14.dp),
                )
            }
            item {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Score(value = last?.toString() ?: "—", label = "Dernier")
                    Box(Modifier.width(1.dp).height(24.dp).background(Line))
                    Score(value = best.toString(), label = "Record")
                }
            }
            item { PillButton(text = "Jouer", onClick = onStart, modifier = Modifier.fillMaxWidth(0.7f)) }
        }
    }
}

@Composable
private fun Score(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = Type.display(22))
        Label(label)
    }
}
