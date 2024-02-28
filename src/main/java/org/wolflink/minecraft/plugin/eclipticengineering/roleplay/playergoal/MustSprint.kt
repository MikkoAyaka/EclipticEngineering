package org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerToggleSprintEvent
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightEvent
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler

class MustSprint(disguiser: Player): PlayerGoal(disguiser,Difficulty.HARD) {
    override val description = "一整天必须一直奔跑(允许休息，休息总时长不超过3分钟)"
    // 计时器
    private var count = 0
    @EventHandler
    fun on(e: DayNightEvent) { if(e.nowTime == DayNightHandler.Status.DAY) finished() }
    private suspend fun timerTask() {
        // 每秒进行一次检测
        while (enabled) {
            if(!disguiser.isSprinting) {
                count++
                if(count >= 180) failed()
            }
            delay(1000)
        }
    }
    override fun init() {
        EEngineeringScope.launch { timerTask() }
    }

}