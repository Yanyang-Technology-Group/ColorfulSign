package com.xiaobai.chat

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import kotlin.test.Test
import kotlin.test.assertEquals

class SignListenerTest {
    private val parser = LegacyColorParser()
    private val plainText = PlainTextComponentSerializer.plainText()

    @Test
    fun `applies color codes to sign lines`() {
        val line = Component.text("&cHello")
        val processed = parser.parse(plainText.serialize(line)).component

        assertEquals(NamedTextColor.RED, processed.color())
        assertEquals("Hello", plainText.serialize(processed))
    }

    @Test
    fun `leaves unformatted sign lines as white`() {
        val line = Component.text("Hello")
        val processed = parser.parse(plainText.serialize(line)).component

        assertEquals(NamedTextColor.WHITE, processed.color())
        assertEquals("Hello", plainText.serialize(processed))
    }

    @Test
    fun `does not process ordinary ampersands on sign lines`() {
        val line = Component.text("A & B")
        val processed = parser.parse(plainText.serialize(line)).component

        assertEquals(NamedTextColor.WHITE, processed.color())
        assertEquals("A & B", plainText.serialize(processed))
    }
}
