package org.wolflink.minecraft.plugin.eclipticengineering.resource

import org.bukkit.Location
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure

class OreResourceBlock(structure: Structure,location: Location): ResourceBlock(
    structure,
    location,
    OreResourceCycle(0.0),
    75,
    8,
    Ability.MINING,
    2
)
