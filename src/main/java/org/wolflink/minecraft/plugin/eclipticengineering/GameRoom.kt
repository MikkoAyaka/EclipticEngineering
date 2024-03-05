package org.wolflink.minecraft.plugin.eclipticengineering

import org.bukkit.Location
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.OccupationType
import org.wolflink.minecraft.plugin.eclipticengineering.structure.foothold.LivingHouse
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureRepository

/**
 * 游戏房间信息
 */
object GameRoom {
    /**
     * 玩家据点坐标
     */
    fun getFootholdLocation(): Location?
    = StructureRepository.findBy { it is LivingHouse }.firstOrNull()?.builder?.buildLocation
    private val occupationPlayers = mutableMapOf<OccupationType,MutableSet<Player>>()
    enum class Result {
        NONE,PIONEER_WIN,DISGUISER_WIN
    }
    var result = Result.NONE

}