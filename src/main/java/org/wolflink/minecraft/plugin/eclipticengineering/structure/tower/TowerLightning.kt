package org.wolflink.minecraft.plugin.eclipticengineering.structure.tower

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureBlueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureBuilder

class TowerLightning(builder: StructureBuilder) : Structure(blueprint,builder) {
    companion object {
        val blueprint = StructureBlueprint(
            "§a引雷塔",
            "${this::class.java.enclosingClass.simpleName}.schem",
            30,
            ItemStack(Material.COBBLESTONE,128),
            ItemStack(Material.IRON_INGOT,16),
            ItemStack(Material.GOLD_INGOT,8)
            )
    }
}