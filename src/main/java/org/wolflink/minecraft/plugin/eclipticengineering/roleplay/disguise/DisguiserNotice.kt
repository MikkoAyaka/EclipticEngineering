package org.wolflink.minecraft.plugin.eclipticengineering.roleplay.disguise

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.eeLogger
import org.wolflink.minecraft.plugin.eclipticengineering.extension.disguiserPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightEvent
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal.PlayerGoalHolder
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import java.util.Random

object DisguiserNotice: Listener {
    private val random = Random()
    @EventHandler
    fun on(e: DayNightEvent) {
        if(e.nowTime == DayNightHandler.Status.DAY) {
            if(disguiserPlayers.isEmpty()) return
            val randomDisguiser = disguiserPlayers.random()
            val randomDouble = random.nextDouble()
            if(randomDouble <= 0.3) {
                broadcastGoal(randomDisguiser)
            }
            else {
                broadcastActivity(randomDisguiser)
            }
            ActivityRecorder.clearActivity()
        }
    }
    private fun broadcastGoal(disguiser: Player) {
        val playerGoal = PlayerGoalHolder.getPlayerGoal(disguiser) ?: run {
            eeLogger.warning("尝试播报伪装者线索时未能获取到 ${disguiser.name} 的每日目标，这是不应该的。")
            return
        }
        Bukkit.broadcast("$MESSAGE_PREFIX 开拓者们，我们发现了关于伪装者的一些线索，有一位伪装者昨日的目标是：${playerGoal.description}。".toComponent())
    }
    private fun broadcastActivity(disguiser: Player) {
        val activity = ActivityRecorder.getActivity(disguiser) ?: run {
            eeLogger.warning("尝试播报伪装者线索时未能获取到 ${disguiser.name} 的每日活动列表，这是不应该的。")
            return
        }
        if(activity.size < 3) {
            eeLogger.warning("尝试播报伪装者线索时未能获取到足够数量的 ${disguiser.name} 的每日活动列表。")
            return
        }
        Bukkit.broadcast("$MESSAGE_PREFIX 开拓者们，我们发现了伪装者的一些行踪：".toComponent())
        val first = random.nextInt(activity.size - 2)
        val second = random.nextInt(first,activity.size - 1)
        val third = random.nextInt(second,activity.size)
        Bukkit.broadcast("<white>伪装者今天先去过 ${activity[first].blueprint.structureName}，随后前往了 ${activity[second].blueprint.structureName}，接下来是 ${activity[third].blueprint.structureName}".toComponent())
    }
}