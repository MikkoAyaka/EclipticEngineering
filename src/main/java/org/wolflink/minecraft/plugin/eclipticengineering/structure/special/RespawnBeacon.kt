package org.wolflink.minecraft.plugin.eclipticengineering.structure.special

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class RespawnBeacon private constructor(blueprint: Blueprint,builder: Builder): Structure(blueprint,builder),IStructureListener {
    override val customListener: IStructureListener by lazy { this }
    companion object {
        val blueprints = listOf(Blueprint(
            1,
            "幽光充能信标",
            5,
            1000,
            ItemStack(Material.COBBLESTONE,128),
            ItemStack(Material.IRON_INGOT,16),
            ItemStack(Material.GOLD_INGOT,8)
            ))
        fun create(structureLevel: Int, builder: Builder): RespawnBeacon {
            val blueprint = blueprints.getOrNull(structureLevel)
                ?: throw IllegalArgumentException("不支持的建筑等级：${structureLevel}")
            return RespawnBeacon(blueprint, builder)
        }
    }
}