package org.wolflink.minecraft.plugin.eclipticengineering.structure.generator

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.GeneratorBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.VirtualResourceType
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.AbilityCondition
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.ItemRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.VirtualRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.resource.crop.CropResourceBlock
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class GeneratorCrop private constructor(blueprint: GeneratorBlueprint, builder: Builder) :
    AbstractGenerator(StructureType.GENERATOR_CROP, blueprint, builder) {
    companion object : StructureCompanion<GeneratorCrop>() {
        private const val STRUCTURE_NAME = "精培农场"
        override fun supplier(blueprint: Blueprint, builder: Builder): GeneratorCrop {
            return GeneratorCrop(blueprint as GeneratorBlueprint, builder)
        }

        override val blueprints = listOf(
            GeneratorBlueprint(
                1,
                STRUCTURE_NAME,
                60,
                3000,
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
                ItemRequirement("需要 96 泥土", ItemStack(Material.DIRT, 96)),
                VirtualRequirement("需要 60 木材", VirtualResourceType.WOOD, 60),
                VirtualRequirement("需要 15 金属", VirtualResourceType.METAL, 15),
                AbilityCondition(Ability.BUILDING,3)
            )
        )
    }
}