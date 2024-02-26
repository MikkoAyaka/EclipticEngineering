package org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightEvent
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler
import org.wolflink.minecraft.plugin.eclipticstructure.event.builder.BuilderCompletedEvent

class PreventBuildStructure(disguiser: Player): PlayerGoal(disguiser,Difficulty.HARD) {
    private lateinit var structureType: StructureType
    override val description by lazy { "阻止任何人在今天之内建造 ${structureType.displayName} 类型的建筑结构。" }
    override fun init() {
        structureType = setOf(
            StructureType.LARGE_CAMPFIRE,
            StructureType.LOGGING_PLACE,
            StructureType.FARMING_PLACE,
            StructureType.MINING_PLACE,
            StructureType.GENERATOR_ORE,
            StructureType.GENERATOR_LOG,
            StructureType.GENERATOR_CROP,
        ).random()
    }
    @EventHandler
    fun on(e: BuilderCompletedEvent) {
        if(e.structure::class.java == structureType.clazz) {
            failed()
        }
    }
    @EventHandler
    fun on(e: DayNightEvent) { if(e.nowTime == DayNightHandler.Status.DAY)  finished() }
}