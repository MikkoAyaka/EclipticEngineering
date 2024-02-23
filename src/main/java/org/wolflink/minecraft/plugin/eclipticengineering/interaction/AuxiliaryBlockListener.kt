package org.wolflink.minecraft.plugin.eclipticengineering.interaction

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.VirtualResourceType
import org.wolflink.minecraft.plugin.eclipticengineering.extension.abilityTable
import org.wolflink.minecraft.plugin.eclipticengineering.resource.virtual.VirtualTeamInventory

object AuxiliaryBlockListener : Listener {
    // 材质 数量 资源类型 价格
    private val priceSet = mapOf(
        Material.TORCH to Triple(16, VirtualResourceType.WOOD, 4.0),
        Material.SCAFFOLDING to Triple(16, VirtualResourceType.WOOD, 6.0),
        Material.RAIL to Triple(16, VirtualResourceType.METAL, 4.0),
        Material.ACTIVATOR_RAIL to Triple(4, VirtualResourceType.METAL, 4.0),
        Material.LADDER to Triple(16, VirtualResourceType.WOOD, 4.0),
        Material.COBBLESTONE_WALL to Triple(16, VirtualResourceType.STONE, 8.0)
    )

    @EventHandler
    fun on(e: PlayerInteractEvent) {
        if (e.action.isLeftClick && e.hasItem() && e.player.abilityTable.hasAbility(Ability.BUILDING)) {
            if (e.item?.type in priceSet.keys) {
                val priceInfo = priceSet[e.item!!.type]!!
                if(VirtualTeamInventory.has(priceInfo.second,priceInfo.third)) {
                    VirtualTeamInventory.take(priceInfo.second,priceInfo.third)
                    e.player.inventory.addItem(ItemStack(e.item!!.type,priceInfo.first))
                }
            }
        }
    }
}