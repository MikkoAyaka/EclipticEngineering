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
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class EnergySource private constructor(blueprint: Blueprint, builder: Builder) : GameStructure(blueprint, builder, 1),
    IStructureListener {
    override val tags = setOf(
        GameStructureTag.AMOUNT_LIMITED,
        GameStructureTag.ENERGY_SUPPLY
    )
    override val customListeners by lazy { listOf(this) }

    companion object : StructureCompanion<EnergySource>() {
        private const val STRUCTURE_NAME = "幽光能量发生场"
        override fun supplier(blueprint: Blueprint, builder: Builder): EnergySource {
            return EnergySource(blueprint, builder)
        }

        override val blueprints = listOf(
            ConditionBlueprint(
                1,
                STRUCTURE_NAME,
                60,
                3000,
                ItemRequirement("需要 1 圆石", ItemStack(Material.COBBLESTONE)),
                ItemRequirement("需要 1 铁锭", ItemStack(Material.IRON_INGOT)),
                VirtualRequirement("需要 15 石料", VirtualResourceType.STONE, 15),
                VirtualRequirement("需要 40 木材", VirtualResourceType.WOOD, 40)
            )
        )
    }
}