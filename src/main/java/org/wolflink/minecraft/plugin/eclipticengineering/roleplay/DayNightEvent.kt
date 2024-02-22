package org.wolflink.minecraft.plugin.eclipticengineering.roleplay

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class DayNightEvent(val nowTime: DayNightHandler.Status): Event() {

    override fun getHandlers() = handlerList
    companion object {
        private val handlerList = HandlerList()
        @JvmStatic
        fun getHandlerList() = handlerList
    }
}