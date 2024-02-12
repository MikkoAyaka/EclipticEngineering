package org.wolflink.minecraft.plugin.eclipticengineering.structure.generator

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.GeneratorBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.resource.crop.CropResourceBlock
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class GeneratorCrop private constructor(blueprint: GeneratorBlueprint, builder: Builder) :
    AbstractGenerator(blueprint, builder) {
    companion object : StructureCompanion<GeneratorCrop>(){
        private const val STRUCTURE_NAME = "精培农场"
        override val clazz: Class<GeneratorCrop> = GeneratorCrop::class.java
        override val blueprints = listOf(
            GeneratorBlueprint(
                1,
                STRUCTURE_NAME,
                5,
                1000,
                { structure, buildLocation ->
                    setOf(
                        CropResourceBlock(structure, buildLocation.clone().add(-1.0, 1.0, 1.0)),
                        CropResourceBlock(structure, buildLocation.clone().add(-1.0, 1.0, 0.0)),
                        CropResourceBlock(structure, buildLocation.clone().add(-1.0, 1.0, -1.0)),
                        CropResourceBlock(structure, buildLocation.clone().add(0.0, 1.0, 1.0)),
                        CropResourceBlock(structure, buildLocation.clone().add(0.0, 1.0, -1.0)),
                        CropResourceBlock(structure, buildLocation.clone().add(1.0, 1.0, 1.0)),
                        CropResourceBlock(structure, buildLocation.clone().add(1.0, 1.0, 0.0)),
                        CropResourceBlock(structure, buildLocation.clone().add(1.0, 1.0, -1.0)),
                    )
                },
                ItemStack(Material.COBBLESTONE, 128),
                ItemStack(Material.IRON_INGOT, 16),
                ItemStack(Material.GOLD_INGOT, 8)
            )
        )
    }
}