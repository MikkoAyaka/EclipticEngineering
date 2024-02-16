package org.wolflink.minecraft.plugin.eclipticengineering.stage.goal

data class GoalCondition(val description: String,val predicate: ()->Boolean) {
    fun check() = predicate.invoke()
}