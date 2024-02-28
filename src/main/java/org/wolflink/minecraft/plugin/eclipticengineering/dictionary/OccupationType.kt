package org.wolflink.minecraft.plugin.eclipticengineering.dictionary

import org.bukkit.Color
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.ability.AbilityTable
import org.wolflink.minecraft.plugin.eclipticengineering.extension.abilityTable

enum class OccupationType(val displayName: String,val color: Color,val abilityData: Map<Ability,Int>) {
    BUILDER("建筑师",Color.fromRGB(0,191,255),mapOf(
        Ability.BUILDING to 3,
        Ability.ENGINEERING to 1,
        Ability.SMELTING to 1,
        Ability.MINING to 1,
        Ability.LOGGING to 1,
        Ability.COMBAT to 1,
    )),
    WARRIOR("战士",Color.fromRGB(255,99,71), mapOf(
        Ability.BUILDING to 2,
        Ability.ENGINEERING to 1,
        Ability.COMBAT to 3,
        Ability.MINING to 2
    )),
    MINER("工人",Color.fromRGB(240,255,240),mapOf(
        Ability.FARMING to 2,
        Ability.MINING to 2,
        Ability.LOGGING to 2,
        Ability.COMBAT to 2,
    )),
    BLACKSMITH("铁匠", Color.fromRGB(238,238,209), mapOf(
        Ability.ENGINEERING to 2,
        Ability.SMELTING to 2,
        Ability.MINING to 2,
        Ability.COMBAT to 2,
    ));
}