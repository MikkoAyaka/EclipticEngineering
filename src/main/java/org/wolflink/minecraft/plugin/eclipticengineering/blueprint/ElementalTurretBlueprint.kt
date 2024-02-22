package org.wolflink.minecraft.plugin.eclipticengineering.blueprint

import org.bukkit.util.Vector
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.Condition
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructureTag

class ElementalTurretBlueprint(
    structureLevel: Int,
    structureName: String,
    buildSeconds: Int,
    maxDurability: Int,
    val displayEntityRelativeVector: Vector,
    val attackCooldownSeconds: Int,
    val attackDamage: IntRange,
    val attackRange: Int,
    vararg conditions: Condition
) : ConditionBlueprint(
    structureLevel, structureName, buildSeconds, maxDurability, setOf(
        GameStructureTag.ENERGY_REQUIRED
    ), *conditions
)