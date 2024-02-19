package org.wolflink.minecraft.plugin.eclipticengineering.interaction

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.*
import org.wolflink.minecraft.plugin.eclipticengineering.extension.abilityTable
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers

/**
 * 检查玩家是否有能力使用对应的工具
 * 刷新工具破坏表
 */
object WorkingToolListener: Listener {
    init {
        EEngineeringScope.launch {
            while (EclipticEngineering.instance.isEnabled) {
                // 每0.3秒检查一次
                delay(300)
                gamingPlayers
                    .forEach {
                        val item = it.inventory.itemInMainHand
                        val destroyKeys = DestroyableMap.get(item.type)
                        if(destroyKeys.isNotEmpty()) item.itemMeta = item.itemMeta.apply {
                            setDestroyableKeys(destroyKeys)
                        }
                    }
            }
        }
    }
    @EventHandler
    fun checkTool(e: PlayerInteractEvent) {
        // 左键与工具交互
        if(e.action.isLeftClick && e.item != null) {
            val itemType = e.item!!.type
            if(itemType.isAxe() && !e.player.abilityTable.checkAbilityWithNotice(Ability.LOGGING)) {
                e.isCancelled = true
                return
            }
            if(itemType.isHoe() && !e.player.abilityTable.checkAbilityWithNotice(Ability.FARMING)) {
                e.isCancelled = true
                return
            }
            if(itemType.isPickaxe() && !e.player.abilityTable.checkAbilityWithNotice(Ability.MINING)) {
                e.isCancelled = true
                return
            }
        }
        // 右键与工具交互
        if(e.action.isRightClick && e.item != null) {
            val itemType = e.item!!.type
            if(itemType.isRemoteWeapon() && !e.player.abilityTable.checkAbilityWithNotice(Ability.COMBAT)) {
                e.isCancelled = true
                return
            }
            if(itemType.isHoe() && !e.player.abilityTable.checkAbilityWithNotice(Ability.FARMING)) {
                e.isCancelled = true
                return
            }
        }
        val blockType = e.clickedBlock?.type ?: return
        // 与方块交互
        if(e.action == Action.RIGHT_CLICK_BLOCK) {
            // 与熔炉方块交互
            if(blockType.isFurnace() && !e.player.abilityTable.checkAbilityWithNotice(Ability.SMELTING)) {
                e.isCancelled = true
                return
            }
            // 与工作台交互
            if(blockType == Material.CRAFTING_TABLE && !e.player.abilityTable.checkAbilityWithNotice(Ability.ENGINEERING)) {
                e.isCancelled = true
                return
            }
        }
    }
}