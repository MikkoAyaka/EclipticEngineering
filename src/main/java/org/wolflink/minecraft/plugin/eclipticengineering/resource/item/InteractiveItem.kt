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
abstract class InteractiveItem(defaultItem: ItemStack): SpecialItem(defaultItem), Listener {
    protected var lastUpdateMeta: ItemMeta = defaultItem.itemMeta
    protected open fun onInteract(player: Player) {}
    protected open fun update(player: Player, itemStack: ItemStack) {}
    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {
        if(e.action.isRightClick && e.item?.itemMeta == lastUpdateMeta) {
            onInteract(e.player)
            update(e.player, e.item!!)
        }
    }
}