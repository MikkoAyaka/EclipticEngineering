package org.wolflink.minecraft.plugin.eclipticengineering.structure.survival

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ConditionBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.GameStructureBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.VirtualResourceType
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.AbilityCondition
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.ItemRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.VirtualRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.SpecialGold
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.SpecialIron
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructure
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructureTag
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class EnchantRoom private constructor(
    blueprint: GameStructureBlueprint, builder: Builder
) : GameStructure(StructureType.ENCHANT_ROOM,blueprint, builder) {
    override val customListeners = listOf<IStructureListener>()
    companion object : StructureCompanion<EnchantRoom>() {
        override fun supplier(blueprint: Blueprint, builder: Builder): EnchantRoom {
            return EnchantRoom(blueprint as GameStructureBlueprint, builder)
        }

        override val blueprints = listOf(
            ConditionBlueprint(
                1,
                "附魔站台",
                60,
                5000,
                setOf(),
                setOf(VirtualRequirement(VirtualResourceType.STONE,10)),
                setOf(VirtualRequirement(VirtualResourceType.STONE,40),
                    VirtualRequirement(VirtualResourceType.METAL,15),
                    ItemRequirement("需要 2 闪金矿石", SpecialGold.defaultItem.clone().apply { amount = 2 }),
                    AbilityCondition(Ability.BUILDING,2))
            )
        )
    }
}