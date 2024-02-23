package org.wolflink.minecraft.plugin.eclipticengineering.structure.special

import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ConditionBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.VirtualResourceType
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.AbilityCondition
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.VirtualRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructure
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructureTag
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class LivingHouse private constructor(
    blueprint: Blueprint, builder: Builder
) : GameStructure(StructureType.LIVING_HOUSE, blueprint, builder,1) {
    override val customListeners = listOf<IStructureListener>()

    companion object : StructureCompanion<LivingHouse>() {
        override fun supplier(blueprint: Blueprint, builder: Builder): LivingHouse {
            return LivingHouse(blueprint, builder)
        }

        override val blueprints = listOf(
            ConditionBlueprint(
                1,
                "居住屋",
                120,
                20000,
                setOf(
                    GameStructureTag.AMOUNT_LIMITED
                ),
                VirtualRequirement("需要 90 石料", VirtualResourceType.STONE, 90),
                VirtualRequirement("需要 90 木材", VirtualResourceType.WOOD, 90),
                AbilityCondition(Ability.BUILDING,3)
            )
        )
    }
}