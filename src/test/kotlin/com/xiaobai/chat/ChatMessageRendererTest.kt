package com.xiaobai.chat

import kotlin.test.Test
import kotlin.test.assertEquals

class ChatMessageRendererTest {
    private val renderer = ChatMessageRenderer(LegacyColorParser())

    @Test
    fun `renders team prefix colored player name suffix and message in order`() {
        val style = TeamChatStyle(
            prefix = "[P] ",
            playerNameColor = org.bukkit.ChatColor.GOLD,
            suffix = " [S]",
        )

        val rendered = renderer.render(style, "Alex", "Hello")

        assertEquals("[P] §6Alex§r [S]: §fHello", rendered)
    }

    @Test
    fun `uses white for an unformatted message without a team`() {
        val rendered = renderer.render(TeamChatStyle.none(), "Alex", "Hello")

        assertEquals("§fAlex§r: §fHello", rendered)
    }
}
