package org.wolflink.minecraft.plugin.eclipticengineering.resource.item

import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.Quality
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.SpecialItemType
import org.wolflink.minecraft.plugin.eclipticengineering.extension.createSpecialItem
import org.wolflink.minecraft.plugin.eclipticengineering.extension.isDisguiser
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal.PlayerGoal
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal.PlayerGoalHolder
import org.wolflink.minecraft.plugin.eclipticstructure.config.PRIMARY_TEXT_COLOR
import org.wolflink.minecraft.plugin.eclipticstructure.config.SECONDARY_TEXT_COLOR
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import java.util.*

object DisguiserBook : InteractiveItem(
    "幽匿伪装者 帮助手册",
    Material.BOOK.createSpecialItem(
        SpecialItemType.SPECIAL_ITEM,
        Quality.UNIQUE,
        "幽匿伪装者 帮助手册",
        false,
        listOf(
            "    ${PRIMARY_TEXT_COLOR}获胜条件：完成至少 ${PlayerGoalHolder.DISGUISER_WIN_GOAL_COUNT} 个每日目标，并存活至第 ${DayNightHandler.BOSS_DAY} 天。",
            "",
            "    ${PRIMARY_TEXT_COLOR}幽匿伪装者拥有所有能力(不包括职业被动)，",
            "    ${PRIMARY_TEXT_COLOR}每天到来都会从帮助手册获得一个随机目标，",
            "    ${PRIMARY_TEXT_COLOR}努力去完成它吧，但不要暴露了你的身份。",
            "    ${PRIMARY_TEXT_COLOR}如果没能按时完成目标，没关系，",
            "    ${PRIMARY_TEXT_COLOR}你还可以在最后带走几个人陪葬！",
            "",
            "    <yellow>手持该手册，右键以获取目标",
        )
    )
) {
    private var lastUpdateTime = 0L
    override fun update(player: Player, itemStack: ItemStack) {
        if(!player.isDisguiser()) return
        if (Calendar.getInstance().timeInMillis - lastUpdateTime <= 10 * 1000) {
            player.sendActionBar("$MESSAGE_PREFIX <yellow>目标刷新冷却中，请稍后再尝试".toComponent())
            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1f, 1.5f)
            return
        }
        lastUpdateTime = Calendar.getInstance().timeInMillis

        val playerGoal: PlayerGoal = PlayerGoalHolder.getPlayerGoal(player) ?: run {
            PlayerGoalHolder.refreshPlayerGoal(player)
            PlayerGoalHolder.getPlayerGoal(player)
        } ?: return
        val newMeta = Material.BOOK.createSpecialItem(
            SpecialItemType.SPECIAL_ITEM,
            Quality.UNIQUE,
            "幽匿伪装者 帮助手册",
            false,
            listOf(
                when (playerGoal.status) {
                    PlayerGoal.Status.FINISHED -> "<green>☑ $SECONDARY_TEXT_COLOR<st><italic>${playerGoal.formatDescription()}</italic></st>"
                    PlayerGoal.Status.FAILED -> "<red>☒ $SECONDARY_TEXT_COLOR<st><italic>${playerGoal.formatDescription()}</italic></st>"
                    PlayerGoal.Status.IN_PROGRESS -> "<white>☐ ${PRIMARY_TEXT_COLOR}${playerGoal.formatDescription()}"
                }
            )
        ).itemMeta
        itemStack.itemMeta = newMeta
        lastUpdateMeta = newMeta

        player.sendActionBar("$MESSAGE_PREFIX <green>帮助手册已刷新".toComponent())
        player.playSound(player, Sound.ENTITY_VILLAGER_AMBIENT, 1f, 1.5f)
    }
}