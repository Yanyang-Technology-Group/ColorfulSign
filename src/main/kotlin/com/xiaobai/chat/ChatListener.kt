package com.xiaobai.chat

import io.papermc.paper.chat.ChatRenderer
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level

class ChatListener(
    private val plugin: JavaPlugin,
    private val messageRenderer: ChatMessageRenderer,
) : Listener {
    @EventHandler(ignoreCancelled = true)
    fun onPlayerChat(event: AsyncChatEvent) {
        val player = event.player
        val style = player.chatStyle(event.isAsynchronous)

        event.renderer(
            ChatRenderer.viewerUnaware { _, displayName, message ->
                messageRenderer.render(style, displayName, message)
            },
        )
    }

    private fun Player.chatStyle(isAsynchronous: Boolean): TeamChatStyle {
        if (isAsynchronous) {
            return runCatching {
                plugin.server.scheduler.callSyncMethod(plugin) { readChatStyle() }.get()
            }.getOrElse { failure ->
                plugin.logger.log(
                    Level.WARNING,
                    "Unable to read the player's team chat style; using the default chat style.",
                    failure,
                )
                TeamChatStyle.none()
            }
        }

        return readChatStyle()
    }

    private fun Player.readChatStyle(): TeamChatStyle {
        val team = scoreboard.getEntryTeam(name) ?: return TeamChatStyle.none()

        return TeamChatStyle(
            prefix = team.prefix(),
            playerNameColor = if (team.hasColor()) team.color() else NamedTextColor.WHITE,
            suffix = team.suffix(),
        )
    }
}
