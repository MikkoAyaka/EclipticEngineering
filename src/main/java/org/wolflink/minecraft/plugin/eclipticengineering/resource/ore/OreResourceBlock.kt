package org.wolflink.minecraft.plugin.eclipticengineering.resource.ore

import org.bukkit.Location
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.resource.ResourceBlock
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure

class OreResourceBlock(structure: Structure,location: Location): ResourceBlock(
    structure,
    location,
    OreResourceCycle(),
    90,
    8,
    Ability.MINING,
    2
)
