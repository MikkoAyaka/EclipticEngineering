package org.wolflink.minecraft.plugin.eclipticengineering.resource.item

import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.Quality
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.SpecialItemType
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.PRIMARY_TEXT_COLOR
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.SECONDARY_TEXT_COLOR
import org.wolflink.minecraft.plugin.eclipticengineering.extension.createSpecialItem
import org.wolflink.minecraft.plugin.eclipticengineering.stage.goal.Goal
import org.wolflink.minecraft.plugin.eclipticengineering.stage.goal.GoalHolder
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import java.util.Calendar

object TaskBook: InteractiveItem(
    Material.BOOK.createSpecialItem(
        SpecialItemType.SPECIAL_ITEM,
        Quality.UNIQUE,
        "任务书",
        true,
        listOf("    <yellow>右键以刷新任务列表")
    )
) {
    private var lastUpdateTime = 0L
    override fun update(player: Player, itemStack: ItemStack) {
        if(GoalHolder.nowGoal?.status == Goal.Status.PREPARING) {
            player.sendActionBar("$MESSAGE_PREFIX <yellow>新的目标将在准备时间结束后到来".toComponent())
            player.playSound(player, Sound.ENTITY_VILLAGER_NO,1f,1.5f)
            return
        }
        if(Calendar.getInstance().timeInMillis - lastUpdateTime <= 10 * 1000) {
            player.sendActionBar("$MESSAGE_PREFIX <yellow>任务书刷新冷却中，请稍后再尝试".toComponent())
            player.playSound(player, Sound.ENTITY_VILLAGER_NO,1f,1.5f)
            return
        }
        lastUpdateTime = Calendar.getInstance().timeInMillis
        val goal = GoalHolder.nowGoal ?: return
        if(goal.status == Goal.Status.PREPARING) return
        val newMeta = Material.BOOK.createSpecialItem(
            SpecialItemType.SPECIAL_ITEM,
            Quality.UNIQUE,
            "任务书",
            true,
            goal.finishConditions.map {
                if(it.isSatisfy()) "<green>☑ ${SECONDARY_TEXT_COLOR}<st><italic>${it.description}</italic></st>"
                else "<white>☐ ${PRIMARY_TEXT_COLOR}${it.description}"
            }
        ).itemMeta
        itemStack.itemMeta = newMeta
        lastUpdateMeta = newMeta
        // 所有条件通过
        if(goal.finishConditions.all { it.isSatisfy() }) goal.finish()

        player.sendActionBar("$MESSAGE_PREFIX <green>任务书已刷新".toComponent())
        player.playSound(player, Sound.ENTITY_VILLAGER_AMBIENT,1f,1.5f)
    }
}