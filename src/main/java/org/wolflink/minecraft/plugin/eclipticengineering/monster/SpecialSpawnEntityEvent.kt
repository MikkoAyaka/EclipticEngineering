package org.wolflink.minecraft.plugin.eclipticengineering.monster

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class SpecialSpawnEntityEvent(val belongPlayer: Player, val entity: Entity) : Event() {
    override fun getHandlers(): HandlerList {
        return handlerList
    }
    companion object {
        val handlerList = HandlerList()
    }
}
