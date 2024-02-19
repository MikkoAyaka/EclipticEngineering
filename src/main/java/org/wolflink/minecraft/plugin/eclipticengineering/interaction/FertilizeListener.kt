package org.wolflink.minecraft.plugin.eclipticengineering.interaction

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockFertilizeEvent
import java.util.Random

object FertilizeListener: Listener {
    private val random = Random()
    private const val SUCCESS_RATE = 0.07
    @EventHandler
    fun on(e: BlockFertilizeEvent) {
        if(random.nextDouble() <= SUCCESS_RATE) return
        else e.isCancelled = true
    }
}