package org.wolflink.minecraft.plugin.eclipticengineering.blueprint

import org.wolflink.minecraft.plugin.eclipticengineering.requirement.Condition
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructureTag

open class ConditionBlueprint(
    structureLevel: Int,
    structureName: String,
    buildSeconds: Int,
    maxDurability: Int,
    tags: Set<GameStructureTag>,
    vararg val conditions: Condition
) : GameStructureBlueprint(structureLevel, structureName, buildSeconds, maxDurability, tags)