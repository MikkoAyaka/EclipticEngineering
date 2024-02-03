package org.wolflink.minecraft.plugin.eclipticengineering.structure.generator

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class GeneratorCrop(builder: Builder) : Structure(blueprint,builder) {
    companion object {
        val blueprint = Blueprint(
            "§a耕种场",
            "${this::class.java.enclosingClass.simpleName}.schem",
            30,
            1000,
            ItemStack(Material.COBBLESTONE,128),
            ItemStack(Material.IRON_INGOT,16),
            ItemStack(Material.GOLD_INGOT,8)
            )
    }

    override val customListener: IStructureListener?
        get() = TODO("Not yet implemented")
}