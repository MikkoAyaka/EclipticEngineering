package org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerToggleSprintEvent
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightEvent
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler

class CantSprint(disguiser: Player): PlayerGoal(disguiser,Difficulty.HARD) {
    override val description = "一整天都不可以奔跑"
    @EventHandler
    fun on(e: DayNightEvent) { if(e.nowTime == DayNightHandler.Status.DAY) finished() }
    @EventHandler
    fun on(e: PlayerToggleSprintEvent) {
        if(e.player.uniqueId != disguiser.uniqueId) return
        if(e.isSprinting) failed()
    }
    override fun init() {
    }

}