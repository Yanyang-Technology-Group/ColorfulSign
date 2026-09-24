package com.xiaobai.chat

import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.scoreboard.Scoreboard
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import kotlin.test.Test
import kotlin.test.assertEquals

class ChatListenerTest {
    private val listener = ChatListener(ChatMessageRenderer(LegacyColorParser()))

    @Test
    fun `hover chat formatter appends message only once`() {
        val event = chatEvent("1")

        listener.onPlayerChat(event)

        // CMI's legacy hover formatter removes the body placeholder from the
        // format, then appends the message separately as a component.
        val header = event.format.replace("%1\$s", "Alex").replace("%2\$s", "")
        assertEquals("Alex: 1", ChatColor.stripColor(header + event.message))
        assertEquals("§fAlex§r: %2\$s", event.format)
    }

    @Test
    fun `message color codes and percent signs remain message data`() {
        for ((input, expected) in listOf(
            "Hello" to "§fHello",
            "&c你好 100% %s %2\$s" to "§c你好 100% %s %2\$s",
        )) {
            val event = chatEvent(input)

            listener.onPlayerChat(event)

            assertEquals(expected, event.message)
            assertEquals("§fAlex§r: $expected", String.format(event.format, "Alex", event.message))
        }
    }

    private fun chatEvent(message: String): AsyncPlayerChatEvent {
        val scoreboard = mock(Scoreboard::class.java)
        val player = mock(Player::class.java)
        `when`(player.name).thenReturn("Alex")
        `when`(player.displayName).thenReturn("Alex")
        `when`(player.scoreboard).thenReturn(scoreboard)
        return AsyncPlayerChatEvent(true, player, message, mutableSetOf())
    }
}
