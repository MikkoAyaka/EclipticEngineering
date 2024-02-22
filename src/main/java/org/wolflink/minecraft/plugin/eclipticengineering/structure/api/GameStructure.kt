package org.wolflink.minecraft.plugin.eclipticengineering.structure.api

import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

abstract class GameStructure(val type: StructureType, blueprint: Blueprint, builder: Builder, val maxAmount: Int = 100): Structure(blueprint, builder) {
    /**
     * 该建筑是否拥有能源(被其它能源建筑充能)
     */
    fun hasEnergySource(): Boolean {
        return GameStructureCounter.energyStructures.any { it.available && builder.buildLocation.distance(it.builder.buildLocation) <= 40 }
    }
}