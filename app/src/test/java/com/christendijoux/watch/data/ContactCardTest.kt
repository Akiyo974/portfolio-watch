package com.christendijoux.watch.data

import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.BitMatrix
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Copies des titres de values/strings.xml et values-fr/strings.xml (les tests JVM n'ont pas les ressources). */
private const val TITLE_EN = "Digital Innovation Specialist · Full-Stack & AI Developer"
private const val TITLE_FR = "Spécialiste en innovation numérique · Développeur full-stack & IA"

/**
 * Taille maximale du QR : 65 modules (version 12) restent lisibles sur une montre de 1,4 pouce.
 * Si ce test échoue, la carte est devenue trop longue : raccourcir le titre ou retirer un lien.
 */
private const val MAX_MODULES = 65

class ContactCardTest {

    @Test
    fun `la carte contient toutes les coordonnées`() {
        val lines = contactCard(TITLE_EN).split("\r\n")
        assertEquals("BEGIN:VCARD", lines.first())
        assertEquals("END:VCARD", lines.last())
        listOf(
            "VERSION:3.0",
            "FN:${Portfolio.NAME}",
            "TEL:${Portfolio.PHONE}",
            "EMAIL:${Portfolio.EMAIL}",
            "URL:${Portfolio.SITE}",
            "URL:https://linkedin.com/in/christen-dijoux",
            "URL:${Portfolio.GITHUB}",
        ).forEach { assertTrue("Ligne manquante : $it", it in lines) }
    }

    @Test
    fun `les caractères réservés du titre sont échappés`() {
        val card = contactCard("Dev, web; IA\\data")
        assertTrue(card.contains("TITLE:Dev\\, web\\; IA\\\\data"))
    }

    @Test
    fun `le QR reste assez petit pour la montre, dans les deux langues`() {
        listOf(TITLE_EN, TITLE_FR).forEach { title ->
            val size = qrMatrix(contactCard(title)).width
            assertTrue("QR de $size modules pour « $title »", size <= MAX_MODULES)
        }
    }

    @Test
    fun `le QR se relit à l'identique, accents compris`() {
        listOf(TITLE_EN, TITLE_FR).forEach { title ->
            val card = contactCard(title)
            assertEquals(card, decode(qrMatrix(card)))
        }
    }
}

/** Relit la matrice comme le ferait l'appareil photo, avec la zone blanche de 4 modules. */
private fun decode(matrix: BitMatrix, scale: Int = 4, quietZone: Int = 4): String {
    val side = (matrix.width + quietZone * 2) * scale
    val pixels = IntArray(side * side) { 0xFFFFFFFF.toInt() }
    for (y in 0 until matrix.height) {
        for (x in 0 until matrix.width) {
            if (!matrix[x, y]) continue
            for (dy in 0 until scale) {
                for (dx in 0 until scale) {
                    pixels[((y + quietZone) * scale + dy) * side + (x + quietZone) * scale + dx] = 0xFF000000.toInt()
                }
            }
        }
    }
    val bitmap = BinaryBitmap(HybridBinarizer(RGBLuminanceSource(side, side, pixels)))
    return QRCodeReader().decode(bitmap, mapOf(DecodeHintType.CHARACTER_SET to "UTF-8")).text
}
