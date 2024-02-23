package org.wolflink.minecraft.plugin.eclipticengineering.requirement

import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.extension.abilityTable

class AbilityCondition(private val abilityType: Ability, private val level: Int = 1):Condition("需要 ${abilityType.displayName} 达到等级 $level") {
    override fun isSatisfy(player: Player?): Boolean {
        return player?.abilityTable?.hasAbility(abilityType,level) == true
    }
}