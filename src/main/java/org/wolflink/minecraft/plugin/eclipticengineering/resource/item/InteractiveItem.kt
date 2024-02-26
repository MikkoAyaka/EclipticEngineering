package org.wolflink.minecraft.plugin.eclipticengineering.resource.item

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * 可交互物品
 * 需要注册监听器
 */
abstract class InteractiveItem(defaultItem: ItemStack, private val cancelInteract: Boolean = false): SpecialItem(defaultItem), Listener {
    protected var lastUpdateMeta: ItemMeta = defaultItem.itemMeta
    protected open fun onRightClick(e: PlayerInteractEvent) {}
    protected open fun onLeftClick(e: PlayerInteractEvent) {}
    protected open fun update(player: Player, itemStack: ItemStack) {}
    @EventHandler
    fun on(e: PlayerInteractEvent) {
        if(e.action.isRightClick && e.item?.itemMeta == lastUpdateMeta) {
            onRightClick(e)
            update(e.player, e.item!!)
            if(cancelInteract) e.isCancelled = true
        }
    }
}