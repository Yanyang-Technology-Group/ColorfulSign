package com.xiaobai.chat

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.SignChangeEvent

class SignListener(
    private val colorParser: LegacyColorParser,
    private val plainTextSerializer: PlainTextComponentSerializer = PlainTextComponentSerializer.plainText(),
) : Listener {
    @EventHandler(ignoreCancelled = true)
    fun onSignChange(event: SignChangeEvent) {
        for (i in 0 until 4) {
            val line = event.line(i) ?: continue
            val parsed = colorParser.parse(plainTextSerializer.serialize(line))
            event.line(i, parsed.component)
        }
    }
}
