package org.wolflink.minecraft.plugin.eclipticengineering.structure.special

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.structure.decoration.Lighthouse
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class PipelineInterface private constructor(blueprint: Blueprint, builder: Builder) : Structure(blueprint, builder){
    override val customListener: IStructureListener? = null

    companion object : StructureCompanion<PipelineInterface>(){
        override val clazz: Class<PipelineInterface> = PipelineInterface::class.java
        override val blueprints = listOf(
            Blueprint(
                1,
                "管道接口",
                5,
                3000,
                ItemStack(Material.COBBLESTONE, 128),
                ItemStack(Material.IRON_INGOT, 16),
                ItemStack(Material.GOLD_INGOT, 8)
            )
        )
    }
}