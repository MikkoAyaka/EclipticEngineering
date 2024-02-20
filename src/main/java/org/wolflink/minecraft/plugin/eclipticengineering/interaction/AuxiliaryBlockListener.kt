package org.wolflink.minecraft.plugin.eclipticengineering.interaction

import com.destroystokyo.paper.Namespaced
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.isAuxiliaryBlock
import org.wolflink.minecraft.plugin.eclipticengineering.extension.abilityTable
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers

@Deprecated("已由冒险模式改为生存模式")
object AuxiliaryBlockListener: Listener {
//    init {
//        EEngineeringScope.launch {
//            while (EclipticEngineering.instance.isEnabled) {
//                // 每0.3秒检查一次
//                delay(300)
//                gamingPlayers
//                    .filter {
//                        it.abilityTable.hasAbility(Ability.BUILDING)
//                                && it.inventory.itemInMainHand.type.isAuxiliaryBlock()
//                    }
//                    .forEach {
//                        Bukkit.getScheduler().runTask(EclipticEngineering.instance, Runnable {
//                            val targetBlockType = it.getTargetBlockExact(4)?.type
//                            if(targetBlockType != null) {
//                                val item = it.inventory.itemInMainHand
//                                val newMeta = item.itemMeta.apply {
//                                    setPlaceableKeys(mutableSetOf<Namespaced>(targetBlockType.key))
//                                    setDestroyableKeys(mutableSetOf<Namespaced>(item.type.key))
//                                }
//                                item.setItemMeta(newMeta)
//                            }
//                        })
//                    }
//            }
//        }
//    }
//    @EventHandler
//    fun onPlace(e: BlockPlaceEvent) {
//        val blockType = e.block.type
//        if(!blockType.isAuxiliaryBlock()) return
//        if(!e.player.abilityTable.checkAbilityWithNotice(Ability.BUILDING)) {
//            e.isCancelled = true
//            return
//        }
//    }

}