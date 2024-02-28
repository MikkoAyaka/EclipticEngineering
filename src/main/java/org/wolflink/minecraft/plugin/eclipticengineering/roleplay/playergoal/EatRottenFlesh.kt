package org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightEvent
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler

class EatRottenFlesh(disguiser: Player): PlayerGoal(disguiser,Difficulty.EASY) {
    override val description = "一整天都只能吃腐肉"
    @EventHandler
    fun on(e: DayNightEvent) { if(e.nowTime == DayNightHandler.Status.DAY) finished() }
    @EventHandler
    fun on(e: FoodLevelChangeEvent) {
        if(e.entity.uniqueId != disguiser.uniqueId) return
        if(e.item == null) return
        if(e.item!!.type != Material.ROTTEN_FLESH) failed()
    }
    override fun init() {
    }
}