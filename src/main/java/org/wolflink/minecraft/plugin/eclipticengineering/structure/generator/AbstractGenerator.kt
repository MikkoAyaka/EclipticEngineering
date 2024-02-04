package org.wolflink.minecraft.plugin.eclipticengineering.structure.generator

import kotlinx.coroutines.launch
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.GeneratorBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.resource.ResourceBlock
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureCompletedEvent
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureDurabilityDamageEvent
import org.wolflink.minecraft.plugin.eclipticstructure.extension.deepEquals
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder


abstract class AbstractGenerator(blueprint: GeneratorBlueprint,builder: Builder): Structure(blueprint,builder), IStructureListener {
    override val customListener by lazy { this }
    // 矿物资源方块
    private val resourceBlocks by lazy { blueprint.resourceBlocksSupplier(this,builder.buildLocation) }
    private fun getOreResourceBlock(player: Player): ResourceBlock? {
        val targetBlock = player.getTargetBlockExact(4) ?: return null
        val targetLocationTriple = Triple(targetBlock.location.blockX,targetBlock.location.blockY,targetBlock.location.blockZ)
        return resourceBlocks.firstOrNull {
            Triple(it.location.blockX,it.location.blockY,it.location.blockZ).deepEquals(targetLocationTriple)
        }
    }
    override fun onDurabilityDamage(e: StructureDurabilityDamageEvent) {
        if(e.damageSourceType == DamageSource.PLAYER_BREAK) {
            val player = e.damageSource as? Player ?: return
            val resource = getOreResourceBlock(player) ?: return
            // 取消事件，防止玩家不小心损坏结构
            e.isCancelled = true
            // 采集资源
            resource.breaking(player)
        }
    }

    override fun completed(e: StructureCompletedEvent) {
        resourceBlocks.forEach {
            EEngineeringScope.launch { it.reset() }
        }
    }
}