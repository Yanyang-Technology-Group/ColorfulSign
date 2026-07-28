package com.xiaobai.chat

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LegacyColorParserTest {
    private val parser = LegacyColorParser()

    @Test
    fun `recognizes and parses valid ampersand color codes`() {
        val parsed = parser.parse("&aHello")

        assertTrue(parsed.hasFormatting)
        assertEquals("§aHello", parsed.text)
    }

    @Test
    fun `does not treat ordinary ampersands as formatting`() {
        val parsed = parser.parse("A & B")

        assertFalse(parsed.hasFormatting)
        assertEquals("A & B", parsed.text)
    }

    @Test
    fun `recognizes uppercase color and formatting codes`() {
        val parsed = parser.parse("&AHello &LWorld")

        assertTrue(parsed.hasFormatting)
        assertEquals("§aHello §lWorld", parsed.text)
    }
}
