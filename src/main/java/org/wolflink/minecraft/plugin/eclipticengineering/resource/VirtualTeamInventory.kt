package org.wolflink.minecraft.plugin.eclipticengineering.resource

import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.VirtualResourceType
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.isVirtualResource
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.isWood
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toHexFormat
import java.util.concurrent.atomic.AtomicInteger

/**
 * 团队虚拟背包
 * 存放各种虚拟物资
 * 当玩家 SHIFT+右键 可虚拟化的物资时，将自动存储到团队虚拟背包中
 */
object VirtualTeamInventory: Listener {
    private val resourceMap = enumValues<VirtualResourceType>().associateWith { AtomicInteger(0) }
    @Synchronized private fun add(type: VirtualResourceType,amount: Int) {
        resourceMap[type]!!.addAndGet(amount)
        gamingPlayers.forEach {  player ->
            player.sendActionBar("<green>+ <white>$amount ${type.color.toHexFormat()}${type.displayName}".toComponent())
            player.playSound(player, Sound.BLOCK_CHEST_OPEN,1f,1.5f)
        }
    }
    fun has(type: VirtualResourceType,amount: Int): Boolean {
        return resourceMap[type]!!.get() >= amount
    }
    @Synchronized fun take(type: VirtualResourceType,amount: Int): Boolean {
        return if(has(type,amount)) {
            resourceMap[type]!!.addAndGet(-amount)
            gamingPlayers.forEach {  player ->
                player.sendActionBar("<red>- <white>$amount ${type.color.toHexFormat()}${type.displayName}".toComponent())
                player.playSound(player, Sound.BLOCK_CHEST_CLOSE,1f,1.5f)
            }
            true
        } else false
    }

    private fun interactVirtualItemHandler(player: Player, itemStack: ItemStack) {
        when(val material = itemStack.type) {
            Material.IRON_INGOT -> add(VirtualResourceType.METAL,5 * itemStack.amount)
            Material.GOLD_INGOT -> add(VirtualResourceType.METAL,10 * itemStack.amount)
            Material.DIAMOND -> add(VirtualResourceType.METAL,30 * itemStack.amount)
            Material.STONE -> add(VirtualResourceType.STONE,1 * itemStack.amount)
            Material.COBBLESTONE -> add(VirtualResourceType.STONE,1 * itemStack.amount)
            Material.DEEPSLATE -> add(VirtualResourceType.STONE,2 * itemStack.amount)
            Material.COBBLED_DEEPSLATE -> add(VirtualResourceType.STONE,2 * itemStack.amount)
            else -> {
                if(material.isWood()) add(VirtualResourceType.WOOD,1 * itemStack.amount)
                else return
            }
        }
        player.inventory.removeItem(itemStack)
    }
    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {
        if(e.hasItem() && e.action.isRightClick && e.player.isSneaking) {
            e.isCancelled = true
            val itemStack = e.item!!
            if(itemStack.type.isVirtualResource()) interactVirtualItemHandler(e.player,itemStack)
        }
    }
}