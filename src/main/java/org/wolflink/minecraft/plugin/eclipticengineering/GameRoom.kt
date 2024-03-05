package org.wolflink.minecraft.plugin.eclipticengineering

import org.bukkit.Location
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.OccupationType
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructure
import org.wolflink.minecraft.plugin.eclipticengineering.structure.foothold.LivingHouse
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureRepository

/**
 * 游戏房间信息
 */
object GameRoom {
    /**
     * 获取据点建筑
     */
    fun getFoothold(): GameStructure? {
        val gameStructure = StructureRepository.findBy { it is LivingHouse }.firstOrNull() ?: return null
        return gameStructure as GameStructure
    }
    /**
     * 获取据点坐标
     */
    fun getFootholdLocation(): Location?
    = getFoothold()?.builder?.buildLocation
    private val occupationPlayers = mutableMapOf<OccupationType,MutableSet<Player>>()
    enum class Result {
        NONE,PIONEER_WIN,DISGUISER_WIN
    }
    var result = Result.NONE

}