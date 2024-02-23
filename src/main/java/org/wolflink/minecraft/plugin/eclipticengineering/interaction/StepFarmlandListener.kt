package org.wolflink.minecraft.plugin.eclipticengineering.interaction

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

object StepFarmlandListener: Listener {
    @EventHandler
    fun on(e: PlayerInteractEvent) {
        val block = e.clickedBlock ?: return
        if(block.type == Material.FARMLAND && e.action == Action.PHYSICAL)
        {
            e.isCancelled = true
        }
    }
}