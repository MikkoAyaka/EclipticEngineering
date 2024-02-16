package org.wolflink.minecraft.plugin.eclipticengineering.resource

import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.isVirtualResource
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.isWood
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent

/**
 * 团队虚拟背包
 * 存放各种虚拟物资
 * 当玩家 SHIFT+右键 可虚拟化的物资时，将自动存储到团队虚拟背包中
 */
object VirtualTeamInventory: Listener {
    var metal = 0
        private set
    var stone = 0
        private set
    var wood = 0
        private set
    @Synchronized private fun addMetal(amount: Int) {
        metal += amount
        gamingPlayers.forEach {  player ->
            player.sendActionBar("<green>+ <white>$amount <aqua>金属".toComponent())
            player.playSound(player, Sound.BLOCK_CHEST_OPEN,1f,1.5f)
        }
    }
    @Synchronized private fun takeMetal(amount: Int): Boolean {
        return if(metal >= amount) {
            metal -= amount
            gamingPlayers.forEach {  player ->
                player.sendActionBar("<red>- <white>$amount <aqua>金属".toComponent())
                player.playSound(player, Sound.BLOCK_CHEST_CLOSE,1f,1.5f)
            }
            true
        } else false
    }
    @Synchronized private fun addStone(amount: Int) {
        stone += amount
        gamingPlayers.forEach { player ->
            player.sendActionBar("<green>+ <white>$amount <gray>石料".toComponent())
            player.playSound(player, Sound.BLOCK_CHEST_OPEN,1f,1.5f)
        }
    }
    @Synchronized private fun takeStone(amount: Int): Boolean {
        return if(stone >= amount) {
            stone -= amount
            gamingPlayers.forEach {  player ->
                player.sendActionBar("<red>- <white>$amount <gray>石料".toComponent())
                player.playSound(player, Sound.BLOCK_CHEST_CLOSE,1f,1.5f)
            }
            true
        } else false
    }
    @Synchronized private fun addWood(amount: Int) {
        wood += amount
        gamingPlayers.forEach { player ->
            player.sendActionBar("<green>+ <white>$amount <yellow>木材".toComponent())
            player.playSound(player, Sound.BLOCK_CHEST_OPEN,1f,1.5f)
        }
    }
    @Synchronized private fun takeWood(amount: Int): Boolean {
        return if(wood >= amount) {
            wood -= amount
            gamingPlayers.forEach {  player ->
                player.sendActionBar("<red>- <white>$amount <yellow>木材".toComponent())
                player.playSound(player, Sound.BLOCK_CHEST_CLOSE,1f,1.5f)
            }
            true
        } else false
    }

    private fun interactVirtualItemHandler(player: Player, itemStack: ItemStack) {
        when(val material = itemStack.type) {
            Material.IRON_INGOT -> addMetal(5)
            Material.GOLD_INGOT -> addMetal(10)
            Material.DIAMOND -> addMetal(30)
            Material.STONE -> addStone(1)
            Material.COBBLESTONE -> addStone(1)
            Material.DEEPSLATE -> addStone(2)
            Material.COBBLED_DEEPSLATE -> addStone(2)
            else -> {
                if(material.isWood()) addWood(1)
                else throw IllegalStateException("开发时缺省虚拟资源：${material.name}")
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