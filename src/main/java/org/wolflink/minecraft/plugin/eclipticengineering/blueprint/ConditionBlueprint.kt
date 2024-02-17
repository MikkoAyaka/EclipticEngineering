package org.wolflink.minecraft.plugin.eclipticengineering.blueprint

import org.wolflink.minecraft.plugin.eclipticengineering.requirement.Condition
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint

open class ConditionBlueprint(
    structureLevel: Int,
    structureName: String,
    buildSeconds: Int,
    maxDurability: Int,
    vararg val conditions: Condition
) : Blueprint(structureLevel, structureName, buildSeconds, maxDurability)