package org.wolflink.minecraft.plugin.eclipticengineering.extension

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.Quality
import org.wolflink.minecraft.plugin.eclipticengineering.SpecialItemType
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toHex


fun Material.createSpecialItem(specialItemType: SpecialItemType,quality: Quality,itemName: String,glowing: Boolean,description: List<String>): ItemStack {
    val loreTemplate = listOf(
        "<reset>",
        "   <#9C9C9C>│ <#E8E8E8>类型 <#E0FFFF>${specialItemType.displayName}",
        "   <#9C9C9C>│ <#E8E8E8>品质 <#${quality.color.toHex()}>${quality.displayName}",
        "<reset>"
    )
    val itemStack = ItemStack(this)
    val itemMeta = itemStack.itemMeta
    itemMeta.displayName("<#${quality.color.toHex()}>$itemName".toComponent())
    itemMeta.lore((loreTemplate + description).map{ it.toComponent() })
    if(glowing) {
        itemMeta.addEnchant(Enchantment.ARROW_INFINITE,10,true)
        itemMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS)
    }
    return itemStack.apply { setItemMeta(itemMeta) }
}