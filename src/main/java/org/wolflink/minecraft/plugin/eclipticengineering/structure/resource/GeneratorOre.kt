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
import org.wolflink.minecraft.plugin.eclipticengineering.resource.ore.OreResourceBlock
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class GeneratorOre private constructor(blueprint: GeneratorBlueprint, builder: Builder) :
    AbstractGenerator(StructureType.GENERATOR_ORE, blueprint, builder) {
    companion object : StructureCompanion<GeneratorOre>() {
        private const val STRUCTURE_NAME = "精炼矿场"
        override fun supplier(blueprint: Blueprint, builder: Builder): GeneratorOre {
            return GeneratorOre(blueprint as GeneratorBlueprint, builder)
        }

        override val blueprints = listOf(
            GeneratorBlueprint(
                1,
                STRUCTURE_NAME,
                30,
                3000,
                { structure, buildLocation ->
                    setOf(
                        OreResourceBlock(structure, buildLocation.clone().add(-1.0, 1.0, 0.0)),
                        OreResourceBlock(structure, buildLocation.clone().add(1.0, 0.0, -1.0)),
                        OreResourceBlock(structure, buildLocation.clone().add(-2.0, 2.0, 0.0)),
                        OreResourceBlock(structure, buildLocation.clone().add(-1.0, 3.0, -1.0))
                    )
                },
                setOf(VirtualRequirement(VirtualResourceType.WOOD, 20)),
                setOf(ItemRequirement("需要 1 熔岩桶", ItemStack(Material.LAVA_BUCKET)),
                    VirtualRequirement(VirtualResourceType.WOOD, 120),
                    VirtualRequirement(VirtualResourceType.METAL, 30),
                    AbilityCondition(Ability.BUILDING,3))
            )
        )
    }
}