package org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightEvent
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler

class EatPufferfish(disguiser: Player): PlayerGoal(disguiser,Difficulty.EASY) {
    override val description: String = "吃下一只河豚"
    override fun init() {
    }
    @EventHandler
    fun on(e: DayNightEvent) { if(e.nowTime == DayNightHandler.Status.DAY) failed() }
    @EventHandler
    fun on(e: FoodLevelChangeEvent) {
        if(e.entity.uniqueId != disguiser.uniqueId) return
        if(e.item == null) return
        if(e.item!!.type == Material.PUFFERFISH) finished()
    }
}