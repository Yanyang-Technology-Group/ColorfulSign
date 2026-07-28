package com.xiaobai.chat

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor

data class TeamChatStyle(
    val prefix: Component,
    val playerNameColor: TextColor,
    val suffix: Component,
) {
    companion object {
        fun none(): TeamChatStyle = TeamChatStyle(
            prefix = Component.empty(),
            playerNameColor = NamedTextColor.WHITE,
            suffix = Component.empty(),
        )
    }
}
