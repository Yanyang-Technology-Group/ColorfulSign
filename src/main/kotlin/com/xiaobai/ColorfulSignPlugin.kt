package com.xiaobai

import com.xiaobai.chat.ChatListener
import com.xiaobai.chat.ChatMessageRenderer
import com.xiaobai.chat.LegacyColorParser
import com.xiaobai.chat.SignListener
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin

class ColorfulSignPlugin : JavaPlugin() {
    override fun onEnable() {
        val colorParser = LegacyColorParser()

        server.pluginManager.registerEvents(
            ChatListener(ChatMessageRenderer(colorParser)),
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
        val da = ChatColor.DARK_AQUA
        console.sendMessage("${da}╔════════════════════════════════════════╗")
        console.sendMessage("${ChatColor.GOLD}           ◆ ColorFulSign 插件 ◆")
        console.sendMessage("${da}╠════════════════════════════════════════╣")
        console.sendMessage("${ChatColor.AQUA}    插件版本: ${ChatColor.GREEN}v${pluginMeta.version}")
        console.sendMessage("${ChatColor.AQUA}    技术支持: ${ChatColor.BLUE}晏阳技术组")
        console.sendMessage("${da}╠════════════════════════════════════════╣")
        console.sendMessage("${ChatColor.LIGHT_PURPLE}  感谢您支持 晏阳技术组！")
        console.sendMessage("${ChatColor.LIGHT_PURPLE}    祝您游戏愉快！■")
        console.sendMessage("${da}╚════════════════════════════════════════╝")
    }
}
