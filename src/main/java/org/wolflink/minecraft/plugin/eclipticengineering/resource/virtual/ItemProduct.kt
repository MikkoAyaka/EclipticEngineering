package org.wolflink.minecraft.plugin.eclipticengineering.resource.virtual

import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.VirtualResourceType

/**
 * TODO 花费虚拟资源购买商品
 */
abstract class ItemProduct(val item: ItemStack,val type: VirtualResourceType,val price: Double) {
    fun buy(player: Player) {
        if(VirtualTeamInventory.has(type,price)) {
            VirtualTeamInventory.take(type,price)
            player.playSound(player,Sound.BLOCK_NOTE_BLOCK_BIT,0.6f,2f)
            player.inventory.addItem(item)
        }
    }
}