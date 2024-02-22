package org.wolflink.minecraft.plugin.eclipticengineering.blueprint

import org.wolflink.minecraft.plugin.eclipticengineering.requirement.Condition

class TowerArrowBlueprint(
    structureLevel: Int,
    structureName: String,
    buildSeconds: Int,
    maxDurability: Int,
    val extraDamageRange: IntRange,
    val maxShooterAmount: Int,
    val returnArrowPercent: Double,
    vararg conditions: Condition
) : ConditionBlueprint(
    structureLevel, structureName, buildSeconds, maxDurability, setOf(), *conditions
)