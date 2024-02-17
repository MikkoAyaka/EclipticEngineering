package org.wolflink.minecraft.plugin.eclipticengineering.requirement

import org.bukkit.entity.Player

class FunctionalCondition(description: String, private val satisfyFunc:(Player?)->Boolean): Condition(description) {
    override fun isSatisfy(player: Player?): Boolean {
        return satisfyFunc.invoke(player)
    }
}