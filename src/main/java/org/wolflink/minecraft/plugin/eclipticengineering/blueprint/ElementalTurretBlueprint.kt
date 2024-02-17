package org.wolflink.minecraft.plugin.eclipticengineering.blueprint

import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.Condition
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint

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
): ConditionBlueprint(structureLevel,structureName, buildSeconds, maxDurability, *conditions)