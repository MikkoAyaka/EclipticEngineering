package org.wolflink.minecraft.plugin.eclipticengineering.roleplay

import org.bukkit.Bukkit
import org.bukkit.entity.Monster
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.wolflink.minecraft.plugin.eclipticengineering.config.Config

object DayListener: Listener {
    @EventHandler
    fun on(e: DayNightEvent) {
        if(e.nowTime == DayNightHandler.Status.DAY) {
            Config.gameWorld.entities
                .filterIsInstance<Monster>()
                .filter { it.hasAI() }
                .forEach { it.remove() }
        }
    }
}