package com.xiaobai

import com.xiaobai.chat.ChatListener
import com.xiaobai.chat.ChatMessageRenderer
import com.xiaobai.chat.LegacyColorParser
import com.xiaobai.chat.SignListener
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.plugin.java.JavaPlugin

class ColorfulSignPlugin : JavaPlugin() {
    override fun onEnable() {
        val colorParser = LegacyColorParser()

        server.pluginManager.registerEvents(
            ChatListener(this, ChatMessageRenderer(colorParser)),
            this,
        )

        server.pluginManager.registerEvents(
            SignListener(colorParser),
            this,
        )

        logger.info("核心插件已启用。")
        displayStartupMessage()
    }

    private fun displayStartupMessage() {
        val console = server.consoleSender
        console.sendMessage(Component.text("╔════════════════════════════════════════╗", NamedTextColor.DARK_AQUA))
        console.sendMessage(
            Component.text("           ◆ ", NamedTextColor.GOLD)
                .append(Component.text("ColorFulSign 插件", NamedTextColor.GOLD))
                .append(Component.text(" ◆", NamedTextColor.GOLD)),
        )
        console.sendMessage(Component.text("╠════════════════════════════════════════╣", NamedTextColor.DARK_AQUA))
        console.sendMessage(
            Component.text("    插件版本: ", NamedTextColor.AQUA)
                .append(Component.text("v${pluginMeta.version}", NamedTextColor.GREEN)),
        )
        console.sendMessage(
            Component.text("    技术支持: ", NamedTextColor.AQUA)
                .append(Component.text("晏阳技术组", NamedTextColor.BLUE)),
        )
        console.sendMessage(Component.text("╠════════════════════════════════════════╣", NamedTextColor.DARK_AQUA))
        console.sendMessage(Component.text("  感谢您支持 晏阳技术组！", NamedTextColor.LIGHT_PURPLE))
        console.sendMessage(Component.text("    祝您游戏愉快！■", NamedTextColor.LIGHT_PURPLE))
        console.sendMessage(Component.text("╚════════════════════════════════════════╝", NamedTextColor.DARK_AQUA))
    }
}
