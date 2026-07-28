package com.xiaobai.chat

import net.kyori.adventure.text.Component

class ChatMessageRenderer(
    private val colorParser: LegacyColorParser,
) {
    fun render(style: TeamChatStyle, displayName: Component, message: Component): Component {
        val parsedMessage = colorParser.parse(message)

        return Component.empty()
            .append(style.prefix)
            .append(displayName.color(style.playerNameColor))
            .append(style.suffix)
            .append(Component.text(": "))
            .append(parsedMessage.component)
    }
}
