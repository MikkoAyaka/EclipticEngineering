package org.wolflink.minecraft.plugin.eclipticengineering.structure.resource

import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ConditionBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.GameStructureBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.VirtualResourceType
import org.wolflink.minecraft.plugin.eclipticengineering.forge.ForgeHandler
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.AbilityCondition
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.SpecialItemRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.VirtualRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.SpecialIron
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructure
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructureTag
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class RefiningFurnace private constructor(blueprint: GameStructureBlueprint, builder: Builder) :
    GameStructure(StructureType.REFINING_FURNACE, blueprint, builder, 1), IStructureListener {
    override val customListeners: List<IStructureListener> = listOf(this)
    override fun onInteract(e: PlayerInteractEvent) {
        // 玩家交互的是木制按钮
        if(e.clickedBlock?.type != Material.OAK_BUTTON) return
        ForgeHandler.openForgeMenu(e.player)
    }
    companion object : StructureCompanion<RefiningFurnace>() {
        private const val STRUCTURE_NAME = "精炼熔炉"
        override fun supplier(blueprint: Blueprint, builder: Builder): RefiningFurnace {
            return RefiningFurnace(blueprint as GameStructureBlueprint, builder)
        }


        override val blueprints = listOf(
            ConditionBlueprint(
                1,
                STRUCTURE_NAME,
                30,
                6000,
                true,
                // tag
                setOf(
                    GameStructureTag.AMOUNT_LIMITED,
                    GameStructureTag.SPECIAL_RESOURCE_GENERATOR
                ),
                // repair
                setOf(
                    VirtualRequirement(VirtualResourceType.STONE, 5),
                    VirtualRequirement(VirtualResourceType.METAL, 5),
                ),
                // build
                setOf(
                    VirtualRequirement(VirtualResourceType.STONE, 40),
                    VirtualRequirement(VirtualResourceType.METAL, 40),
                    SpecialItemRequirement(SpecialIron,5),
                    AbilityCondition(Ability.BUILDING,3)
                )
            )
        )
    }
}