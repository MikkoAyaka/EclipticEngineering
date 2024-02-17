package org.wolflink.minecraft.plugin.eclipticengineering.blueprint

import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.Condition
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint

class TowerArrowBlueprint(
    structureLevel: Int,
    structureName: String,
    buildSeconds: Int,
    maxDurability: Int,
    val extraDamageRange: IntRange,
    val maxShooterAmount: Int,
    val returnArrowPercent: Double,
    vararg conditions: Condition
): ConditionBlueprint(structureLevel,structureName, buildSeconds, maxDurability, *conditions)