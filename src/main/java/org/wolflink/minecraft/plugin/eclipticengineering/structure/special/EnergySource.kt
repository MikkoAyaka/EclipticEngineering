package org.wolflink.minecraft.plugin.eclipticengineering.structure.special

import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ConditionBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.VirtualResourceType
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.AbilityCondition
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.VirtualRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructure
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructureTag
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class EnergySource private constructor(blueprint: Blueprint, builder: Builder) :
    GameStructure(StructureType.ENERGY_SOURCE, blueprint, builder, 1),
    IStructureListener {
    override val customListeners by lazy { listOf(this) }

    companion object : StructureCompanion<EnergySource>() {
        private const val STRUCTURE_NAME = "幽光能量发生场"
        override fun supplier(blueprint: Blueprint, builder: Builder): EnergySource {
            return EnergySource(blueprint, builder)
        }

        override val blueprints = listOf(
            ConditionBlueprint(
                1,
                STRUCTURE_NAME,
                120,
                20000,
                setOf(
                    GameStructureTag.AMOUNT_LIMITED,
                    GameStructureTag.ENERGY_SUPPLY
                ),
                VirtualRequirement("需要 150 石料", VirtualResourceType.STONE, 150),
                VirtualRequirement("需要 60 木材", VirtualResourceType.WOOD, 60),
                AbilityCondition(Ability.BUILDING,3)
            )
        )
    }
}