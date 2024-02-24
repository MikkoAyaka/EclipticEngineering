package org.wolflink.minecraft.plugin.eclipticengineering.structure.foothold

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

class Lighthouse private constructor(
    blueprint: Blueprint, builder: Builder
) : GameStructure(StructureType.LIGHT_HOUSE, blueprint, builder) {
    override val customListeners = listOf<IStructureListener>()

    companion object : StructureCompanion<Lighthouse>() {
        override fun supplier(blueprint: Blueprint, builder: Builder): Lighthouse {
            return Lighthouse(blueprint, builder)
        }

        override val blueprints = listOf(
            ConditionBlueprint(
                1,
                "幽光灯塔",
                90,
                8000,
                setOf(
                    GameStructureTag.ENERGY_SUPPLY
                ),
                VirtualRequirement(VirtualResourceType.STONE, 150),
                VirtualRequirement(VirtualResourceType.WOOD, 60),
                VirtualRequirement(VirtualResourceType.METAL, 20),
                AbilityCondition(Ability.BUILDING,3)
            )
        )
    }
}