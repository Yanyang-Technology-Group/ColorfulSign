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

    @Test
    fun `parses hex rgb codes`() {
        val parsed = parser.parse("&#ff0000Hello")

        assertTrue(parsed.hasFormatting)
        assertEquals("§x§f§f§0§0§0§0Hello", parsed.text)
    }

    @Test
    fun `parses legacy hex code format`() {
        val parsed = parser.parse("&x&f&f&0&0&0&0Hello")

        assertTrue(parsed.hasFormatting)
        assertEquals("§x§f§f§0§0§0§0Hello", parsed.text)
    }
}
