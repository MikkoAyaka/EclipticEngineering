package org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightEvent
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler

class SomeoneHitMe(disguiser: Player): PlayerGoal(disguiser,Difficulty.EASY) {
    override val description = "让某个玩家对你造成一次伤害"
    @EventHandler
    fun on(e: DayNightEvent) { if(e.nowTime == DayNightHandler.Status.DAY) failed() }
    @EventHandler
    fun on(e: EntityDamageByEntityEvent) {
        val damager = e.damager
        if(e.entity == disguiser && damager is Player) {
            finished()
        }
    }
    override fun init() {
    }

}