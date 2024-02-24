package org.wolflink.minecraft.plugin.eclipticengineering.structure.resource

import kotlinx.coroutines.launch
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.GeneratorBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticengineering.resource.ResourceBlock
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructure
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureCompletedEvent
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureDurabilityDamageEvent
import org.wolflink.minecraft.plugin.eclipticstructure.extension.deepEquals
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder


abstract class AbstractGenerator(type: StructureType, blueprint: GeneratorBlueprint, builder: Builder) :
    GameStructure(type, blueprint, builder, 1), IStructureListener {
    override val customListeners by lazy { listOf(this) }

    // 矿物资源方块
    private val resourceBlocks by lazy { blueprint.resourceBlocksSupplier(this, builder.buildLocation) }
    private fun getResourceBlock(player: Player): ResourceBlock? {
        val targetBlock = player.getTargetBlockExact(4) ?: return null
        val targetLocationTriple =
            Triple(targetBlock.location.blockX, targetBlock.location.blockY, targetBlock.location.blockZ)
        return resourceBlocks.firstOrNull {
            Triple(it.location.blockX, it.location.blockY, it.location.blockZ).deepEquals(targetLocationTriple)
        }
    }

    override fun onDurabilityDamage(e: StructureDurabilityDamageEvent) {
        if (e.damageSourceType == DamageSource.PLAYER_BREAK) {
            val player = e.damageSource as? Player ?: return
            val resource = getResourceBlock(player) ?: return
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