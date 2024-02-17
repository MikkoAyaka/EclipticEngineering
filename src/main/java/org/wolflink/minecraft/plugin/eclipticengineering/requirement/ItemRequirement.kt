package org.wolflink.minecraft.plugin.eclipticengineering.requirement

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticstructure.extension.hasItems
import org.wolflink.minecraft.plugin.eclipticstructure.extension.takeItems

class ItemRequirement(description: String,private val itemStack: ItemStack): Requirement(description) {
    override fun isSatisfy(player: Player?): Boolean {
        if(player == null) throw IllegalArgumentException("物品条件检测必须指定玩家对象作为参数")
        return player.hasItems(setOf(itemStack))
    }
    override fun delivery(player: Player?): Boolean {
        return player?.takeItems(itemStack) ?: false
    }
}