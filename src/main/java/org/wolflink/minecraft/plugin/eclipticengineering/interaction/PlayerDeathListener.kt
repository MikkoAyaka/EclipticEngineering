package org.wolflink.minecraft.plugin.eclipticengineering.interaction

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.wolflink.minecraft.plugin.eclipticengineering.extension.inGameDeath

object PlayerDeathListener: Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun on(e: PlayerDeathEvent) {
        e.isCancelled = true
        e.player.inGameDeath()
    }
}