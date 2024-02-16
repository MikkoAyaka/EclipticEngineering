package org.wolflink.minecraft.plugin.eclipticengineering.extension

import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.Quality
import org.wolflink.minecraft.plugin.eclipticengineering.SpecialItemType
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.SECONDARY_TEXT_COLOR
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.SPLITER_COLOR
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toHex


fun Material.createSpecialItem(specialItemType: SpecialItemType,quality: Quality,itemName: String,glowing: Boolean,description: List<String>): ItemStack {
    val loreTemplate = listOf(
        "<reset>",
        "   ${SPLITER_COLOR}│ ${SECONDARY_TEXT_COLOR}类型 <#E0FFFF>${specialItemType.displayName}",
        "   ${SPLITER_COLOR}│ ${SECONDARY_TEXT_COLOR}品质 <#${quality.color.toHex()}>${quality.displayName}",
        "<reset>"
    )
    val itemStack = ItemStack(this)
    val itemMeta = itemStack.itemMeta
    itemMeta.displayName("<#${quality.color.toHex()}>$itemName".toComponent().decorationIfAbsent(TextDecoration.ITALIC,TextDecoration.State.FALSE))
    itemMeta.lore((loreTemplate + description + "<reset>").map{ it.toComponent().decorationIfAbsent(TextDecoration.ITALIC,TextDecoration.State.FALSE) })
    if(glowing) {
        itemMeta.addEnchant(Enchantment.ARROW_INFINITE,10,true)
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
    }
    return itemStack.apply { setItemMeta(itemMeta) }
}