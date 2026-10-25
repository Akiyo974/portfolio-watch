@file:OptIn(ExperimentalTextApi::class)

package com.christendijoux.watch.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme
import com.christendijoux.watch.R

// Palette du portfolio web : encre, papier et lignes translucides
val Ink = Color(0xFF050505)
val Stage = Color(0xFF161616)
val Paper = Color(0xFFF2F2EE)
val PaperMuted = Paper.copy(alpha = 0.66f)
val PaperSoft = Paper.copy(alpha = 0.78f)
val Line = Paper.copy(alpha = 0.16f)
val LineStrong = Paper.copy(alpha = 0.28f)

private fun variable(res: Int, weight: Int) = Font(
    resId = res,
    weight = FontWeight(weight),
    variationSettings = FontVariation.Settings(FontVariation.weight(weight)),
)

/** Titres condensés (Big Shoulders Display). */
val Display = FontFamily(
    variable(R.font.big_shoulders_display, 300),
    variable(R.font.big_shoulders_display, 400),
)

/** Texte courant (Geist). */
val Sans = FontFamily(
    variable(R.font.geist, 400),
    variable(R.font.geist, 500),
)

/** Étiquettes (Geist Mono). */
val Mono = FontFamily(
    variable(R.font.geist_mono, 400),
    variable(R.font.geist_mono, 500),
)

object Type {
    /** Étiquette mono majuscule, comme `.label` sur le site. */
    val label = TextStyle(fontFamily = Mono, fontSize = 11.sp, letterSpacing = 0.12.em, color = PaperMuted)

    val body = TextStyle(fontFamily = Sans, fontSize = 13.sp, lineHeight = 18.sp, color = PaperSoft)

    fun display(size: Int, color: Color = Paper) = TextStyle(
        fontFamily = Display,
        fontWeight = FontWeight.Light,
        fontSize = size.sp,
        lineHeight = (size * 0.9).sp,
        color = color,
    )
}

@Composable
fun PortfolioTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = Colors(
            primary = Paper,
            onPrimary = Ink,
            secondary = Paper,
            onSecondary = Ink,
            background = Ink,
            onBackground = Paper,
            surface = Stage,
            onSurface = Paper,
            onSurfaceVariant = PaperMuted,
        ),
        content = content,
    )
}
