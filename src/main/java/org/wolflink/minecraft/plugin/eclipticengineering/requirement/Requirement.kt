package org.wolflink.minecraft.plugin.eclipticengineering.requirement

import org.bukkit.entity.Player

/**
 * 需求(满足需求后需要执行交付)
 */
sealed class Requirement(description: String): Condition(description) {
    /**
     * 交付方法
     */
    abstract fun delivery(player: Player? = null): Boolean
}