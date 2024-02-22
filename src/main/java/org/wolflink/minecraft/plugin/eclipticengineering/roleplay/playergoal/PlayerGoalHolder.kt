package org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal

import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.extension.isDisguiser
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger

object PlayerGoalHolder {
    private val randomPlayerGoals = setOf(
        CantJump::class.java,
        DeathOfFalling::class.java,
        EveryOneHitMe::class.java,
        HitEveryOne::class.java,
        LowerHead::class.java,
        PlaceShrieker::class.java,
        PreventBuildStructure::class.java,
        StayAtStructure::class.java,
        PlaceMeme::class.java,
    )
    // 完成次数
    private val finishCounter = mutableMapOf<UUID,AtomicInteger>()
    private val disguiserGoals = mutableMapOf<UUID,PlayerGoal>()
    // 今日已刷新过目标的玩家，每日重置
    private val refreshCache = mutableSetOf<UUID>()
    fun clearCache() {
        disguiserGoals.clear()
        refreshCache.clear()
    }
    fun getPlayerGoal(player: Player) = disguiserGoals[player.uniqueId]

    fun refreshPlayerGoal(player: Player) {
        if(!player.isDisguiser()) {
            player.sendMessage("$MESSAGE_PREFIX 你不是幽匿伪装者，无法刷新目标。")
            return
        }
        if(refreshCache.contains(player.uniqueId)) {
            player.sendMessage("$MESSAGE_PREFIX 今天你已经刷新过目标了。")
            return
        }
        refreshCache.add(player.uniqueId)
        val playerGoal = randomPlayerGoals.random().getConstructor(Player::class.java).newInstance(player)
        disguiserGoals[player.uniqueId] = playerGoal
        playerGoal.enable()
    }
}