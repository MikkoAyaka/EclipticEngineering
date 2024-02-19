package org.wolflink.minecraft.plugin.eclipticengineering.papi

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.extension.abilityTable

/**
 * 玩家能力变量
 * %eea_level_xxxxx%
 * %eea_maxlevel_xxxxx%
 */
object AbilityPapi: PlaceholderExpansion() {
    override fun getIdentifier() = "eea"

    override fun getAuthor() = "MikkoAyaka"

    override fun getVersion() = "1.0.0"

    override fun onRequest(player: OfflinePlayer?, params: String): String {
        if(player == null) return "未找到玩家"
        val args = params.split('_')
        when(args.getOrNull(0)) {
            "level" -> {
                val abilityName = args.getOrNull(1)?.uppercase()
                return try {
                    val ability = Ability.valueOf( abilityName ?: "")
                    "${player.abilityTable.getLevel(ability)}"
                } catch (_: Exception) {
                    "未知天赋"
                }
            }
            "maxlevel" -> {
                val abilityName = args.getOrNull(1)?.uppercase()
                return try {
                    val ability = Ability.valueOf( abilityName ?: "")
                    "${ability.maxLevel}"
                } catch (_: Exception) {
                    "未知天赋"
                }
            }
        }
        return "未知变量"
    }
}