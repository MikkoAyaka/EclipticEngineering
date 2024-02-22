package org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightEvent
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler

class LowerHead(disguiser: Player): PlayerGoal(disguiser) {
    override val description = "一整天都必须低头(俯角在30~90度)"
    @EventHandler
    fun on(e: DayNightEvent) { if(e.nowTime == DayNightHandler.Status.DAY) finished() }
    override fun init() {
        EEngineeringScope.launch { timerTask() }
    }
    private suspend fun timerTask() {
        while (enabled) {
            if(disguiser.pitch < 30) failed()
            delay(1000)
        }
    }
}