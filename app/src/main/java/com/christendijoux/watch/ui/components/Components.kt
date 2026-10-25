package com.christendijoux.watch.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.scrollAway
import com.christendijoux.watch.ui.theme.Ink
import com.christendijoux.watch.ui.theme.LineStrong
import com.christendijoux.watch.ui.theme.Paper
import com.christendijoux.watch.ui.theme.PaperSoft
import com.christendijoux.watch.ui.theme.Type

/** Écran standard : heure en haut, vignette et indicateur de défilement courbe. */
@Composable
fun ScreenScaffold(
    listState: ScalingLazyListState? = null,
    showTime: Boolean = true,
    content: @Composable () -> Unit,
) {
    Scaffold(
        modifier = Modifier.background(Ink),
        timeText = {
            if (showTime) {
                // L'heure s'efface quand la liste défile, pour ne pas chevaucher le contenu
                TimeText(
                    timeTextStyle = Type.label,
                    modifier = if (listState != null) Modifier.scrollAway(listState, itemIndex = 1) else Modifier,
                )
            }
        },
        vignette = { if (listState != null) Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { if (listState != null) PositionIndicator(scalingLazyListState = listState) },
        content = content,
    )
}

/** Étiquette mono majuscule. */
@Composable
fun Label(text: String, modifier: Modifier = Modifier) {
    Text(text = text.uppercase(), style = Type.label, textAlign = TextAlign.Center, modifier = modifier)
}

/** Bouton principal plein (papier sur encre), en pilule. */
@Composable
fun PillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .background(Paper, RoundedCornerShape(50))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = text.uppercase(), style = Type.label.copy(color = Ink))
        if (icon != null) Icon(imageVector = icon, contentDescription = null, tint = Ink, modifier = Modifier.size(12.dp))
    }
}

/** Petite étiquette arrondie (technologies d'un projet). */
@Composable
fun Tag(text: String) {
    Text(
        text = text.uppercase(),
        style = Type.label.copy(color = PaperSoft, fontSize = Type.label.fontSize * 0.85f),
        modifier = Modifier
            .border(1.dp, LineStrong, RoundedCornerShape(50))
            .padding(horizontal = 7.dp, vertical = 3.dp),
    )
}

/** Bouton rond à icône, avec son libellé dessous (écran Contact). */
@Composable
fun RoundAction(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    filled: Boolean = false,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .then(if (filled) Modifier.background(Paper, CircleShape) else Modifier.border(1.dp, LineStrong, CircleShape))
                .clickable(role = Role.Button, onClickLabel = label, onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = if (filled) Ink else Paper, modifier = Modifier.size(20.dp))
        }
        Label(label)
    }
}
