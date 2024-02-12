package org.wolflink.minecraft.plugin.eclipticengineering.structure.decoration

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class Lighthouse private constructor(blueprint: Blueprint,builder: Builder) : Structure(blueprint, builder) {
    override val customListener = null
    companion object : StructureCompanion<Lighthouse>() {
        override val blueprints = listOf(
            Blueprint(
                1,
                "灯塔",
                5,
                3000,
                ItemStack(Material.COBBLESTONE, 128),
                ItemStack(Material.IRON_INGOT, 16),
                ItemStack(Material.GOLD_INGOT, 8)
            )
        )
        override val clazz: Class<Lighthouse> = Lighthouse::class.java
    }
}