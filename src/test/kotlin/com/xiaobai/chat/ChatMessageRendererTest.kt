package com.xiaobai.chat

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import kotlin.test.Test
import kotlin.test.assertEquals

class ChatMessageRendererTest {
    private val renderer = ChatMessageRenderer(LegacyColorParser())
    private val plainText = PlainTextComponentSerializer.plainText()

    @Test
    fun `renders team prefix colored player name suffix and message in order`() {
        val style = TeamChatStyle(
            prefix = Component.text("[P] "),
            playerNameColor = NamedTextColor.GOLD,
            suffix = Component.text(" [S]"),
        )

        val rendered = renderer.render(style, Component.text("Alex"), Component.text("Hello"))

        assertEquals("[P] Alex [S]: Hello", plainText.serialize(rendered))
    }

    @Test
    fun `uses white for an unformatted message without a team`() {
        val rendered = renderer.render(TeamChatStyle.none(), Component.text("Alex"), Component.text("Hello"))

        val message = rendered.children().last()
        assertEquals(NamedTextColor.WHITE, message.color())
        assertEquals("Alex: Hello", plainText.serialize(rendered))
    }

    @Test
    fun `preserves display name interaction while applying team color`() {
        val clickEvent = ClickEvent.runCommand("/profile Alex")
        val style = TeamChatStyle(
            prefix = Component.empty(),
            playerNameColor = NamedTextColor.GOLD,
            suffix = Component.empty(),
        )

        val rendered = renderer.render(
            style,
            Component.text("Alex").clickEvent(clickEvent),
            Component.text("Hello"),
        )

        val displayName = rendered.children()[0]
        assertEquals(NamedTextColor.GOLD, displayName.color())
        assertEquals(clickEvent, displayName.clickEvent())
    }
}
