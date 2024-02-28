package org.wolflink.minecraft.plugin.eclipticengineering.roleplay

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.extension.disguiserPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal.PlayerGoalHolder
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent

object DisguiserNotice: Listener {
    @EventHandler
    fun on(e: DayNightEvent) {
        if(e.nowTime == DayNightHandler.Status.DAY) {
            if(disguiserPlayers.isEmpty()) return
            val randomDisguiser = disguiserPlayers.random()
            val playerGoal = PlayerGoalHolder.getPlayerGoal(randomDisguiser) ?: return
            Bukkit.broadcast("$MESSAGE_PREFIX 开拓者们，我们发现了关于伪装者的一些线索，有一位伪装者昨日的目标是：${playerGoal.description}。".toComponent())
        }
    }
}