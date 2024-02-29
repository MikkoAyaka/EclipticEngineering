package org.wolflink.minecraft.plugin.eclipticengineering.ability

import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.OccupationType
import org.wolflink.minecraft.plugin.eclipticengineering.extension.abilityTable
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers

/**
 * 持续为玩家应用职业效果
 */
object OccupationApplier {
    fun init() {
        gamingPlayers.filter { it.abilityTable.occupationType == OccupationType.WARRIOR }.forEach {
            it.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION,20 * 5,0,false,false))
            it.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE,20 * 5,0,false,false))
        }
    }
}