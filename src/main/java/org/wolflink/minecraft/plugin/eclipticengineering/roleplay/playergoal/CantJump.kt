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
import org.wolflink.minecraft.plugin.eclipticengineering.utils.RandomAPI
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import java.util.Random

class CantJump(disguiser: Player): PlayerGoal(disguiser,Difficulty.NORMAL) {
    private val jumpCount = RandomAPI.nextInt(2,6)
    private var count = 0
    override val description = "一天内最多只能跳跃 $jumpCount 次"
    @EventHandler
    fun on(e: DayNightEvent) { if(e.nowTime == DayNightHandler.Status.DAY) finished() }
    @EventHandler
    fun on(e: PlayerJumpEvent) {
        if(e.player == disguiser) {
            count++
            if(count >= jumpCount) failed()
        }
    }
    override fun init() {
    }

}