package org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.PlayerDeathEvent
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightEvent
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent

class CantJump(disguiser: Player): PlayerGoal(disguiser) {
    override val description = "一整天都不可以跳跃"
    @EventHandler
    fun on(e: DayNightEvent) { if(e.nowTime == DayNightHandler.Status.DAY) finished() }
    @EventHandler
    fun on(e: PlayerJumpEvent) {
        if(e.player == disguiser) failed()
    }
    override fun init() {
    }

}