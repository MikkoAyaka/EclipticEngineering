package org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightEvent
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler

class EveryOneHitMe(disguiser: Player): PlayerGoal(disguiser) {
    override val description = "让每个玩家对你造成一次伤害"
    private val hitPlayers = mutableSetOf<Player>()
    @EventHandler
    fun on(e: DayNightEvent) { if(e.nowTime == DayNightHandler.Status.DAY) failed() }
    @EventHandler
    fun on(e: EntityDamageByEntityEvent) {
        val damager = e.damager
        if(e.entity == disguiser && damager is Player && damager !in hitPlayers) {
            hitPlayers.add(damager)
            noticeInProgress()
        }
        // 包括所有其它游戏玩家
        if(hitPlayers.containsAll(gamingPlayers.filter { it != disguiser })) finished()
    }
    override fun init() {
    }

}