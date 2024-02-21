package org.wolflink.minecraft.plugin.eclipticengineering.structure.listener

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructure
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructureCounter
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructureTag
import org.wolflink.minecraft.plugin.eclipticstructure.event.builder.*
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.IBuilderListener

object BuilderListener: Listener, IBuilderListener {
    private fun amountCheck(structure: GameStructure,player: Player): Boolean {
        val atomicInteger = GameStructureCounter.counter[structure::class.java] ?: throw IllegalArgumentException("未注册到计数器的建筑结构类：${structure::class.java.name}")
        val amount = atomicInteger.get()
        // 达到建筑数量限制
        if(amount >= structure.maxAmount) {
            player.sendMessage("$MESSAGE_PREFIX <yellow>该类型建筑已达到建筑数量限制，无法建造更多。".toComponent())
            return false
        } else atomicInteger.incrementAndGet()
        return true
    }
    private fun energyCheck(structure: GameStructure,player: Player): Boolean {

        val availableEnergySources = GameStructureCounter.energyStructures.filter {
            structure.builder.buildLocation.distance(it.builder.buildLocation) <= 40
        }
        if(availableEnergySources.isEmpty()) {
            player.sendMessage("$MESSAGE_PREFIX <yellow>该建筑需要供能，在 40 格范围内未找到有效建筑结构。".toComponent())
            return false
        }
        return true
    }
    @EventHandler
    override fun preBuild(e: BuilderPreBuildEvent) {
        val structure = e.builder.structure
        if(structure is GameStructure) {
            if(!amountCheck(structure,e.player)) {
                e.isCancelled = true
                return
            }
            if(GameStructureTag.ENERGY_REQUIRED in structure.tags) {
                if(!energyCheck(structure,e.player)) {
                    e.isCancelled = true
                    return
                }
            }
        }
    }
    @EventHandler
    override fun completed(e: BuilderCompletedEvent) {
        val structure = e.structure
        if(structure is GameStructure) {
            if(GameStructureTag.ENERGY_SUPPLY in structure.tags) {
                GameStructureCounter.energyStructures.add(structure)
            }
        }
    }
    @EventHandler
    override fun started(e: BuilderStartedEvent) {
    }
    @EventHandler
    override fun toggleStatus(e: BuilderStatusEvent) {
    }

    private fun amountDecrement(structure: GameStructure) {
        val atomicInteger = GameStructureCounter.counter[structure::class.java] ?: throw IllegalArgumentException("未注册到计数器的建筑结构类：${structure::class.java.name}")
        val amount = atomicInteger.decrementAndGet()
        if(amount < 0) throw IllegalStateException("建筑数量被更改为 $amount 而最少应该是 0")
    }
    @EventHandler
    override fun destroyed(e: BuilderDestroyedEvent) {
        val structure = e.builder.structure
        if(structure is GameStructure) {
            amountDecrement(structure)
            if(GameStructureTag.ENERGY_SUPPLY in structure.tags) {
                GameStructureCounter.energyStructures.remove(structure)
            }
        }
    }

}