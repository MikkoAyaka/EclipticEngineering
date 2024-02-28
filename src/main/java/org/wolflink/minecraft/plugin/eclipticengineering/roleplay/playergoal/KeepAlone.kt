package org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.extension.isDisguiser
import org.wolflink.minecraft.plugin.eclipticengineering.extension.pioneerPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightEvent
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler

class KeepAlone(disguiser: Player): PlayerGoal(disguiser,Difficulty.NORMAL) {
    override val description get() = "一整天必须独自一人(与其他玩家距离保持在15米之外)"
    // 计时器
    private var count = 0
    @EventHandler
    fun on(e: DayNightEvent) { if(e.nowTime == DayNightHandler.Status.DAY) finished() }
    private suspend fun timerTask() {
        // 每秒进行一次检测
        while (enabled) {
            // 附近存在玩家
            if(pioneerPlayers.map { it.location }.any { it.distance(disguiser.location) <= 15 }) {
                count++
                if(count >= 30) failed()
            }
            delay(1000)
        }
    }
    override fun init() {
        EEngineeringScope.launch { timerTask() }
    }

}