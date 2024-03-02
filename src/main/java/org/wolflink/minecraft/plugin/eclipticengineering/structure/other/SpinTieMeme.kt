package org.wolflink.minecraft.plugin.eclipticengineering.structure.other

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ConditionBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.GameStructureBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.VirtualResourceType
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.ItemRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.VirtualRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructure
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class SpinTieMeme private constructor(
    blueprint: GameStructureBlueprint, builder: Builder
) : GameStructure(StructureType.SPIN_TIE_MEME, blueprint, builder) {
    override val customListeners = listOf<IStructureListener>()

    companion object : StructureCompanion<SpinTieMeme>() {
        override fun supplier(blueprint: Blueprint, builder: Builder): SpinTieMeme {
            return SpinTieMeme(blueprint as GameStructureBlueprint, builder)
        }

        override val blueprints = listOf(
            ConditionBlueprint(
                1,
                "非常好建筑，使我的领带旋转",
                60,
                5000,
                setOf(),
                setOf(VirtualRequirement(VirtualResourceType.STONE, 24)),
                setOf(ItemRequirement("需要 12 腐肉", ItemStack(Material.ROTTEN_FLESH, 12)),
                    VirtualRequirement(VirtualResourceType.STONE, 96))
            )
        )
    }
}