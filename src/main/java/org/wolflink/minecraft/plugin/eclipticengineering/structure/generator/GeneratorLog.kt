package org.wolflink.minecraft.plugin.eclipticengineering.structure.generator

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.GeneratorBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.ItemRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.resource.log.EvergreenWood
import org.wolflink.minecraft.plugin.eclipticengineering.resource.ore.OreResourceBlock
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class GeneratorLog private constructor(blueprint: GeneratorBlueprint, builder: Builder) :
    AbstractGenerator(blueprint, builder) {
    companion object : StructureCompanion<GeneratorLog>(){
        private const val STRUCTURE_NAME = "优质木场"
        override fun supplier(blueprint: Blueprint, builder: Builder): GeneratorLog {
            return GeneratorLog(blueprint as GeneratorBlueprint,builder)
        }
        override val blueprints = listOf(
            GeneratorBlueprint(
                1,
                STRUCTURE_NAME,
                5,
                1000,
                { structure, buildLocation ->
                    setOf(
                        EvergreenWood(structure, buildLocation.clone().add(0.0, 1.0, -2.0)),
                        EvergreenWood(structure, buildLocation.clone().add(0.0, 2.0, -1.0)),
                        EvergreenWood(structure, buildLocation.clone().add(0.0, 3.0, 0.0))
                    )
                },
                ItemRequirement("需要 1 圆石", ItemStack(Material.COBBLESTONE)),
                ItemRequirement("需要 1 铁锭", ItemStack(Material.COBBLESTONE))
            ),
            GeneratorBlueprint(
                2,
                STRUCTURE_NAME,
                10,
                1750,
                { structure, buildLocation ->
                    setOf(
                        OreResourceBlock(structure, buildLocation.clone().add(-1.0, 1.0, 0.0)),
                        OreResourceBlock(structure, buildLocation.clone().add(1.0, 0.0, -1.0)),
                        OreResourceBlock(structure, buildLocation.clone().add(-2.0, 2.0, 0.0)),
                        OreResourceBlock(structure, buildLocation.clone().add(-1.0, 3.0, -1.0))
                    )
                },
                ItemRequirement("需要 1 圆石", ItemStack(Material.COBBLESTONE)),
                ItemRequirement("需要 1 铁锭", ItemStack(Material.COBBLESTONE))
            )
        )
    }
}