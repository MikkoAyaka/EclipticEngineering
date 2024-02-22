package org.wolflink.minecraft.plugin.eclipticengineering.blueprint

import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructureTag
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint

open class GameStructureBlueprint(
    structureLevel: Int,
    structureName: String,
    buildSeconds: Int,
    maxDurability: Int,
    val tags: Set<GameStructureTag>
) : Blueprint(
    structureLevel,
    structureName,
    buildSeconds,
    maxDurability,
)