package org.wolflink.minecraft.plugin.eclipticengineering.structure.special

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ConditionBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.VirtualResourceType
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.ItemRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.VirtualRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructure
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructureTag
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class Lighthouse private constructor(
    blueprint: Blueprint, builder: Builder
) : GameStructure(blueprint, builder) {
    override val tags: Set<GameStructureTag> = setOf(
        GameStructureTag.ENERGY_SUPPLY
    )
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
                VirtualRequirement("需要 150 石料", VirtualResourceType.STONE, 150),
                VirtualRequirement("需要 60 木材", VirtualResourceType.WOOD, 60),
                VirtualRequirement("需要 20 金属",VirtualResourceType.METAL,20)
            )
        )
    }
}