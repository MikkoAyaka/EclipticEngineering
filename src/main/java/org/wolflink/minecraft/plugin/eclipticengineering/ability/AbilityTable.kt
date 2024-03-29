package org.wolflink.minecraft.plugin.eclipticengineering.ability

import org.bukkit.Bukkit
import org.bukkit.Sound
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.extension.isDisguiser
import org.wolflink.minecraft.plugin.eclipticengineering.extension.toRoma
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toHexFormat
import java.util.EnumMap
import java.util.UUID

/**
 * 存放关于玩家的能力数据
 */
class AbilityTable(val ownerUuid: UUID) {
    val totalPoints = 9
    private val map = EnumMap<Ability,Int>(Ability::class.java)
    init {
        Ability.entries.forEach { map[it] = 0 }
    }
    fun usedPoints() = map.values.reduce(Int::plus)
    fun usablePoints() = totalPoints - usedPoints()
    /**
     * 判断玩家是否有剩余能力点数
     */
    fun hasPoint() = map.values.reduce(Int::plus) < totalPoints
    fun setAbility(abilityType: Ability,abilityLevel: Int) {
        map[abilityType] = abilityLevel
    }
    fun getLevel(abilityType: Ability) = map[abilityType]!!
    fun addAbility(abilityType: Ability) {
        map[abilityType] = map.getOrDefault(abilityType,0) + 1
    }

    /**
     * 检查玩家是否拥有对应的能力，如果缺失会发送信息提示玩家
     */
    fun checkAbilityWithNotice(abilityType: Ability,abilityLevel: Int = 1): Boolean {
        val result = hasAbility(abilityType,abilityLevel)
        if(!result) {
            val player = Bukkit.getPlayer(ownerUuid) ?: return false
            if(!player.isOnline) return false
            player.playSound(player, Sound.ENTITY_VILLAGER_NO,1f,1f)
            player.sendActionBar("$MESSAGE_PREFIX 需要能力 ${abilityType.color.toHexFormat()}${abilityType.displayName} ${abilityLevel.toRoma()}".toComponent())
        }
        return result
    }
    fun hasAbility(abilityType: Ability,abilityLevel: Int = 1) = map[abilityType]!! >= abilityLevel || Bukkit.getPlayer(ownerUuid)?.isDisguiser() == true
}