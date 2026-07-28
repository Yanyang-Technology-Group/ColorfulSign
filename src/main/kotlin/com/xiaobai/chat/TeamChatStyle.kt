package com.xiaobai.chat

import org.bukkit.ChatColor

data class TeamChatStyle(
    val prefix: String,
    val playerNameColor: ChatColor,
    val suffix: String,
) {
    companion object {
        fun none(): TeamChatStyle = TeamChatStyle(
            prefix = "",
            playerNameColor = ChatColor.WHITE,
            suffix = "",
        )
    }
}
