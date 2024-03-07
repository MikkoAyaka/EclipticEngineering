package org.wolflink.minecraft.plugin.eclipticengineering.roleplay.disguise

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.extension.disguiserPlayers
import org.wolflink.minecraft.plugin.eclipticstructure.coroutine.SingletonJob
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureRepository
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureZoneRelationRepository
import org.wolflink.minecraft.plugin.eclipticstructure.repository.ZoneRepository
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure

/**
 * 内鬼活动记录者
 */
object ActivityRecorder {
    private var available = false
    // 内鬼在建筑区域活动记录
    private val disguisersDailyActivityMap = mutableMapOf<Player,MutableList<Structure>>()
    fun clearActivity() {
        disguisersDailyActivityMap.clear()
    }
    fun getActivity(disguiser: Player) = disguisersDailyActivityMap[disguiser]

    private val startTask = SingletonJob {
        while (available) {
            delay(1000)
            disguiserPlayers.forEach {
                val structure = ZoneRepository.findByLocation(it.location)
                    .firstNotNullOfOrNull { zone -> StructureZoneRelationRepository.find1(zone) } ?: return@forEach
                val list = disguisersDailyActivityMap.getOrPut(it){ mutableListOf() }
                list.add(structure)
            }
        }
    }
    fun enable() {
        available = true
        startTask.tryLaunch(EEngineeringScope)
    }
    fun disable() {
        available = false
    }
}