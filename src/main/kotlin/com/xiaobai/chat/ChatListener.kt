package com.xiaobai.chat

import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class ChatListener(
    private val messageRenderer: ChatMessageRenderer,
) : Listener {
    @EventHandler(ignoreCancelled = true)
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        val player = event.player
        val style = player.chatStyle()
        val message = event.message

        event.format = messageRenderer.render(style, player.name, message)
    }

    private fun Player.chatStyle(): TeamChatStyle {
        val team = scoreboard.getEntryTeam(name) ?: return TeamChatStyle.none()

        return TeamChatStyle(
            prefix = team.prefix ?: "",
            playerNameColor = team.color ?: ChatColor.WHITE,
            suffix = team.suffix ?: "",
        )
    }
}
