package org.wolflink.minecraft.plugin.eclipticengineering.ability

/**
 * 存放关于玩家的能力数据
 */
class AbilityTable {
    private val totalPoints = 6
    private val map = mutableMapOf<Ability,Int>()
    init {
        Ability.entries.forEach { map[it] = 0 }
    }
    /**
     * 判断玩家是否有剩余能力点数
     */
    fun hasPoint() = map.values.reduce(Int::plus) < totalPoints
    fun setAbility(abilityType: Ability,abilityLevel: Int) {
        map[abilityType] = abilityLevel
    }
    fun hasAbility(abilityType: Ability,abilityLevel: Int) = map[abilityType]!! >= abilityLevel
}