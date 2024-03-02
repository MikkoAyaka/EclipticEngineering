package org.wolflink.minecraft.plugin.eclipticengineering

import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.OccupationType

/**
 * 游戏房间信息
 */
object GameRoom {
    private val occupationPlayers = mutableMapOf<OccupationType,MutableSet<Player>>()
    enum class Result {
        NONE,PIONEER_WIN,DISGUISER_WIN
    }
    var result = Result.NONE

}