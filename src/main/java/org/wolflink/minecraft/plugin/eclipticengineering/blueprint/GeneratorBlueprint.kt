package org.wolflink.minecraft.plugin.eclipticengineering.blueprint

import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.Condition
import org.wolflink.minecraft.plugin.eclipticengineering.resource.ResourceBlock
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure

/**
 * 资源产出型建筑蓝图
 */
class GeneratorBlueprint(
    structureLevel: Int,
    structureName: String,
    buildSeconds: Int,
    maxDurability: Int,
    val resourceBlocksSupplier: (structure: Structure,buildLocation:Location) -> Set<ResourceBlock>,
    vararg conditions: Condition
): ConditionBlueprint(structureLevel,structureName, buildSeconds, maxDurability, *conditions)