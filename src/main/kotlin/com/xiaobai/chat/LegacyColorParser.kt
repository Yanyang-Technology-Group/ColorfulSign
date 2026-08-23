package com.xiaobai.chat

import org.bukkit.ChatColor

class LegacyColorParser {
    fun parse(message: String): ParsedMessage {
        val hasFormatting = FORMATTING_CODE.containsMatchIn(message) || HEX_RGB_CODE.containsMatchIn(message)
        val colored = if (hasFormatting) {
            translate(message)
        } else {
            message
        }
        return ParsedMessage(colored, hasFormatting)
    }

    private fun translate(message: String): String {
        val expanded = HEX_RGB_CODE.replace(message) { match ->
            val hex = match.groupValues[1]
            val builder = StringBuilder("&x")
            for (ch in hex) {
                builder.append('&').append(ch.lowercaseChar())
            }
            builder.toString()
        }
        return ChatColor.translateAlternateColorCodes('&', expanded)
    }

    data class ParsedMessage(
        val text: String,
        val hasFormatting: Boolean,
    )

    private companion object {
        val FORMATTING_CODE = Regex("&([0-9a-fk-or])", RegexOption.IGNORE_CASE)
        val HEX_RGB_CODE = Regex("&#([0-9a-fA-F]{6})")
    }
}
