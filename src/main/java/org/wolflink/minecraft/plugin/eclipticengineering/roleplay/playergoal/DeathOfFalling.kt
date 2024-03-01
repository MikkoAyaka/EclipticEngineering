package org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.PlayerDeathEvent
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightEvent
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler

class DeathOfFalling(disguiser: Player): PlayerGoal(disguiser,Difficulty.HARD) {
    override val description = "在其它玩家的视线中摔死"
    @EventHandler
    fun on(e: PlayerDeathEvent) {
        // 死亡的不是内鬼玩家
        if(e.player != disguiser) return
        gamingPlayers.filter { it != e.player }.forEach {
            if(it.hasLineOfSight(e.player)) finished()
        }
    }
    @EventHandler
    fun on(e: DayNightEvent) {  if(e.nowTime == DayNightHandler.Status.DAY) failed() }
    override fun init() {
    }
}