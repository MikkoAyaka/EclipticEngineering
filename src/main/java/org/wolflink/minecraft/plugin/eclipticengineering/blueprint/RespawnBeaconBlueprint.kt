package org.wolflink.minecraft.plugin.eclipticengineering.blueprint

import org.wolflink.minecraft.plugin.eclipticengineering.requirement.Condition
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructureTag

class RespawnBeaconBlueprint(
    structureLevel: Int,
    structureName: String,
    buildSeconds: Int,
    maxDurability: Int,
    val chargeSeconds: Int,
    val chargeAmount: Int,
    repairConditions: Set<Condition>,
    buildConditions: Set<Condition>
) : ConditionBlueprint(
    structureLevel, structureName, buildSeconds, maxDurability,
    setOf(
        GameStructureTag.AMOUNT_LIMITED
    ),
    repairConditions,
    buildConditions
)