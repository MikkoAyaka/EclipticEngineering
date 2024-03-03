package org.wolflink.minecraft.plugin.eclipticengineering.structure.foothold

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ConditionBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.GameStructureBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.VirtualResourceType
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.AbilityCondition
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.VirtualRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.MeetingHandler
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructure
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructureTag
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureCompletedEvent
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureDestroyedEvent
import org.wolflink.minecraft.plugin.eclipticstructure.extension.register
import org.wolflink.minecraft.plugin.eclipticstructure.extension.unregister
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class MeetingPlace private constructor(blueprint: GameStructureBlueprint, builder: Builder) :
    GameStructure(StructureType.MEETING_PLACE, blueprint, builder, 1), IStructureListener, Listener {
    override val customListeners = listOf<IStructureListener>(this)

    companion object : StructureCompanion<MeetingPlace>() {
        override val blueprints = listOf(
            ConditionBlueprint(
                1,
                "会议大厅",
                45,
                6000,
                setOf(
                    GameStructureTag.AMOUNT_LIMITED
                ),
                setOf(VirtualRequirement(VirtualResourceType.WOOD, 10)),
                setOf(VirtualRequirement(VirtualResourceType.STONE, 30),
                    VirtualRequirement(VirtualResourceType.WOOD, 60),
                    AbilityCondition(Ability.BUILDING,3))
            )
        )

        override fun supplier(blueprint: Blueprint, builder: Builder): MeetingPlace {
            return MeetingPlace(blueprint as GameStructureBlueprint, builder)
        }
    }

    override fun completed(e: StructureCompletedEvent) {
        this.register(EclipticEngineering.instance)
    }

    override fun destroyed(e: StructureDestroyedEvent) {
        this.unregister()
    }

    @EventHandler
    fun on(e: PlayerInteractEvent) {
        if (!available) return
        val block = e.clickedBlock ?: return
        if (block.type == Material.BELL && block.location in zone) {
            MeetingHandler.startMeeting(e.player)
        }
    }
}