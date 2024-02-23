package org.wolflink.minecraft.plugin.eclipticengineering.resource.virtual

import com.google.common.util.concurrent.AtomicDouble
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent
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
    private val resourceMap = enumValues<VirtualResourceType>().associateWith { AtomicDouble(0.0) }
    @Synchronized private fun add(type: VirtualResourceType,amount: Double) {
        resourceMap[type]!!.addAndGet(amount)
        gamingPlayers.forEach {  player ->
            player.sendActionBar("<green>+ <white>$amount ${type.color.toHexFormat()}${type.displayName}".toComponent())
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL,0.4f,1.5f)
        }
    }
    fun get(type: VirtualResourceType): Double = resourceMap[type]!!.get()
    fun has(type: VirtualResourceType,amount: Double): Boolean {
        return resourceMap[type]!!.get() >= amount
    }
    @Synchronized fun take(type: VirtualResourceType,amount: Double): Boolean {
        return if(has(type,amount)) {
            resourceMap[type]!!.addAndGet(-amount)
            gamingPlayers.forEach {  player ->
                player.sendActionBar("<red>- <white>$amount ${type.color.toHexFormat()}${type.displayName}".toComponent())
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_COW_BELL,0.4f,1.2f)
            }
            true
        } else false
    }

    private fun interactVirtualItemHandler(itemStack: ItemStack) {
        when(val material = itemStack.type) {
            Material.IRON_INGOT -> add(VirtualResourceType.METAL,1.0 * itemStack.amount)
            Material.GOLD_INGOT -> add(VirtualResourceType.METAL,2.0 * itemStack.amount)
            Material.DIAMOND -> add(VirtualResourceType.METAL,6.0 * itemStack.amount)
            Material.STONE -> add(VirtualResourceType.STONE,2.0 * itemStack.amount)
            Material.COBBLESTONE -> add(VirtualResourceType.STONE,2.0 * itemStack.amount)
            Material.DEEPSLATE -> add(VirtualResourceType.STONE,4.0 * itemStack.amount)
            Material.COBBLED_DEEPSLATE -> add(VirtualResourceType.STONE,4.0 * itemStack.amount)
            else -> {
                if(material.isWood()) add(VirtualResourceType.WOOD,2.0 * itemStack.amount)
            }
        }

    }
    @EventHandler
    fun onInteract(e: PlayerDropItemEvent) {
        if(e.player.isSneaking) {
            val itemStack = e.itemDrop.itemStack
            if(itemStack.type.isVirtualResource()) {
                interactVirtualItemHandler(itemStack)
                e.itemDrop.remove()
            }
        }
    }
}