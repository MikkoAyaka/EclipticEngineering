package org.wolflink.minecraft.plugin.eclipticengineering.structure.foothold

import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ConditionBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.GameStructureBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.VirtualResourceType
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.AbilityCondition
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.VirtualRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.EnergyRequiredListener
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructure
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructureTag
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

/**
 * 开采站(剧情需要)
 */
class MiningStation private constructor(blueprint: GameStructureBlueprint, builder: Builder) :
    GameStructure(StructureType.MINING_STATION, blueprint, builder) {
    override val customListeners = listOf(EnergyRequiredListener(this))

    companion object : StructureCompanion<MiningStation>() {
        override val blueprints = listOf(
            ConditionBlueprint(
                1,
                "幽光开采站",
                120,
                10000,
                setOf(
                    GameStructureTag.ENERGY_REQUIRED
                ),
                setOf(),
                setOf(VirtualRequirement(VirtualResourceType.STONE, 90),
                    VirtualRequirement(VirtualResourceType.METAL, 60),
                    AbilityCondition(Ability.BUILDING,3))
            )
        )

        override fun supplier(blueprint: Blueprint, builder: Builder): MiningStation {
            return MiningStation(blueprint as GameStructureBlueprint, builder)
        }
    }
}