package org.wolflink.minecraft.plugin.eclipticengineering.resource.item

import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent

/**
 * 可交互物品
 * 需要注册监听器
 */
abstract class InteractiveItem(private val defaultItem: ItemStack): Listener {
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
    fun give(player: Player) {
        player.inventory.addItem(defaultItem)
        player.sendActionBar("$MESSAGE_PREFIX <white>你拿到了 ".toComponent().append(defaultItem.displayName()))
        player.playSound(player, Sound.ITEM_ARMOR_EQUIP_LEATHER,1f,0.8f)
    }
}