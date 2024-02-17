package org.wolflink.minecraft.plugin.eclipticengineering.structure.decoration

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ConditionBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.ItemRequirement
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class Lighthouse private constructor(blueprint: Blueprint, builder: Builder) : Structure(blueprint, builder) {
    override val customListener = null
    companion object : StructureCompanion<Lighthouse>() {
        override fun supplier(blueprint: Blueprint, builder: Builder): Lighthouse {
            return Lighthouse(blueprint,builder)
        }
        override val blueprints = listOf(
            ConditionBlueprint(
                1,
                "灯塔",
                5,
                3000,
                ItemRequirement("需要 1 圆石", ItemStack(Material.COBBLESTONE)),
                ItemRequirement("需要 1 铁锭", ItemStack(Material.COBBLESTONE))
            )
        )
        override val clazz: Class<Lighthouse> = Lighthouse::class.java
    }
}