package org.wolflink.minecraft.plugin.eclipticengineering.resource.crop

import org.bukkit.Location
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.resource.ResourceBlock
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure

class CropResourceBlock(structure: Structure, location: Location): ResourceBlock(
    structure,
    location,
    CropResourceCycle(),
    300,
    1,
    Ability.FARMING,
    2
)
