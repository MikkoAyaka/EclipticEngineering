package org.wolflink.minecraft.plugin.eclipticengineering.roleplay

import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.wolflink.minecraft.plugin.eclipticengineering.extension.onlinePlayers

object RangeChat: Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun on(e: AsyncChatEvent) {
        e.isCancelled = true
        onlinePlayers
            .filter { it.location.distance(e.player.location) <= 12 }
            .forEach { it.sendMessage(e.message()) }
    }
}