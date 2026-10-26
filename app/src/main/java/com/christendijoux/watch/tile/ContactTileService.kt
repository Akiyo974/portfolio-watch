package com.christendijoux.watch.tile

import android.content.Context
import androidx.annotation.StringRes
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.ColorBuilders.argb
import androidx.wear.protolayout.DeviceParametersBuilders.DeviceParameters
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.material.Button
import androidx.wear.protolayout.material.ButtonColors
import androidx.wear.protolayout.material.ChipColors
import androidx.wear.protolayout.material.CompactChip
import androidx.wear.protolayout.material.Text
import androidx.wear.protolayout.material.Typography
import androidx.wear.protolayout.material.layouts.MultiButtonLayout
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.TileService
import com.christendijoux.watch.MainActivity
import com.christendijoux.watch.R
import com.christendijoux.watch.data.Portfolio
import com.christendijoux.watch.ui.Shortcuts
import com.christendijoux.watch.util.AppLanguage
import com.google.common.util.concurrent.ListenableFuture

// Palette du portfolio (les tuiles n'ont accès ni au thème Compose ni aux polices embarquées)
private const val INK = 0xFF050505.toInt()
private const val STAGE = 0xFF161616.toInt()
private const val PAPER = 0xFFF2F2EE.toInt()
private const val PAPER_MUTED = 0xA8F2F2EE.toInt()

/** À changer quand les icônes changent, pour que le système recharge les ressources. */
private const val RESOURCES_VERSION = "1"
private const val ICON_QR = "qr"
private const val ICON_CALL = "call"
private const val ICON_MAIL = "mail"

/**
 * Tuile « Christen » : disponibilité et accès direct à la carte QR, à l'appel et à l'e-mail,
 * en un glissement depuis le cadran. Chaque bouton ouvre l'app sur l'écran voulu.
 */
class ContactTileService : TileService() {

    override fun onTileRequest(requestParams: RequestBuilders.TileRequest): ListenableFuture<TileBuilders.Tile> {
        // Même langue que celle choisie dans l'app, pas seulement celle de la montre
        val context = AppLanguage.wrap(this)
        val tile = TileBuilders.Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTileTimeline(TimelineBuilders.Timeline.fromLayoutElement(layout(context, requestParams.deviceConfiguration)))
            .build()
        return immediate(tile)
    }

    override fun onTileResourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest,
    ): ListenableFuture<ResourceBuilders.Resources> = immediate(
        ResourceBuilders.Resources.Builder()
            .setVersion(RESOURCES_VERSION)
            .addIdToImageMapping(ICON_QR, image(R.drawable.ic_tile_qr))
            .addIdToImageMapping(ICON_CALL, image(R.drawable.ic_tile_phone))
            .addIdToImageMapping(ICON_MAIL, image(R.drawable.ic_tile_mail))
            .build(),
    )
}

private fun layout(context: Context, device: DeviceParameters): LayoutElementBuilders.LayoutElement =
    PrimaryLayout.Builder(device)
        .setPrimaryLabelTextContent(
            Text.Builder(context, Portfolio.NAME.uppercase())
                .setTypography(Typography.TYPOGRAPHY_CAPTION1)
                .setColor(argb(PAPER))
                .build(),
        )
        .setSecondaryLabelTextContent(
            Text.Builder(context, "● " + context.getString(R.string.contact_availability))
                .setTypography(Typography.TYPOGRAPHY_CAPTION2)
                .setColor(argb(PAPER_MUTED))
                .build(),
        )
        .setContent(
            MultiButtonLayout.Builder()
                .addButtonContent(button(context, ICON_QR, R.string.contact_card, Shortcuts.QR, filled = true))
                .addButtonContent(button(context, ICON_CALL, R.string.contact_call, Shortcuts.CALL))
                .addButtonContent(button(context, ICON_MAIL, R.string.contact_email, Shortcuts.EMAIL))
                .build(),
        )
        .setPrimaryChipContent(
            CompactChip.Builder(context, context.getString(R.string.tile_open_app), launch(context, null), device)
                .setChipColors(ChipColors(STAGE, PAPER))
                .build(),
        )
        .build()

private fun button(context: Context, icon: String, @StringRes label: Int, shortcut: String, filled: Boolean = false) =
    Button.Builder(context, launch(context, shortcut))
        .setIconContent(icon)
        .setContentDescription(context.getString(label))
        .setButtonColors(if (filled) ButtonColors(PAPER, INK) else ButtonColors(STAGE, PAPER))
        .build()

/** Ouvre l'app, sur l'écran du raccourci s'il y en a un. */
private fun launch(context: Context, shortcut: String?): ModifiersBuilders.Clickable {
    val activity = ActionBuilders.AndroidActivity.Builder()
        .setPackageName(context.packageName)
        .setClassName(MainActivity::class.java.name)
    if (shortcut != null) {
        activity.addKeyToExtraMapping(
            Shortcuts.EXTRA,
            ActionBuilders.AndroidStringExtra.Builder().setValue(shortcut).build(),
        )
    }
    return ModifiersBuilders.Clickable.Builder()
        .setId(shortcut ?: "open")
        .setOnClick(ActionBuilders.LaunchAction.Builder().setAndroidActivity(activity.build()).build())
        .build()
}

private fun image(resId: Int) = ResourceBuilders.ImageResource.Builder()
    .setAndroidResourceByResId(ResourceBuilders.AndroidImageResourceByResId.Builder().setResourceId(resId).build())
    .build()

private fun <T> immediate(value: T): ListenableFuture<T> =
    CallbackToFutureAdapter.getFuture { completer ->
        completer.set(value)
        "immediate"
    }
