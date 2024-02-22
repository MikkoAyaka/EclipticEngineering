package org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureRepository
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure

class StayAtStructure(disguiser: Player): PlayerGoal(disguiser) {
    private lateinit var structure: Structure
    private var counter = 240
    override val description by lazy { "待在 ${structure.blueprint.structureName} 类型的建筑中持续4分钟。" }
    override fun init() {
        structure = StructureRepository.findBy { it.available }.random()
        EEngineeringScope.launch { timerTask() }
    }
    private suspend fun timerTask() {
        while (available) {
            if(disguiser.location in structure.builder.zone) {
                counter--
                noticeInProgress()
                if(counter <= 0) finished()
            }
            else counter = 240
            delay(1000)
        }
    }
}