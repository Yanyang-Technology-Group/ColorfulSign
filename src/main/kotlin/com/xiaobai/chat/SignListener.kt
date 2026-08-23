package com.xiaobai.chat

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.plugin.java.JavaPlugin

class SignListener(
    private val colorParser: LegacyColorParser,
    private val plugin: JavaPlugin,
) : Listener {
    @EventHandler(ignoreCancelled = true)
    fun onSignChange(event: SignChangeEvent) {
        var changed = false
        for (i in 0 until 4) {
            val line = event.getLine(i) ?: continue
            val parsed = colorParser.parse(line)
            if (parsed.text != line) {
                event.setLine(i, parsed.text)
                changed = true
            }
        }
        if (changed) {
            val location = event.block.location
            plugin.logger.info(
                "已为告示牌应用颜色码：${location.world.name} (${location.blockX}, ${location.blockY}, ${location.blockZ})",
            )
        }
    }
}
