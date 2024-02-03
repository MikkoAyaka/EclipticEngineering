package org.wolflink.minecraft.plugin.eclipticengineering.ability

class AbilityTree {
    private val map = mutableMapOf<Ability,Int>()
    init {
        Ability.entries.forEach { map[it] = 0 }
    }
    fun setAbility(abilityType: Ability,abilityLevel: Int) {
        map[abilityType] = abilityLevel
    }
    fun hasAbility(abilityType: Ability,abilityLevel: Int) = map[abilityType]!! >= abilityLevel
}