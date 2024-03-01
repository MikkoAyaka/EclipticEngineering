package org.wolflink.minecraft.plugin.eclipticengineering.ability

import org.bukkit.Bukkit
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.OccupationType
import org.wolflink.minecraft.plugin.eclipticengineering.extension.abilityTable
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers

/**
 * 持续为玩家应用职业效果
 */
object OccupationApplier {
    fun init() {
        EclipticEngineering.runTaskTimer(20 * 3,20 * 3) {
            // 战士
            gamingPlayers.filter { it.abilityTable.occupationType == OccupationType.WARRIOR }.forEach {
                it.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION,20 * 5,0,false,false))
                it.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE,20 * 5,0,false,false))
            }
            // 建筑师
            gamingPlayers.filter { it.abilityTable.occupationType == OccupationType.BUILDER }.forEach {
                it.addPotionEffect(PotionEffect(PotionEffectType.GLOWING,20 * 5,0,false,false))
            }
            // 打工仔
            gamingPlayers.filter { it.abilityTable.occupationType == OccupationType.MINER }.forEach {
                it.addPotionEffect(PotionEffect(PotionEffectType.FAST_DIGGING,20 * 5,0,false,false))
                it.addPotionEffect(PotionEffect(PotionEffectType.SPEED,20 * 5,0,false,false))
            }
            // 铁匠
            gamingPlayers.filter { it.abilityTable.occupationType == OccupationType.BLACKSMITH }.forEach {
                it.addPotionEffect(PotionEffect(PotionEffectType.HEALTH_BOOST,20 * 5,2,false,false))
            }
        }
    }
}