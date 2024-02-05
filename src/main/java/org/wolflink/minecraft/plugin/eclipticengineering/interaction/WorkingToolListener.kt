package org.wolflink.minecraft.plugin.eclipticengineering.interaction

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.isAXe
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.isFurnace
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.isHoe
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.isPickaxe
import org.wolflink.minecraft.plugin.eclipticengineering.extension.abilityTable

/**
 * 检查玩家是否有能力使用对应的工具
 */
object WorkingToolListener: Listener {
    @EventHandler
    fun checkTool(e: PlayerInteractEvent) {
        // 与工具交互
        if(e.action == Action.LEFT_CLICK_BLOCK && e.item != null) {
            val itemType = e.item!!.type
            if(itemType.isAXe() && !e.player.abilityTable.hasAbility(Ability.LOGGING)) {
                e.isCancelled = true
                return
            }
            if(itemType.isHoe() && !e.player.abilityTable.hasAbility(Ability.FARMING)) {
                e.isCancelled = true
                return
            }
            if(itemType.isPickaxe() && !e.player.abilityTable.hasAbility(Ability.MINING)) {
                e.isCancelled = true
                return
            }
        }
        val blockType = e.clickedBlock?.type ?: return
        // 与方块交互
        if(e.action == Action.RIGHT_CLICK_BLOCK) {
            // 与熔炉方块交互
            if(blockType.isFurnace() && !e.player.abilityTable.hasAbility(Ability.SMELTING)) {
                e.isCancelled = true
                return
            }
            // 与工作台交互
            if(blockType == Material.CRAFTING_TABLE && !e.player.abilityTable.hasAbility(Ability.ENGINEERING)) {
                e.isCancelled = true
                return
            }
        }
    }
}