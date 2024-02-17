package org.wolflink.minecraft.plugin.eclipticengineering.requirement

import org.bukkit.entity.Player

/**
 * 条件(满足条件后无需进行交付)
 */
abstract class Condition(val description: String) {
    abstract fun isSatisfy(player: Player? = null): Boolean
}