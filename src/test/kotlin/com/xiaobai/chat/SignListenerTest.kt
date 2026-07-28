package com.xiaobai.chat

import kotlin.test.Test
import kotlin.test.assertEquals

class SignListenerTest {
    private val parser = LegacyColorParser()

    @Test
    fun `applies color codes to sign lines`() {
        val parsed = parser.parse("&cHello")

        assertEquals("§cHello", parsed.text)
    }

    @Test
    fun `leaves unformatted sign lines unchanged`() {
        val parsed = parser.parse("Hello")

        assertEquals("Hello", parsed.text)
    }

    @Test
    fun `does not process ordinary ampersands on sign lines`() {
        val parsed = parser.parse("A & B")

        assertEquals("A & B", parsed.text)
    }
}
