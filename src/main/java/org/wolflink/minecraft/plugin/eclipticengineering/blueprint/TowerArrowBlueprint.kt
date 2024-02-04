package org.wolflink.minecraft.plugin.eclipticengineering.blueprint

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Blueprint

class TowerArrowBlueprint(
    structureLevel: Int,
    structureName: String,
    buildSeconds: Int,
    maxDurability: Int,
    val extraDamageRange: IntRange,
    val maxShooterAmount: Int,
    val returnArrowPercent: Double,
    vararg requiredItems: ItemStack
): Blueprint(structureLevel,structureName, buildSeconds, maxDurability, *requiredItems)