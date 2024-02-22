package org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightEvent
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler
import org.wolflink.minecraft.plugin.eclipticengineering.structure.decoration.SpinTieMeme
import org.wolflink.minecraft.plugin.eclipticstructure.event.builder.BuilderCompletedEvent

class PlaceMeme(disguiser: Player): PlayerGoal(disguiser) {
    private val structureType = StructureType.SPIN_TIE_MEME
    override val description = "在日落之前在能源中心附近建造一个 ${structureType.displayName}"
    override fun init() {

    }
    @EventHandler
    fun on(e: BuilderCompletedEvent) {
        if(e.structure is SpinTieMeme) finished()
    }
    @EventHandler
    fun on(e: DayNightEvent) { if(e.nowTime == DayNightHandler.Status.NIGHT) failed() }

}