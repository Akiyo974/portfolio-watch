package com.christendijoux.watch.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.christendijoux.watch.R
import com.christendijoux.watch.data.Portfolio
import com.christendijoux.watch.ui.Routes
import com.christendijoux.watch.ui.components.Label
import com.christendijoux.watch.ui.components.PortfolioIcons
import com.christendijoux.watch.ui.components.ScreenScaffold
import com.christendijoux.watch.ui.theme.Ink
import com.christendijoux.watch.ui.theme.Line
import com.christendijoux.watch.ui.theme.Paper
import com.christendijoux.watch.ui.theme.Type

private data class MenuEntry(
    val number: String,
    @StringRes val title: Int,
    val route: String,
    val count: String? = null,
    val icon: ImageVector? = null,
)

private val entries = listOf(
    MenuEntry("01", R.string.menu_projects, Routes.PROJECTS, count = "%02d".format(Portfolio.projects.size)),
    MenuEntry("02", R.string.menu_journey, Routes.JOURNEY),
    MenuEntry("03", R.string.menu_contact, Routes.CONTACT),
    MenuEntry("04", R.string.menu_game, Routes.GAME, icon = PortfolioIcons.Star),
)

/** Menu numéroté, comme celui du site. */
@Composable
fun MenuScreen(onNavigate: (String) -> Unit) {
    val listState = rememberScalingLazyListState()
    ScreenScaffold(listState = listState) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().background(Ink),
        ) {
            item { Label(stringResource(R.string.menu_title)) }
            items(entries) { entry ->
                MenuRow(entry = entry, onClick = { onNavigate(entry.route) })
            }
        }
    }
}

@Composable
private fun MenuRow(entry: MenuEntry, onClick: () -> Unit) {
    val title = stringResource(entry.title)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .clickable(role = Role.Button, onClickLabel = title, onClick = onClick)
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Label(entry.number)
            Text(text = title.uppercase(), style = Type.display(32))
            entry.count?.let { Label(it) }
            entry.icon?.let { Icon(imageVector = it, contentDescription = null, tint = Paper, modifier = Modifier.size(14.dp)) }
        }
        Box(Modifier.fillMaxWidth(0.7f).height(1.dp).background(Line))
    }
}
