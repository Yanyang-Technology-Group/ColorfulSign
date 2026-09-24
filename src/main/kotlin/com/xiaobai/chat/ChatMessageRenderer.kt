package com.xiaobai.chat

import org.bukkit.ChatColor

class ChatMessageRenderer(
    private val colorParser: LegacyColorParser,
) {
    fun render(style: TeamChatStyle, playerName: String, message: String): String {
        return String.format(format(style, playerName), playerName, formatMessage(message))
    }

    fun format(style: TeamChatStyle, playerName: String): String {
        val header = style.prefix + style.playerNameColor + playerName + ChatColor.RESET + style.suffix + ": "
        // Keep the body separate so other chat formatters can append it once.
        return header.replace("%", "%%") + "%2\$s"
    }

    fun formatMessage(message: String): String {
        val parsedMessage = colorParser.parse(message)
        return if (parsedMessage.hasFormatting) {
            parsedMessage.text
        } else {
            ChatColor.WHITE.toString() + parsedMessage.text
        }
    }
}
