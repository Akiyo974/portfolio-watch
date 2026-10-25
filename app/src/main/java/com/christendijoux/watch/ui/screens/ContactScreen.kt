package com.christendijoux.watch.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Text
import com.christendijoux.watch.R
import com.christendijoux.watch.data.Portfolio
import com.christendijoux.watch.ui.components.Label
import com.christendijoux.watch.ui.components.PillButton
import com.christendijoux.watch.ui.components.PortfolioIcons
import com.christendijoux.watch.ui.components.RoundAction
import com.christendijoux.watch.ui.components.ScreenScaffold
import com.christendijoux.watch.ui.theme.Ink
import com.christendijoux.watch.ui.theme.Paper
import com.christendijoux.watch.ui.theme.Type
import com.christendijoux.watch.util.call
import com.christendijoux.watch.util.openOnPhone

/** Contact : chaque action s'ouvre sur la montre ou sur le téléphone associé ; la carte de visite s'affiche en QR code. */
@Composable
fun ContactScreen(onShowQr: () -> Unit) {
    val context = LocalContext.current
    val listState = rememberScalingLazyListState()

    ScreenScaffold(listState = listState) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().background(Ink),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item { Label(stringResource(R.string.menu_contact)) }
            item { Text(text = stringResource(R.string.contact_title).uppercase(), style = Type.display(34), textAlign = TextAlign.Center) }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    RoundAction(
                        icon = PortfolioIcons.Mail,
                        label = stringResource(R.string.contact_email),
                        filled = true,
                        onClick = { context.openOnPhone("mailto:${Portfolio.EMAIL}") },
                    )
                    RoundAction(
                        icon = PortfolioIcons.Phone,
                        label = stringResource(R.string.contact_call),
                        onClick = { context.call(Portfolio.PHONE) },
                    )
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    RoundAction(
                        icon = PortfolioIcons.LinkedIn,
                        label = "LinkedIn",
                        onClick = { context.openOnPhone(Portfolio.LINKEDIN) },
                    )
                    RoundAction(
                        icon = PortfolioIcons.GitHub,
                        label = "GitHub",
                        onClick = { context.openOnPhone(Portfolio.GITHUB) },
                    )
                }
            }
            item {
                PillButton(
                    text = stringResource(R.string.contact_card),
                    icon = PortfolioIcons.QrCode,
                    onClick = onShowQr,
                    modifier = Modifier.fillMaxWidth(0.7f),
                )
            }
            item {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Box(Modifier.size(5.dp).background(Paper, CircleShape))
                    Label(stringResource(R.string.contact_availability))
                }
            }
        }
    }
}
