package org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.extension.isDisguiser
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightEvent
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler

class FollowSomeone(disguiser: Player): PlayerGoal(disguiser,Difficulty.NORMAL) {
    private lateinit var another: Player
    override val description get() = "一整天必须一直跟踪 ${another.name}(距离保持在15米之内)"
    // 计时器
    private var count = 0
    @EventHandler
    fun on(e: DayNightEvent) { if(e.nowTime == DayNightHandler.Status.DAY) finished() }
    private suspend fun timerTask() {
        // 每秒进行一次检测
        while (enabled) {
            if(disguiser.location.distance(another.location) > 15) {
                count++
                if(count >= 30) failed()
            }
            delay(1000)
        }
    }
    override fun init() {
        another = gamingPlayers.filter { !it.isDisguiser() }.random()
        EEngineeringScope.launch { timerTask() }
    }

}