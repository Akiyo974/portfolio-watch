package com.christendijoux.watch.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import com.christendijoux.watch.data.Portfolio
import com.christendijoux.watch.ui.components.Label
import com.christendijoux.watch.ui.theme.Ink
import com.christendijoux.watch.ui.theme.PaperMuted
import com.christendijoux.watch.ui.theme.Type
import com.christendijoux.watch.util.rememberCurrentTime
import java.time.format.DateTimeFormatter

private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

/**
 * Écran de veille (écran toujours allumé) : presque tout noir, sans aplat clair,
 * pour économiser la batterie et éviter le marquage de l'écran OLED.
 */
@Composable
fun AmbientScreen() {
    val now by rememberCurrentTime()
    Column(
        modifier = Modifier.fillMaxSize().background(Ink),
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = now.format(timeFormat), style = Type.display(60, color = PaperMuted))
        Label(Portfolio.NAME)
    }
}
