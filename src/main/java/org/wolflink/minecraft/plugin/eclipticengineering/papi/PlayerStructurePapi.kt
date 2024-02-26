package org.wolflink.minecraft.plugin.eclipticengineering.papi

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import org.wolflink.minecraft.plugin.eclipticstructure.extension.parsePapi
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure

/**
 * 玩家当前正在查看的建筑详情变量
 * 这个变量会被重定向到 GameStructurePapi
 * %eeps....% => %eegs_xxxxx_....%
 */
object PlayerStructurePapi: PlaceholderExpansion() {
    override fun getIdentifier() = "eeps"

    override fun getAuthor() = "MikkoAyaka"

    override fun getVersion() = "1.0.0"
    // 玩家当前正在查看的建筑 ID
    private val relationMap = mutableMapOf<OfflinePlayer,Int>()
    fun updateRelation(player: OfflinePlayer,structure: Structure) {
        relationMap[player] = structure.id
    }
    override fun onRequest(player: OfflinePlayer?, params: String): String {
        val structureId = relationMap[player] ?: return "未绑定的建筑结构"
        if(player == null) return "不存在的玩家"
        return "%eegs_${structureId}_${params}%".parsePapi(player)
    }
}