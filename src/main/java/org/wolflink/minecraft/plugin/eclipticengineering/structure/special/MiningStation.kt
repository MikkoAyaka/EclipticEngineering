package org.wolflink.minecraft.plugin.eclipticengineering.structure.special

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ConditionBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.ItemRequirement
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

/**
 * 开采站(剧情需要)
 */
class MiningStation private constructor(blueprint: Blueprint, builder: Builder) : Structure(blueprint, builder) {
    override val customListener: IStructureListener? = null
    companion object : StructureCompanion<MiningStation>(){
        override val blueprints = listOf(
            ConditionBlueprint(
                1,
                "开采站",
                5,
                3000,
                ItemRequirement("需要 1 圆石", ItemStack(Material.COBBLESTONE)),
                ItemRequirement("需要 1 铁锭", ItemStack(Material.COBBLESTONE))
            )
        )
        override val clazz: Class<MiningStation> = MiningStation::class.java
        override fun supplier(blueprint: Blueprint, builder: Builder): MiningStation {
            return MiningStation(blueprint,builder)
        }
    }
}