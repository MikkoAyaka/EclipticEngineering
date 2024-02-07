package org.wolflink.minecraft.plugin.eclipticengineering.blueprint

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Blueprint

class ElementalTurretBlueprint(
    structureLevel: Int,
    structureName: String,
    buildSeconds: Int,
    maxDurability: Int,
    val attackCooldownSeconds: Int,
    val attackDamage: IntRange,
    val attackRange: Int,
    vararg requiredItems: ItemStack
): Blueprint(structureLevel,structureName, buildSeconds, maxDurability, *requiredItems)