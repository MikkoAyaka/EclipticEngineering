package org.wolflink.minecraft.plugin.eclipticengineering.blueprint

import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructureTag
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint

open class GameStructureBlueprint(
    structureLevel: Int,
    structureName: String,
    buildSeconds: Int,
    maxDurability: Int,
    pasteAir: Boolean = false,
    val tags: Set<GameStructureTag>
) : Blueprint(
    structureLevel,
    structureName,
    buildSeconds,
    maxDurability,
    pasteAir
)