package org.wolflink.minecraft.plugin.eclipticengineering.blueprint

import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.Condition
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint

class RespawnBeaconBlueprint(
    structureLevel: Int,
    structureName: String,
    buildSeconds: Int,
    maxDurability: Int,
    val chargeSeconds: Int,
    val chargeAmount: Int,
    vararg conditions: Condition
): ConditionBlueprint(structureLevel,structureName, buildSeconds, maxDurability, *conditions)