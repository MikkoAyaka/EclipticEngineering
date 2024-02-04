package org.wolflink.minecraft.plugin.eclipticengineering.blueprint

import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Blueprint

class RespawnBeaconBlueprint(
    structureLevel: Int,
    structureName: String,
    buildSeconds: Int,
    maxDurability: Int,
    val chargeSeconds: Int,
    val chargeAmount: Int,
    vararg requiredItems: ItemStack
): Blueprint(structureLevel,structureName, buildSeconds, maxDurability, *requiredItems)