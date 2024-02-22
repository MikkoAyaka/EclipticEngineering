package org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightEvent
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler

class HitEveryOne(disguiser: Player): PlayerGoal(disguiser) {
    private val hitPlayers = mutableSetOf<Player>()
    override val description = "对所有玩家造成一次伤害"
    @EventHandler
    fun on(e: DayNightEvent) { if(e.nowTime == DayNightHandler.Status.DAY) failed() }
    @EventHandler
    fun on(e: EntityDamageByEntityEvent) {
        val entity = e.entity
        if(e.damager == disguiser && entity is Player && entity !in hitPlayers) {
            hitPlayers.add(entity)
            noticeInProgress()
        }
        // 包括所有其它游戏玩家
        if(hitPlayers.containsAll(gamingPlayers.filter { it != disguiser })) finished()
    }
    override fun init() {
    }

}