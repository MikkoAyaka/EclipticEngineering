package org.wolflink.minecraft.plugin.eclipticengineering.structure.decoration

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

class SpinTieMeme private constructor(
    blueprint: Blueprint, builder: Builder
) : GameStructure(blueprint, builder) {
    override val tags: Set<GameStructureTag> = setOf(
    )
    override val customListeners = listOf<IStructureListener>()

    companion object : StructureCompanion<SpinTieMeme>() {
        override fun supplier(blueprint: Blueprint, builder: Builder): SpinTieMeme {
            return SpinTieMeme(blueprint, builder)
        }

        override val blueprints = listOf(
            ConditionBlueprint(
                1,
                "非常好建筑，使我的领带旋转",
                60,
                5000,
                ItemRequirement("需要 16 腐肉", ItemStack(Material.ROTTEN_FLESH,16)),
                VirtualRequirement("需要 196 石料", VirtualResourceType.STONE, 196)
            )
        )
    }
}