package org.wolflink.minecraft.plugin.eclipticengineering.structure.special

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ConditionBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.ItemRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.MeetingHandler
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructure
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructureTag
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureAvailableEvent
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureCompletedEvent
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureDestroyedEvent
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureUnavailableEvent
import org.wolflink.minecraft.plugin.eclipticstructure.extension.register
import org.wolflink.minecraft.plugin.eclipticstructure.extension.unregister
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class MeetingPlace private constructor(blueprint: Blueprint, builder: Builder) : GameStructure(blueprint, builder,1),IStructureListener,Listener {
    override val tags = setOf(
        GameStructureTag.AMOUNT_LIMITED
    )
    override val customListeners = listOf<IStructureListener>(this)

    companion object : StructureCompanion<MeetingPlace>() {
        override val blueprints = listOf(
            ConditionBlueprint(
                1,
                "会议大厅",
                5,
                3000,
                ItemRequirement("需要 1 圆石", ItemStack(Material.COBBLESTONE)),
                ItemRequirement("需要 1 铁锭", ItemStack(Material.COBBLESTONE))
            )
        )
        override fun supplier(blueprint: Blueprint, builder: Builder): MeetingPlace {
            return MeetingPlace(blueprint, builder)
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
        if(!available) return
        val block = e.clickedBlock ?: return
        if(block.type == Material.BELL && block.location in zone) {
            MeetingHandler.startMeeting(e.player)
        }
    }
}