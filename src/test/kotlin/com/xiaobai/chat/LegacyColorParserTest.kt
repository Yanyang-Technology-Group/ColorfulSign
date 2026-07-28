package com.xiaobai.chat

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
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
        assertEquals("Hello", PlainTextComponentSerializer.plainText().serialize(parsed.component))
        assertEquals(NamedTextColor.GREEN, parsed.component.color())
    }

    @Test
    fun `does not treat ordinary ampersands as formatting`() {
        val parsed = parser.parse("A & B")

        assertFalse(parsed.hasFormatting)
        assertEquals("A & B", PlainTextComponentSerializer.plainText().serialize(parsed.component))
    }

    @Test
    fun `recognizes uppercase color and formatting codes`() {
        val parsed = parser.parse("&AHello &LWorld")

        assertTrue(parsed.hasFormatting)
        assertEquals("Hello World", PlainTextComponentSerializer.plainText().serialize(parsed.component))
    }

    @Test
    fun `preserves component interaction when parsing legacy formatting`() {
        val clickEvent = ClickEvent.runCommand("/help")
        val parsed = parser.parse(Component.text("&aHello").clickEvent(clickEvent))

        assertTrue(parsed.hasFormatting)
        assertEquals(NamedTextColor.GREEN, parsed.component.color())
        assertEquals(clickEvent, parsed.component.clickEvent())
    }
}
