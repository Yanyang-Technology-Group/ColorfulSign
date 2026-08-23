package com.xiaobai.chat

import org.bukkit.block.Sign
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
        val original = (0 until 4).map { event.getLine(it) ?: "" }
        val parsed = original.map { colorParser.parse(it).text }
        if (parsed == original) {
            return
        }

        for (i in 0 until 4) {
            event.setLine(i, parsed[i])
        }

        // 兜底：事件落库后直接写回告示牌并强制刷新客户端，
        // 兼容部分服务端不应用事件修改或客户端显示陈旧的情况。
        plugin.server.scheduler.runTask(plugin, Runnable {
            val state = event.block.state
            if (state is Sign) {
                val side = state.getSide(event.side)
                for (i in 0 until 4) {
                    side.setLine(i, parsed[i])
                }
                state.update(true, true)
            }
        })
    }
}
