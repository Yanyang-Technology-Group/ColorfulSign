package com.xiaobai.chat

import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.SignChangeEvent

class SignListener(
    private val colorParser: LegacyColorParser,
) : Listener {
    @EventHandler(ignoreCancelled = true)
    fun onSignChange(event: SignChangeEvent) {
        for (i in 0 until 4) {
            val line = event.getLine(i) ?: continue
            event.setLine(i, colorParser.parse(line).text)
        }
    }
}
