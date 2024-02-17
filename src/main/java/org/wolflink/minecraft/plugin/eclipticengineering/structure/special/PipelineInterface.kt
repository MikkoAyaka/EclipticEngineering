package org.wolflink.minecraft.plugin.eclipticengineering.structure.special

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ConditionBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.extension.hasConnection
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.ItemRequirement
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class PipelineInterface private constructor(blueprint: Blueprint, builder: Builder) : Structure(blueprint, builder){
    override val customListener: IStructureListener? = null

    /**
     * 检查两个管道结构之间是否存在通路(被指定方块连接)
     */
    fun hasConnection(another: PipelineInterface): Boolean {
        val thisLoc = builder.buildLocation
        val anotherLoc = another.builder.buildLocation
        return thisLoc.block.hasConnection(anotherLoc.block,Material.SCULK)
    }
    companion object : StructureCompanion<PipelineInterface>(){
        override val clazz: Class<PipelineInterface> = PipelineInterface::class.java
        override fun supplier(blueprint: Blueprint, builder: Builder): PipelineInterface {
            return PipelineInterface(blueprint, builder)
        }

        override val blueprints = listOf(
            ConditionBlueprint(
                1,
                "管道接口",
                5,
                3000,
                ItemRequirement("需要 1 圆石", ItemStack(Material.COBBLESTONE)),
                ItemRequirement("需要 1 铁锭", ItemStack(Material.COBBLESTONE))
            )
        )
    }
}