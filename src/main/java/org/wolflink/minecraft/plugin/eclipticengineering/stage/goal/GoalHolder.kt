package org.wolflink.minecraft.plugin.eclipticengineering.stage.goal

object GoalHolder {
    var nowGoal: Goal = EstablishFoothold
    fun init() {
        nowGoal.into()
    }
}