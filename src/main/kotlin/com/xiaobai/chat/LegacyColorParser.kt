package com.xiaobai.chat

import org.bukkit.ChatColor

class LegacyColorParser {
    fun parse(message: String): ParsedMessage {
        val hasFormatting = FORMATTING_CODE.containsMatchIn(message)
        val colored = if (hasFormatting) {
            ChatColor.translateAlternateColorCodes('&', message)
        } else {
            message
        }
        return ParsedMessage(colored, hasFormatting)
    }

    data class ParsedMessage(
        val text: String,
        val hasFormatting: Boolean,
    )

    private companion object {
        val FORMATTING_CODE = Regex("&([0-9a-fk-or])", RegexOption.IGNORE_CASE)
    }
}
