package org.wolflink.minecraft.plugin.eclipticengineering.stage.goal

import org.bukkit.Location

object GoalHolder {
    // 当前目标
    var nowGoal: Goal = EstablishFoothold
    // 特殊行动坐标
    var specialLocation: Location? = null
    // 据点坐标
    var footholdLocation: Location? = null
    fun init() {
        nowGoal.into()
    }
}