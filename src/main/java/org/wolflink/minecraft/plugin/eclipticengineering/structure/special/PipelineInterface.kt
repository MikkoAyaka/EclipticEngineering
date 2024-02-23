package org.wolflink.minecraft.plugin.eclipticengineering.structure.special

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ConditionBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.VirtualResourceType
import org.wolflink.minecraft.plugin.eclipticengineering.extension.hasConnection
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.AbilityCondition
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.ItemRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.VirtualRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.EnergyRequiredListener
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructure
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructureTag
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class PipelineInterface private constructor(blueprint: Blueprint, builder: Builder) :
    GameStructure(StructureType.PIPELINE_INTERFACE,blueprint, builder) {
    override val customListeners = listOf(EnergyRequiredListener(this))

    /**
     * 检查两个管道结构之间是否存在通路(被指定方块连接)
     */
    fun hasConnection(another: PipelineInterface): Boolean {
        val thisLoc = builder.buildLocation
        val anotherLoc = another.builder.buildLocation
        return thisLoc.block.hasConnection(anotherLoc.block, Material.SCULK)
    }

    companion object : StructureCompanion<PipelineInterface>() {
        override fun supplier(blueprint: Blueprint, builder: Builder): PipelineInterface {
            return PipelineInterface(blueprint, builder)
        }

        override val blueprints = listOf(
            ConditionBlueprint(
                1,
                "管道接口",
                60,
                3000,
                setOf(
                    GameStructureTag.ENERGY_REQUIRED
                ),
                VirtualRequirement("需要 15 石料", VirtualResourceType.STONE, 15),
                VirtualRequirement("需要 30 金属", VirtualResourceType.METAL,30),
                AbilityCondition(Ability.BUILDING,3)
            )
        )
    }
}