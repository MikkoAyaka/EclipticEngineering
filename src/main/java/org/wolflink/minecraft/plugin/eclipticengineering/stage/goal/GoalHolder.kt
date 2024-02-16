package org.wolflink.minecraft.plugin.eclipticengineering.stage.goal

import org.bukkit.Location
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.TaskBook

object GoalHolder {
    // 当前目标
    var nowGoal: Goal? = null
    // 特殊行动坐标
    var specialLocation: Location? = null
    // 据点坐标
    var footholdLocation: Location? = null
    fun init() {
        nowGoal = EstablishFoothold
        // 发放任务书给随机一名正在游戏中的玩家
        TaskBook.give(gamingPlayers.random())
        nowGoal!!.into()
    }
}