package org.wolflink.minecraft.plugin.eclipticengineering.structure.special

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ConditionBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.ItemRequirement
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
                5,
                3000,
                ItemRequirement("需要 1 圆石", ItemStack(Material.COBBLESTONE)),
                ItemRequirement("需要 1 铁锭", ItemStack(Material.COBBLESTONE))
            )
        )
        override val clazz: Class<Lighthouse> = Lighthouse::class.java
    }
}