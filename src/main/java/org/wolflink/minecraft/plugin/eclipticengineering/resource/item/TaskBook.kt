package org.wolflink.minecraft.plugin.eclipticengineering.resource.item

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.wolflink.minecraft.plugin.eclipticengineering.Quality
import org.wolflink.minecraft.plugin.eclipticengineering.SpecialItemType
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.PRIMARY_TEXT_COLOR
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.SECONDARY_TEXT_COLOR
import org.wolflink.minecraft.plugin.eclipticengineering.extension.createSpecialItem
import org.wolflink.minecraft.plugin.eclipticengineering.stage.goal.Goal
import org.wolflink.minecraft.plugin.eclipticengineering.stage.goal.GoalHolder
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent

object TaskBook: Listener {
    private val defaultItem = Material.BOOK.createSpecialItem(
        SpecialItemType.SPECIAL_ITEM,
        Quality.UNIQUE,
        "任务书",
        true,
        listOf("    <yellow>右键以刷新任务列表")
    )
    fun give(player: Player) {
        player.inventory.addItem(defaultItem)
        player.sendActionBar("$MESSAGE_PREFIX 你拿到了任务书".toComponent())
        player.playSound(player, Sound.ENTITY_VILLAGER_AMBIENT,1f,1.5f)
    }
    private var lastUpdateMeta: ItemMeta = defaultItem.itemMeta
    private fun update(player: Player,bookItem: ItemStack) {
        val goal = GoalHolder.nowGoal ?: return
        val newMeta = Material.BOOK.createSpecialItem(
            SpecialItemType.SPECIAL_ITEM,
            Quality.UNIQUE,
            "任务书",
            true,
            goal.finishConditions.map {
                if(it.check()) "<green>☑ ${SECONDARY_TEXT_COLOR}<italic>${it.description}</italic>"
                else "<white>☐ ${PRIMARY_TEXT_COLOR}${it.description}"
            }
        ).itemMeta
        bookItem.itemMeta = newMeta
        lastUpdateMeta = newMeta

        player.sendActionBar("$MESSAGE_PREFIX 任务书已刷新".toComponent())
        player.playSound(player, Sound.ENTITY_VILLAGER_AMBIENT,1f,1.5f)
    }
    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {
        if(e.action.isRightClick && e.item?.itemMeta == lastUpdateMeta) {
            update(e.player,e.item!!)
        }
    }
}