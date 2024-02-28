package org.wolflink.minecraft.plugin.eclipticengineering.dictionary

import org.bukkit.Color
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability

enum class OccupationType(val displayName: String,val color: Color,val abilityData: Map<Ability,Int>) {
    BUILDER("超高校级包工头",Color.fromRGB(0,191,255),mapOf(
        Ability.BUILDING to 3,
        Ability.ENGINEERING to 1,
        Ability.SMELTING to 1,
        Ability.MINING to 1,
        Ability.LOGGING to 1,
    )),
    WARRIOR("热血沸腾铁罐子",Color.fromRGB(255,99,71), mapOf(
        Ability.BUILDING to 2,
        Ability.ENGINEERING to 1,
        Ability.COMBAT to 3,
        Ability.LOGGING to 1
    )),
    MINER("一般路过打灰仔",Color.fromRGB(240,255,240),mapOf(
        Ability.FARMING to 2,
        Ability.MINING to 2,
        Ability.LOGGING to 2,
        Ability.COMBAT to 1,
    )),
    BLACKSMITH("千锤百炼大胡子", Color.fromRGB(238,238,209), mapOf(
        Ability.ENGINEERING to 2,
        Ability.SMELTING to 2,
        Ability.COMBAT to 3,
        Ability.BUILDING to 1,
    ));
}