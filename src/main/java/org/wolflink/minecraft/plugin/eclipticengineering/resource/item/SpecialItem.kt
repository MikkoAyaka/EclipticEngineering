package org.wolflink.minecraft.plugin.eclipticengineering.resource.item

import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent

open class SpecialItem(
    val defaultItem: ItemStack
) {
    fun give(player: Player) {
        player.inventory.addItem(defaultItem)
        player.sendActionBar("$MESSAGE_PREFIX <white>你拿到了 ".toComponent().append(defaultItem.displayName()))
        player.playSound(player, Sound.ITEM_ARMOR_EQUIP_LEATHER,1f,0.8f)
    }
}