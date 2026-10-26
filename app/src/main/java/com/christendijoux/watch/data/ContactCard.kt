package com.christendijoux.watch.data

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

/**
 * Carte de visite vCard 3.0, reprise du portfolio web (CV et JSON-LD) :
 * un scan avec l'appareil photo d'un téléphone propose d'ajouter le contact.
 * Sans paramètres TYPE= superflus : moins d'octets, donc des modules plus gros à l'écran.
 */
fun contactCard(jobTitle: String): String = listOf(
    "BEGIN:VCARD",
    "VERSION:3.0",
    "N:Dijoux;Christen;;;",
    "FN:${Portfolio.NAME}",
    "TITLE:${escape(jobTitle)}",
    "ORG:${escape(Portfolio.COMPANY)}",
    "TEL:${Portfolio.PHONE}",
    "EMAIL:${Portfolio.EMAIL}",
    "ADR:;;;${Portfolio.CITY};${Portfolio.REGION};;${Portfolio.COUNTRY}",
    "URL:${Portfolio.SITE}",
    "URL:${Portfolio.LINKEDIN}",
    "URL:${Portfolio.GITHUB}",
    "END:VCARD",
).joinToString("\r\n")

/** Caractères réservés des valeurs vCard (RFC 2426). */
private fun escape(value: String) =
    value.replace("\\", "\\\\").replace(",", "\\,").replace(";", "\\;")

/**
 * Matrice du QR code, un pixel par module et sans marge (la zone blanche est dessinée à l'affichage).
 * Correction d'erreur minimale : moins de modules, donc des carrés plus gros sur le petit écran.
 */
fun qrMatrix(text: String): BitMatrix = QRCodeWriter().encode(
    text,
    BarcodeFormat.QR_CODE,
    0,
    0,
    mapOf(
        EncodeHintType.CHARACTER_SET to "UTF-8",
        EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.L,
        EncodeHintType.MARGIN to 0,
    ),
)
