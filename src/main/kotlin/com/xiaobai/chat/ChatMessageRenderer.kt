package com.xiaobai.chat

import org.bukkit.ChatColor

class ChatMessageRenderer(
    private val colorParser: LegacyColorParser,
) {
    fun render(style: TeamChatStyle, playerName: String, message: String): String {
        val parsedMessage = colorParser.parse(message)
        val messageText = if (parsedMessage.hasFormatting) {
            parsedMessage.text
        } else {
            ChatColor.WHITE.toString() + parsedMessage.text
        }
        return style.prefix + style.playerNameColor + playerName + ChatColor.RESET + style.suffix + ": " + messageText
    }
}
