package org.wolflink.minecraft.plugin.eclipticengineering.structure.resource

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.GeneratorBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.VirtualResourceType
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.AbilityCondition
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.ItemRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.VirtualRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.resource.log.EvergreenWoodResource
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class GeneratorLog private constructor(blueprint: GeneratorBlueprint, builder: Builder) :
    AbstractGenerator(StructureType.GENERATOR_LOG, blueprint, builder) {
    companion object : StructureCompanion<GeneratorLog>() {
        private const val STRUCTURE_NAME = "优质木场"
        override fun supplier(blueprint: Blueprint, builder: Builder): GeneratorLog {
            return GeneratorLog(blueprint as GeneratorBlueprint, builder)
        }

        override val blueprints = listOf(
            GeneratorBlueprint(
                1,
                STRUCTURE_NAME,
                60,
                3000,
                { structure, buildLocation ->
                    setOf(
                        EvergreenWoodResource(structure, buildLocation.clone().add(0.0, 1.0, -2.0)),
                        EvergreenWoodResource(structure, buildLocation.clone().add(0.0, 2.0, -1.0)),
                        EvergreenWoodResource(structure, buildLocation.clone().add(0.0, 3.0, 0.0))
                    )
                },
                ItemRequirement("需要 16 泥土", ItemStack(Material.DIRT, 16)),
                VirtualRequirement(VirtualResourceType.WOOD, 100),
                VirtualRequirement(VirtualResourceType.METAL, 15),
                AbilityCondition(Ability.BUILDING,3)
            ),
        )
    }
}