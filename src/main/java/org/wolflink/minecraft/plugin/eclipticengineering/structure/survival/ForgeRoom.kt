package org.wolflink.minecraft.plugin.eclipticengineering.structure.survival

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ConditionBlueprint
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

class ForgeRoom private constructor(
    blueprint: Blueprint, builder: Builder
) : GameStructure(StructureType.FORGE_ROOM,blueprint, builder) {
    override val customListeners = listOf<IStructureListener>()
    companion object : StructureCompanion<ForgeRoom>() {
        override fun supplier(blueprint: Blueprint, builder: Builder): ForgeRoom {
            return ForgeRoom(blueprint, builder)
        }

        override val blueprints = listOf(
            ConditionBlueprint(
                1,
                "锻造站台",
                60,
                5000,
                setOf(),
                VirtualRequirement(VirtualResourceType.STONE,60),
                VirtualRequirement(VirtualResourceType.METAL,30),
                ItemRequirement("需要 5 陨铁矿石", SpecialIron.defaultItem.clone().apply { amount = 5 }),
                AbilityCondition(Ability.BUILDING,2)
            )
        )
    }
}