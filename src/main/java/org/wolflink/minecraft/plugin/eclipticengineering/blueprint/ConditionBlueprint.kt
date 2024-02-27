package org.wolflink.minecraft.plugin.eclipticengineering.blueprint

import org.wolflink.minecraft.plugin.eclipticengineering.requirement.Condition
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructureTag

open class ConditionBlueprint(
    structureLevel: Int,
    structureName: String,
    buildSeconds: Int,
    maxDurability: Int,
    pasteAir: Boolean,
    tags: Set<GameStructureTag>,
    repairConditions: Set<Condition>,
    val buildConditions: Set<Condition>
) : GameStructureBlueprint(
    structureLevel,
    structureName,
    buildSeconds,
    maxDurability,
    pasteAir,
    tags,
    repairConditions
) {
    constructor(
        structureLevel: Int,
        structureName: String,
        buildSeconds: Int,
        maxDurability: Int,
        tags: Set<GameStructureTag>,
        repairConditions: Set<Condition>,
        buildConditions: Set<Condition>
    ): this(
        structureLevel,
        structureName,
        buildSeconds,
        maxDurability,
        false,
        tags,
        repairConditions,
        buildConditions
    )
}