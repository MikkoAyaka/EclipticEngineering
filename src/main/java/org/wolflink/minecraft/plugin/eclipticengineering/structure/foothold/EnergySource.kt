package org.wolflink.minecraft.plugin.eclipticengineering.structure.foothold

import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ConditionBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.GameStructureBlueprint
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

class EnergySource private constructor(blueprint: GameStructureBlueprint, builder: Builder) :
    GameStructure(StructureType.ENERGY_SOURCE, blueprint, builder, 1) {
    override val customListeners = listOf<IStructureListener>()

    companion object : StructureCompanion<EnergySource>() {
        private const val STRUCTURE_NAME = "幽光能量发生场"
        override fun supplier(blueprint: Blueprint, builder: Builder): EnergySource {
            return EnergySource(blueprint as GameStructureBlueprint, builder)
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
                setOf(),
                setOf(VirtualRequirement(VirtualResourceType.STONE, 100),
                    VirtualRequirement(VirtualResourceType.WOOD, 50),
                    AbilityCondition(Ability.BUILDING,3))
            )
        )
    }
}