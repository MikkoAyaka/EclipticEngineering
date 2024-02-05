package org.wolflink.minecraft.plugin.eclipticengineering.interaction

import com.destroystokyo.paper.Namespaced
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.isAuxiliaryBlock
import org.wolflink.minecraft.plugin.eclipticengineering.extension.abilityTable

object AuxiliaryBlockListener: Listener {
    init {
        EEngineeringScope.launch {
            while (EclipticEngineering.instance.isEnabled) {
                // 每0.3秒检查一次
                delay(300)
                Bukkit.getOnlinePlayers()
                    // 将手持特殊物品并且拥有建筑能力的玩家筛选出来
                    .filter {
                        it.gameMode == GameMode.ADVENTURE
                                && it.abilityTable.hasAbility(Ability.BUILDING)
                                && it.inventory.itemInMainHand.type.isAuxiliaryBlock()
                    }
                    .forEach {
                        Bukkit.getScheduler().runTask(EclipticEngineering.instance, Runnable {
                            val targetBlockType = it.getTargetBlockExact(4)?.type
                            if(targetBlockType != null) {
                                val item = it.inventory.itemInMainHand
                                val newMeta = item.itemMeta.apply { this.setPlaceableKeys(mutableSetOf<Namespaced>(targetBlockType.key)) }
                                item.setItemMeta(newMeta)
                            }
                        })
                    }
            }
        }
    }
    @EventHandler
    fun onPlace(e: BlockPlaceEvent) {
        val blockType = e.block.type
        if(!blockType.isAuxiliaryBlock()) return
        if(!e.player.abilityTable.checkAbilityWithNotice(Ability.BUILDING)) {
            e.isCancelled = true
            return
        }
    }

}