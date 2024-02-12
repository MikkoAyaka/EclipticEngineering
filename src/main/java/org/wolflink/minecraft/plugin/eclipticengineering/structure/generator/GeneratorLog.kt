package org.wolflink.minecraft.plugin.eclipticengineering.structure.generator

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.GeneratorBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.resource.log.EvergreenWood
import org.wolflink.minecraft.plugin.eclipticengineering.resource.ore.OreResourceBlock
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class GeneratorLog private constructor(blueprint: GeneratorBlueprint, builder: Builder) :
    AbstractGenerator(blueprint, builder) {
    companion object : StructureCompanion<GeneratorLog>(){
        private const val STRUCTURE_NAME = "优质木场"
        override val clazz: Class<GeneratorLog> = GeneratorLog::class.java
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
                ItemStack(Material.COBBLESTONE, 128),
                ItemStack(Material.IRON_INGOT, 16),
                ItemStack(Material.GOLD_INGOT, 8)
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
                ItemStack(Material.COBBLESTONE, 128),
                ItemStack(Material.IRON_INGOT, 16),
                ItemStack(Material.GOLD_INGOT, 8)
            )
        )
    }
}