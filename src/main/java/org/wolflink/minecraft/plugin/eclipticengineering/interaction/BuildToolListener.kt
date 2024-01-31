package org.wolflink.minecraft.plugin.eclipticengineering.interaction

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.wolflink.minecraft.plugin.eclipticengineering.config.BUILD_MENU_CMD
import org.wolflink.minecraft.plugin.eclipticstructure.extension.parsePapi

object BuildToolListener: Listener {
    /**
     * 玩家是否在使用建筑魔杖
     */
    private fun isUsingBuildTool(e: PlayerInteractEvent) =
        e.action.isRightClick && e.player.inventory.itemInMainHand.type == Material.SPYGLASS

    /**
     * 打开建筑菜单
     */
    private fun openBuildMenu(player: Player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), BUILD_MENU_CMD.parsePapi(player))
    }
    @EventHandler
    fun on(e: PlayerInteractEvent) {
        if(isUsingBuildTool(e)) {
            openBuildMenu(e.player)
            e.isCancelled = true
        }
    }
}